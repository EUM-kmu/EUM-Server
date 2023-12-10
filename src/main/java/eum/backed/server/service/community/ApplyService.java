package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final UsersRepository usersRepository;
    private final MarketPostRepository marketPostRepository;
    private final ProfileRepository profileRepository;
    private final ApplyResponseDTO applyResponseDTO;
    private final ChatService chatService;

    private final ChatRoomRepository chatRoomRepository;


    public APIResponse doApply(Long postId,ApplyRequestDTO.Apply applyRequest, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("Invalid email"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid postId"));
        if(getMarketPost.isDeleted()) throw new IllegalArgumentException("Deleted post");
        if(getMarketPost.getUser() == getUser) throw new IllegalArgumentException("자기 게시글에는 신청할수 없습니다");
        if(getMarketPost.getMarketType()== MarketType.PROVIDE_HELP && getMarketPost.getPay() > getUser.getUserBankAccount().getBalance())
            throw new IllegalArgumentException("잔액보다 큰 요구 햇살");
        if(getMarketPost.getCurrentAcceptedPeople() >= getMarketPost.getMaxNumOfPeople()) throw new RuntimeException("최대 신청자 수를 넘었습니다");
        if (applyRepository.existsByUserAndMarketPost(getUser, getMarketPost)) throw new IllegalArgumentException("이미 신청했음");
        Apply apply = Apply.toEntity(applyRequest.getIntroduction(), getUser, getMarketPost);
//        getMarketPost.addCurrentAcceptedPeople();
        applyRepository.save(apply);
        marketPostRepository.save(getMarketPost);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }

    public APIResponse<List<ApplyResponseDTO.ApplyListResponse>> getApplyList(Long postId) {
//        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("Invalid email"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid id"));
//        List<MarketPost> marketPosts = marketPostRepository.findByUserAndIsDeletedFalseOrderByCreateDateDesc(getUser).orElse(Collections.emptyList()); //로그인 유저가 작성한 게시글 목록 조회
        List<ApplyResponseDTO.ApplyListResponse> getAllApplicants = findByTransactionPosts(getMarketPost);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, getAllApplicants);
    }
    private List<ApplyResponseDTO.ApplyListResponse> findByTransactionPosts(MarketPost marketPost){
        List<ApplyResponseDTO.ApplyListResponse> applyListResponses = new ArrayList<>();
        List<Apply> applies = applyRepository.findByMarketPostOrderByCreateDateDesc(marketPost).orElse(Collections.emptyList());
        for(Apply apply : applies){
//                해당 신청과 매핑되는 신청자 프로필 조회
                Profile getApplicantProfile = profileRepository.findByUser(apply.getUser()).orElseThrow(() -> new NullPointerException("프로필이 없는 유저"));
                ApplyResponseDTO.ApplyListResponse singleApplyResponseDTO = applyResponseDTO.newApplyListResponse(marketPost, apply.getUser(), getApplicantProfile,apply);
                applyListResponses.add(singleApplyResponseDTO);
        }
        return applyListResponses;
    }

    public APIResponse accept(List<Long> applyIds, String email) {
        Users getUser = usersRepository.findByEmail(email). orElseThrow(() -> new NullPointerException("Invalid email"));
        applyIds.stream().forEach(applyId -> {
            Apply getApply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid applyId"));
            if (getApply.getMarketPost().getUser() != getUser) throw new IllegalArgumentException("해당 게시글에 대한 권한이 없다");
            if(getApply.getIsAccepted() == true || getApply.getStatus() == eum.backed.server.domain.community.apply.Status.TRADING_CANCEL) throw new IllegalArgumentException("이미 선정했더나 과거 거래 취소를 했던 사람입니다");
            MarketPost marketPost = getApply.getMarketPost();
            getApply.updateAccepted(true);
            getApply.updateStatus(eum.backed.server.domain.community.apply.Status.TRADING);
            marketPost.addCurrentAcceptedPeople();
            if(marketPost.getCurrentAcceptedPeople() == marketPost.getMaxNumOfPeople()) {
                marketPost.updateStatus(Status.RECRUITMENT_COMPLETED);
            }
            marketPostRepository.save(marketPost);
            applyRepository.save(getApply);
            try {
                chatService.createChatRoomWithFireStore(applyId);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, "선정성공, 채팅방 개설 완료");
    }

    public APIResponse unApply(Long postId, Long applyId, String email) {
        Users getUser = usersRepository.findByEmail(email). orElseThrow(() -> new NullPointerException("Invalid email"));
        Apply getApply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid applyId"));
        if(getApply.getMarketPost().getMarketPostId() != postId) throw new IllegalArgumentException("invalid postId");
        if(getApply.getUser() != getUser) throw new IllegalArgumentException("신청 취소할 권한이 없습니다");
        if(getApply.getIsAccepted() == true) throw new IllegalArgumentException("이미 선정되서 취소할 수 없습니다");
        applyRepository.delete(getApply);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);

    }

    public APIResponse cancel(Long postId, Long chatId, String email) {
        Users getUser = usersRepository.findByEmail(email). orElseThrow(() -> new NullPointerException("Invalid email"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("해딩 채팅방이 없습니다"));
        if(chatRoom.getMarketPost().getMarketPostId() != postId) throw new IllegalArgumentException("invalid postId");
        MarketPost getMarketPost = chatRoom.getMarketPost();
        Apply getApply = applyRepository.findByUserAndMarketPost(chatRoom.getApplicant(), getMarketPost).orElseThrow(()->new IllegalArgumentException("신청한 이력이 없는데 채팅방이 있다"));
        if(!(getUser == chatRoom.getApplicant() || getUser == chatRoom.getPostWriter() )) throw new IllegalArgumentException("활동 파기할수있는 권한이 없습니다");
//        블록 상태 처리
        chatRoom.upDateBlocked(true);
        cancel(getApply);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);

    }

    public void blockedAction(Users blocker, Users blocked) {
        List<Apply> applies = new ArrayList<>();
        List<MarketPost> myPosts = marketPostRepository.findByUserAndIsDeletedFalse(blocker).orElse(Collections.emptyList());
        List<Apply> tradingList = applyRepository.findTradingAppliesByApplicantAndPostIn(blocked,myPosts).orElse(Collections.emptyList());
        applies.addAll(tradingList);

        List<MarketPost> opponentsPosts = marketPostRepository.findByUserAndIsDeletedFalse(blocked).orElse(Collections.emptyList());
        List<Apply> tradedList = applyRepository.findTradingAppliesByApplicantAndPostIn(blocker,opponentsPosts).orElse(Collections.emptyList());
        applies.addAll(tradedList);

        for (Apply apply:applies){
            if(apply.getStatus() == eum.backed.server.domain.community.apply.Status.WAITING){
                applyRepository.delete(apply);
            } else if (apply.getStatus() == eum.backed.server.domain.community.apply.Status.TRADING) {
                cancel(apply);
            }
        }
    }

    public void withdrawalApply(Users getUser) {
        List<Apply> applies = applyRepository.findByUser(getUser).orElse(Collections.emptyList());
        for(Apply apply:applies){
            if(apply.getStatus() == eum.backed.server.domain.community.apply.Status.WAITING){
                applyRepository.delete(apply);
            } else if (apply.getStatus() == eum.backed.server.domain.community.apply.Status.TRADING) {
                cancel(apply);
            }
        }
    }
    private void cancel(Apply getApply){
        getApply.updateStatus(eum.backed.server.domain.community.apply.Status.TRADING_CANCEL);
        getApply.updateAccepted(false);
        applyRepository.save(getApply);

        getApply.getMarketPost().subCurrentAcceptedPeople();
        getApply.getMarketPost().updateStatus(Status.RECRUITING);
        marketPostRepository.save(getApply.getMarketPost());
    }
}

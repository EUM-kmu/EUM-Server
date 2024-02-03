package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.ApplyRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.controller.community.DTO.response.ApplyResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.auth.user.UsersRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * 지원하기
     * @param postId
     * @param applyRequest
     * @param userId
     * @return
     */
    public APIResponse doApply(Long postId,ApplyRequestDTO.Apply applyRequest, Long userId) {
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new NullPointerException("Invalid userId"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid postId"));

        if(getMarketPost.isDeleted()) throw new IllegalArgumentException("Deleted post");
        if(getMarketPost.getUser() == getUser) throw new IllegalArgumentException("자기 게시글에는 신청할수 없습니다");
        if(getMarketPost.getMarketType()== MarketType.PROVIDE_HELP && getMarketPost.getPay() > getUser.getUserBankAccount().getBalance())
            throw new IllegalArgumentException("잔액보다 큰 요구 햇살"); //지원하려는 게시글의 요구 햇살이 내 잔액보다 클때
        if(getMarketPost.getCurrentAcceptedPeople() >= getMarketPost.getMaxNumOfPeople()) throw new RuntimeException("최대 신청자 수를 넘었습니다");
        if (applyRepository.existsByUserAndMarketPost(getUser, getMarketPost)) throw new IllegalArgumentException("이미 신청했음");
        Apply apply = Apply.toEntity(applyRequest.getIntroduction(), getUser, getMarketPost);
        applyRepository.save(apply);
        marketPostRepository.save(getMarketPost);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }

    /**
     * 게시글 별 지원리스트 조회
     * @param postId 조회할 게시글 id
     * @return 지원리스트
     */
    public APIResponse<List<ApplyResponseDTO.ApplyListResponse>> getApplyList(Long postId) {
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid id"));
        List<ApplyResponseDTO.ApplyListResponse> getAllApplicants = findByMarketPosts(getMarketPost);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, getAllApplicants);
    }

    /**
     * 햇터 게시글로 찾은 지원리스트 DTO로 변환
     * @param marketPost
     * @return 지원 리스트 DTO
     */
    private List<ApplyResponseDTO.ApplyListResponse> findByMarketPosts(MarketPost marketPost){
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

    /**
     * 지언 수락
     * @param applyIds 수락할 지원 id들
     * @param userId
     * @return
     */
    public APIResponse accept(List<Long> applyIds, Long userId) {
        Users getUser = usersRepository.findById(userId). orElseThrow(() -> new NullPointerException("Invalid email"));
        applyIds.stream().forEach(applyId -> {
            Apply getApply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid applyId"));

            if (getApply.getMarketPost().getUser() != getUser) throw new IllegalArgumentException("해당 게시글에 대한 권한이 없다");
            if(getApply.getIsAccepted() == true || getApply.getStatus() == eum.backed.server.domain.community.apply.Status.TRADING_CANCEL) throw new IllegalArgumentException("이미 선정했더나 과거 거래 취소를 했던 사람입니다");

            MarketPost marketPost = getApply.getMarketPost();
            getApply.updateAccepted(true); //수락
            getApply.updateStatus(eum.backed.server.domain.community.apply.Status.TRADING); //지원 상태 변경
            marketPost.addCurrentAcceptedPeople(); //게시글에 반영
            if(marketPost.getCurrentAcceptedPeople() == marketPost.getMaxNumOfPeople()) {
                marketPost.updateStatus(Status.RECRUITMENT_COMPLETED); //정원이 다차면 완료 처리
            }

            marketPostRepository.save(marketPost);
            applyRepository.save(getApply);

            try {
                chatService.createChatRoomWithFireStore(applyId); //채팅방 생성
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, "선정성공, 채팅방 개설 완료");
    }

    /**
     * 선정 전 지원 취소
     * @param postId
     * @param applyId
     * @param userId
     * @return
     */
    public APIResponse unApply(Long postId, Long applyId, Long userId) {
        Users getUser = usersRepository.findById(userId). orElseThrow(() -> new NullPointerException("Invalid email"));
        Apply getApply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid applyId"));

        if(getApply.getMarketPost().getMarketPostId() != postId) throw new IllegalArgumentException("invalid postId");
        if(getApply.getUser() != getUser) throw new IllegalArgumentException("신청 취소할 권한이 없습니다");
        if(getApply.getIsAccepted() == true) throw new IllegalArgumentException("이미 선정되서 취소할 수 없습니다");

        applyRepository.delete(getApply);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);

    }

    /**
     * 선정 후 활동 파기
     * @param postId
     * @param chatId
     * @param userId
     * @return
     */
    public APIResponse cancel(Long postId, Long chatId, Long userId) {
        Users getUser = usersRepository.findById(userId). orElseThrow(() -> new NullPointerException("Invalid email"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("해딩 채팅방이 없습니다"));
        if(chatRoom.getMarketPost().getMarketPostId() != postId) throw new IllegalArgumentException("invalid postId");

        MarketPost getMarketPost = chatRoom.getMarketPost();
        Apply getApply = applyRepository.findByUserAndMarketPost(chatRoom.getApplicant(), getMarketPost).orElseThrow(()->new IllegalArgumentException("신청한 이력이 없는데 채팅방이 있다"));

        if(!(getUser == chatRoom.getApplicant() || getUser == chatRoom.getPostWriter() )) throw new IllegalArgumentException("활동 파기할수있는 권한이 없습니다");
//        채팅 금지 처리
        chatRoom.upDateBlocked(true);
        cancel(getApply);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);

    }

    /**
     * 차단 할 때 지원 리스트 반영
     * @param blocker 차단하는 유저
     * @param blocked 처단 당한 유저
     */
    public void blockedAction(Users blocker, Users blocked) {
        List<Apply> applies = new ArrayList<>();
        List<MarketPost> myPosts = marketPostRepository.findByUserAndIsDeletedFalse(blocker).orElse(Collections.emptyList()); //차단한 유저의 게시글(나)
        List<Apply> tradingList = applyRepository.findTradingAppliesByApplicantAndPostIn(blocked,myPosts).orElse(Collections.emptyList()); // 개사굴애소 자원리스트 조회
        applies.addAll(tradingList);

        List<MarketPost> opponentsPosts = marketPostRepository.findByUserAndIsDeletedFalse(blocked).orElse(Collections.emptyList()); //차단된 유저의 게시글(상대)
        List<Apply> tradedList = applyRepository.findTradingAppliesByApplicantAndPostIn(blocker,opponentsPosts).orElse(Collections.emptyList());
        applies.addAll(tradedList);

        cancelByType(applies); //지원 상태 타입별 처리
    }

    /**
     * 탈퇴 유저의 지원 리스트 처리
     * @param getUser
     */
    public void withdrawalApply(Users getUser) {
        List<Apply> applies = applyRepository.findByUser(getUser).orElse(Collections.emptyList()); //탈퇴한 유저의 지원 리트 조회
        cancelByType(applies);
    }

    /**
     * 취소 DB에 반영
     * @param getApply
     */
    private void cancel(Apply getApply){
        getApply.updateStatus(eum.backed.server.domain.community.apply.Status.TRADING_CANCEL); //상태변경
        getApply.updateAccepted(false);
        applyRepository.save(getApply);

        getApply.getMarketPost().subCurrentAcceptedPeople(); //게시글 반영
        getApply.getMarketPost().updateStatus(Status.RECRUITING); //모집중으로 변경
        marketPostRepository.save(getApply.getMarketPost());
    }

    /**
     * 선정 전, 선정 후 타입별 지원 처리
     * @param applies
     */
    private void cancelByType(List<Apply> applies){
        for (Apply apply:applies) {
            if (apply.getStatus() == eum.backed.server.domain.community.apply.Status.WAITING) {
                applyRepository.delete(apply);
            } else if (apply.getStatus() == eum.backed.server.domain.community.apply.Status.TRADING) {
                cancel(apply);
            }
        }
    }
}

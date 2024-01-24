package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.MarketPostRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.controller.community.DTO.request.enums.ServiceType;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.controller.community.DTO.response.MarketPostResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.category.MarketCategoryRepository;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketPostService {
    private final MarketPostRepository marketPostRepository;
    private final MarketCategoryRepository marketCategoryRepository;
    private final ScrapRepository scrapRepository;
    private final MarketPostResponseDTO marketPostResponseDTO;
    private final UsersRepository usersRepository;

    private final ApplyRepository applyRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 게시글 작성 메소드
     * @param marketCreate : 작성한 게시글 내용
     * @return : 성공 여부
     * @throws ParseException : 활동 날짜 parsing 예외
     */
    public APIResponse<MarketPostResponseDTO.MarketPostResponse> create(MarketPostRequestDTO.MarketCreate marketCreate, Long userId) throws ParseException {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketCategory getMarketCategory = marketCategoryRepository.findByContents(marketCreate.getCategory()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));

        Long pay = Long.valueOf(marketCreate.getVolunteerTime()); //금액은 활동시간과 같은 값 설정
        if(marketCreate.getMarketType()==MarketType.REQUEST_HELP && user.getUserBankAccount().getBalance() < pay * marketCreate.getMaxNumOfPeople()) throw new IllegalArgumentException("잔액보다 크게 돈 설정 불가"); //잔액에 따른 예외처리

        MarketPost marketPost = MarketPost.toEntity(marketCreate,pay,user,getMarketCategory);
        MarketPost getMarketPost = marketPostRepository.save(marketPost);

        MarketPostResponseDTO.MarketPostResponse marketPostResponse = MarketPostResponseDTO.toMarketPostResponse(getMarketPost,0,0);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,marketPostResponse);
    }

    /**
     * 게시글 삭제
     * @param postId : 삭제할 게시글 id
     * @return : 성공 여부
     */
    public  APIResponse delete(Long postId,Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));

        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketPost.updateDeleted(true); //논리삭제
        List<Scrap> scraps = scrapRepository.findByMarketPost(getMarketPost).orElse(Collections.emptyList()); // 삭제된 게시글 스크랩 취소 처리
        scrapRepository.deleteAll(scraps);

        marketPostRepository.save(getMarketPost);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

    /**
     * 게시글 업데이트
     * @param postId : 게시글 id
     * @param marketUpdate : 수정된 게시글 내용
     * @return : 성동 여부
     * @throws ParseException : 활동날짜 parsing 예외
     */
    public  APIResponse<MarketPostResponseDTO.MarketPostResponse> update(Long postId, MarketPostRequestDTO.MarketUpdate marketUpdate, Long userId) throws ParseException {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        //수정
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN);
        getMarketPost.updateTitle(marketUpdate.getTitle());
        getMarketPost.updateContents(marketUpdate.getContent());
        getMarketPost.updateSlot(marketUpdate.getSlot());
        getMarketPost.updateStartDate(simpleDateFormat.parse(marketUpdate.getStartDate()));
        getMarketPost.updateLocation(marketUpdate.getLocation());
        Long pay = Long.valueOf(marketUpdate.getVolunteerTime());
        if(getMarketPost.getMarketType()==MarketType.REQUEST_HELP && (user.getUserBankAccount().getBalance() < (pay * marketUpdate.getMaxNumOfPeople()))) throw new IllegalArgumentException("잔액보다 크게 돈 설정 불가");
        getMarketPost.updateVolunteerTime(marketUpdate.getVolunteerTime());
        getMarketPost.updateMaxNumOfPeople(marketUpdate.getMaxNumOfPeople());
        getMarketPost.updatePay(pay);

        MarketPost updatedMarketPost = marketPostRepository.save(getMarketPost);
        MarketPostResponseDTO.MarketPostResponse marketPostResponse = MarketPostResponseDTO.toMarketPostResponse(updatedMarketPost,updatedMarketPost.getMarketComments().size(),updatedMarketPost.getApplies().size());
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,marketPostResponse);

    }

    /**
     * 게시글 상태 업데이트
     * @param postId
     * @param status
     * @return : 성공 여부
     */
    public  APIResponse updateState(Long postId,Status status, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");

        getMarketPost.updateStatus(status);
        marketPostRepository.save(getMarketPost);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,"게시글 상태 변경");
    }

    /**
     * 게시글 정보 + 해당 게시글 댓글 들 조회
     * @param postId
     * @param commentResponses
     * @return 게시글 정보 + 댓글 리스트 조회 , 로그인한 유저 활동 조회(스크랩 여부, 지원여부, 작성자 여부)
     */
    public  APIResponse<MarketPostResponseDTO.MarketPostWithComment> getMarketPostWithComment(Long postId, Long userId, List<CommentResponseDTO.CommentResponse> commentResponses) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));

//        유저활동
        Boolean isScrap = scrapRepository.existsByMarketPostAndUser(getMarketPost,user);
        Boolean isApply = applyRepository.existsByUserAndMarketPost(user, getMarketPost);
        eum.backed.server.domain.community.apply.Status tradingStatus = eum.backed.server.domain.community.apply.Status.NONE;

        if(isApply){
            tradingStatus = applyRepository.findByUserAndMarketPost(user,getMarketPost).get().getStatus();
        }
        MarketPostResponseDTO.MarketPostWithComment singlePostResponse = marketPostResponseDTO.toMarketPostWithComment(user,getMarketPost,commentResponses,isApply,isScrap,tradingStatus);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,singlePostResponse);

    }

    /**
     * 필터 조회(검색어, 카테고리, 게시글 유형(모집중, 모집완료), 모집중
     * @param keyword : 검색어
     * @param category : 카테고리
     * @param marketType : 게시글 유형
     * @param status : 모집중
     * @param pageable
     * @param blockedUsers : 차단한, 차단된 유저들은 제외하고 조회
     * @return : 검색어(게시글 전체) > 카테고리 > 카테고리 내 게시글 유형 , 카테고리 내 모집중
     */
    public  APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> findByFilter(String keyword, String category, MarketType marketType, Status status, Pageable pageable, List<Users> blockedUsers) {
//        검색 키워드 있을떄
        if (!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword,blockedUsers);
        }
        MarketCategory marketCategory = marketCategoryRepository.findByContents(category).orElse(null);

        List<MarketPost> marketPosts = marketPostRepository.findByFilters(marketCategory, marketType,status).orElse(Collections.emptyList()); //조건에 맞는 리스트 조회
        List<MarketPostResponseDTO.MarketPostResponse> marketPostResponses = getAllPostResponse(marketPosts); //리스트 dto

        return APIResponse.of(SuccessCode.SELECT_SUCCESS,marketPostResponses);
     }

    /**
     * 내 게시글 활동
     * @param serviceType : scrap, postlist,apply(내 스크랩, 내가 작성한 게시글, 지원)
     * @param blockedUsers
     * @return : 게시글 정보 조회 차단된, 차단한 유저 제외
     */
    public APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> findByServiceType(ServiceType serviceType, Long userId, List<Users> blockedUsers) {
        if(serviceType == ServiceType.scrap){
            return findByScrap(userId,blockedUsers);
        } else if (serviceType == ServiceType.postlist) {
            return getMyPosts(userId);
        }else if(serviceType == ServiceType.apply){
            return getMyApplyList(userId);
        }
        return null;
    }

    /**
     * 게시글 객체 리스트를 받았을때 dto로 전환하는 함수
     * @param marketPosts : jpa로 조회한 게시글 리스트
     * @return
     */
    private List<MarketPostResponseDTO.MarketPostResponse> getAllPostResponse(List<MarketPost> marketPosts){
        List<MarketPostResponseDTO.MarketPostResponse> marketPostResponses = new ArrayList<>();
        for (MarketPost marketPost : marketPosts) {
            MarketPostResponseDTO.MarketPostResponse marketPostResponse = marketPostResponseDTO.toMarketPostResponse(marketPost,marketPost.getMarketComments().size(),marketPost.getApplies().size());
            marketPostResponses.add(marketPostResponse);
        }
        return marketPostResponses;
    }

    /**
     * 스크랩 게시글 조회 함수
     * @param blockedUsers
     * @return
     */
    private  APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> findByScrap(Long userId, List<Users> blockedUsers) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        List<Scrap> scraps = scrapRepository.findScrapPostsForUser(user,blockedUsers).orElse(Collections.emptyList());

        List<MarketPostResponseDTO.MarketPostResponse> marketPostResponses = new ArrayList<>();
        for (Scrap scrap : scraps) {
            MarketPost marketPost = scrap.getMarketPost();
            MarketPostResponseDTO.MarketPostResponse marketPostResponse = MarketPostResponseDTO.toMarketPostResponse(marketPost,marketPost.getMarketComments().size(),marketPost.getApplies().size());
            marketPostResponses.add(marketPostResponse);
        }
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, marketPostResponses);
    }

    /**
     * 내가 작성한 게시글
     * @return
     */
    private APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> getMyPosts(Long userId) {
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        List<MarketPost> marketPosts = marketPostRepository.findByUserAndIsDeletedFalseOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());

        List<MarketPostResponseDTO.MarketPostResponse> marketPostResponses = getAllPostResponse(marketPosts);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, marketPostResponses);
    }

    /**
     * 검색한 게시글
     * @param keyWord : 검색할 키워드
     * @param blockedUsers
     * @return
     */
    private APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> findByKeyWord(String keyWord, List<Users> blockedUsers) {
        List<MarketPost> marketPosts = marketPostRepository.findByKeywords(keyWord,blockedUsers).orElse(Collections.emptyList());
        List<MarketPostResponseDTO.MarketPostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, transactionPostDTOs);
    }

    /**
     * 내가 지원한 게시글
     * @return
     */
    private APIResponse<List<MarketPostResponseDTO.MarketPostResponse>> getMyApplyList(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        List<Apply> applies = applyRepository.findByUserIsDeletedFalse(user).orElse(Collections.emptyList());

        List<MarketPost> marketPosts = new ArrayList<>();
        for(Apply apply : applies){
            MarketPost marketPost = apply.getMarketPost();
            marketPosts.add(marketPost);
        }
        List<MarketPostResponseDTO.MarketPostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, transactionPostDTOs);
    }

    /**
     * 거래 상태 업데이트 함수 -> 지원 데이터에 상태 업데이트로 바뀌어야함
     * @param chatRoomId : 채팅방 Id
     */
    public void updateStatusCompleted(Long chatRoomId){
        MarketPost marketPost = chatRoomRepository.findById(chatRoomId).orElseThrow(()->new NullPointerException("invalid chatRoomdId")).getMarketPost();
        marketPost.updateStatus(Status.TRANSACTION_COMPLETED);
        marketPostRepository.save(marketPost);
    }
}

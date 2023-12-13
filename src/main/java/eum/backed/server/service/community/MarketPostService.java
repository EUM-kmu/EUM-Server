package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.controller.community.dto.request.enums.ServiceType;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.category.MarketCategoryRepository;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.comment.MarketCommentRepository;
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
    private final PostResponseDTO postResponseDTO;
    private final UsersRepository usersRepository;

    private final ApplyRepository applyRepository;
    private final ChatRoomRepository chatRoomRepository;

    public APIResponse<PostResponseDTO.MarketPostResponse> create(PostRequestDTO.MarketCreate marketCreate, String email) throws Exception {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketCategory getMarketCategory = marketCategoryRepository.findByContents(marketCreate.getCategory()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN);
        Long pay = Long.valueOf(marketCreate.getVolunteerTime());
        if(marketCreate.getMarketType()==MarketType.REQUEST_HELP && user.getUserBankAccount().getBalance() < pay * marketCreate.getMaxNumOfPeople()) throw new IllegalArgumentException("잔액보다 크게 돈 설정 불가");
        MarketPost marketPost = MarketPost.builder()
                .title(marketCreate.getTitle())
                .contents(marketCreate.getContent())
                .startDate(simpleDateFormat.parse(marketCreate.getStartTime()))
                .slot(marketCreate.getSlot())
                .pay(pay)
                .isDeleted(false)
                .location(marketCreate.getLocation())
                .volunteerTime(marketCreate.getVolunteerTime())
                .marketType(marketCreate.getMarketType())
                .maxNumOfPeople(marketCreate.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .marketCategory(getMarketCategory)
                .build();
        MarketPost getMarketPost = marketPostRepository.save(marketPost);
        PostResponseDTO.MarketPostResponse marketPostResponse = PostResponseDTO.singleMarketPost(getMarketPost,0);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,marketPostResponse);
    }


    public  APIResponse delete(Long postId,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketPost.updateDeleted(true);
        List<Scrap> scraps = scrapRepository.findByMarketPost(getMarketPost).orElse(Collections.emptyList());
        scrapRepository.deleteAll(scraps);
        marketPostRepository.save(getMarketPost);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

    public  APIResponse<PostResponseDTO.MarketPostResponse> update(Long postId,PostRequestDTO.MarketUpdate marketUpdate, String email) throws ParseException {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN);
        getMarketPost.updateTitle(marketUpdate.getTitle());
        getMarketPost.updateContents(marketUpdate.getContent());
        getMarketPost.updateSlot(marketUpdate.getSlot());
        getMarketPost.updateStartDate(simpleDateFormat.parse(marketUpdate.getStartDate()));
        getMarketPost.updateLocation(marketUpdate.getLocation());
        Long pay = Long.valueOf(marketUpdate.getVolunteerTime());
        if(getMarketPost.getMarketType()==MarketType.REQUEST_HELP && (user.getUserBankAccount().getBalance() < (pay * marketUpdate.getMaxNumOfPeople()))) throw new IllegalArgumentException("잔액보다 크게 돈 설정 불가");
        log.info(String.valueOf(pay * marketUpdate.getMaxNumOfPeople()));
        getMarketPost.updateVolunteerTime(marketUpdate.getVolunteerTime());
        getMarketPost.updateMaxNumOfPeople(marketUpdate.getMaxNumOfPeople());
        getMarketPost.updatePay(pay);
        MarketPost updatedMarketPost = marketPostRepository.save(getMarketPost);
        PostResponseDTO.MarketPostResponse marketPostResponse = PostResponseDTO.singleMarketPost(updatedMarketPost,updatedMarketPost.getMarketComments().size());
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,marketPostResponse);

    }

    public  APIResponse updateState(Long postId,Status status, String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketPost.updateStatus(status);
        marketPostRepository.save(getMarketPost);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,"게시글 상태 변경");
    }
    public  APIResponse<PostResponseDTO.TransactionPostWithComment> getTransactionPostWithComment(Long postId,String email,List<CommentResponseDTO.CommentResponse> commentResponses) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        Boolean isScrap = scrapRepository.existsByMarketPostAndUser(getMarketPost,user);
        Boolean isApply = applyRepository.existsByUserAndMarketPost(user, getMarketPost);
        eum.backed.server.domain.community.apply.Status tradingStatus = eum.backed.server.domain.community.apply.Status.NONE;
        if(isApply){
            tradingStatus = applyRepository.findByUserAndMarketPost(user,getMarketPost).get().getStatus();
        }
        PostResponseDTO.TransactionPostWithComment singlePostResponse = postResponseDTO.newTransactionPostWithComment(user,getMarketPost,commentResponses,isApply,isScrap,tradingStatus);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,singlePostResponse);

    }
    public  APIResponse<List<PostResponseDTO.PostResponse>> findByFilter(String keyword, String category, MarketType marketType, Status status, String email, Pageable pageable,List<Users> blockedUsers) {
        if (!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword,blockedUsers);
        }
            MarketCategory marketCategory = marketCategoryRepository.findByContents(category).orElse(null);

        List<MarketPost> marketPosts = getMarketPosts(marketCategory, marketType, status,blockedUsers);
        List<PostResponseDTO.PostResponse> postResponses = getAllPostResponse(marketPosts);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS,postResponses);
     }


//    }
    private List<MarketPost> getMarketPosts(MarketCategory marketCategory, MarketType marketType, Status status,List<Users> blockedUsers) {
        return marketPostRepository.findByFilters(marketCategory, marketType,status,blockedUsers).orElse(Collections.emptyList());
    }

    private  APIResponse<List<PostResponseDTO.PostResponse>> findByScrap(String email,List<Users> blockedUsers) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<Scrap> scraps = scrapRepository.findScrapPostsForUser(user,blockedUsers).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (Scrap scrap : scraps) {
            MarketPost marketPost = scrap.getMarketPost();
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(marketPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, postResponseArrayList);
    }
    private List<PostResponseDTO.PostResponse> getAllPostResponse(List<MarketPost> marketPosts){
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (MarketPost marketPost : marketPosts) {
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(marketPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return postResponseArrayList;
    }


    private APIResponse<List<PostResponseDTO.PostResponse>> getMyPosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<MarketPost> marketPosts = marketPostRepository.findByUserAndIsDeletedFalseOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, transactionPostDTOs);
    }

    private APIResponse<List<PostResponseDTO.PostResponse>> findByKeyWord(String keyWord, List<Users> blockedUsers) {
//        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<MarketPost> marketPosts = marketPostRepository.findByTitleContainingAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc( keyWord,blockedUsers).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, transactionPostDTOs);
    }


    public APIResponse<List<PostResponseDTO.PostResponse>> findByServiceType(ServiceType serviceType, String email,List<Users> blockedUsers) {
        if(serviceType == ServiceType.scrap){
            return findByScrap(email,blockedUsers);
        } else if (serviceType == ServiceType.postlist) {
            return getMyPosts(email);
        }else if(serviceType == ServiceType.apply){
            return getMyApplyList(email);
        }
        return null;
    }

    private APIResponse<List<PostResponseDTO.PostResponse>> getMyApplyList(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<Apply> applies = applyRepository.findByUserIsDeletedFalse(getUser).orElse(Collections.emptyList());
        List<MarketPost> marketPosts = new ArrayList<>();
        for(Apply apply : applies){
            MarketPost marketPost = apply.getMarketPost();
            marketPosts.add(marketPost);
        }
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, transactionPostDTOs);
    }
    public void updateStatusCompleted(Long chatRoomId){
        MarketPost marketPost = chatRoomRepository.findById(chatRoomId).orElseThrow(()->new NullPointerException("invalid chatRoomdId")).getMarketPost();
        marketPost.updateStatus(Status.TRANSACTION_COMPLETED);
        marketPostRepository.save(marketPost);
    }
}

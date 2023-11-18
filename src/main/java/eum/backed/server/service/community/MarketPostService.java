package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
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
import eum.backed.server.domain.community.comment.TransactionComment;
import eum.backed.server.domain.community.comment.TransactionCommentRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.region.DONG.TownshipRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MarketPostService {
    private final MarketPostRepository marketPostRepository;
    private final MarketCategoryRepository marketCategoryRepository;
    private final ScrapRepository scrapRepository;
    private final PostResponseDTO postResponseDTO;
    private final UsersRepository usersRepository;
    private final TownshipRepository townShipRepository;
    private final TransactionCommentRepository transactionCommentRepository;

    private final ApplyRepository applyRepository;
    private final ChatRoomRepository chatRoomRepository;

    public DataResponse create(PostRequestDTO.Create create,String email) throws Exception {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Township townShip = townShipRepository.findByName(create.getDong()).orElseThrow(() -> new NullPointerException("Invalid address"));
        MarketCategory getMarketCategory = marketCategoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.KOREAN);
        Long pay = Long.valueOf(create.getVolunteerTime());
        if(create.getMarketType()==MarketType.REQUEST_HELP && user.getUserBankAccount().getBalance() < pay) throw new IllegalArgumentException("잔액보다 크게 돈 설정 불가");
        MarketPost marketPost = MarketPost.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startDate(simpleDateFormat.parse(create.getStartTime()))
                .slot(create.getSlot())
                .pay(pay)
                .township(townShip)
                .location(create.getLocation())
                .volunteerTime(create.getVolunteerTime())
                .marketType(create.getMarketType())
                .maxNumOfPeople(create.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .marketCategory(getMarketCategory)
                .build();
        marketPostRepository.save(marketPost);
        return new DataResponse<>().success("게시글 작성 성공");
    }


    public DataResponse delete(Long postId,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        marketPostRepository.delete(getMarketPost);
        return new DataResponse().success("게시글 삭제 성공");
    }

    public DataResponse update(PostRequestDTO.Update update,String email) throws ParseException {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Township townShip = townShipRepository.findByName(update.getDong()).orElseThrow(() -> new NullPointerException("Invalid address"));
        MarketPost getMarketPost = marketPostRepository.findById(update.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);
        getMarketPost.updateTitle(update.getTitle());
        getMarketPost.updateContents(update.getContent());
        getMarketPost.updateSlot(update.getSlot());
        getMarketPost.updateStartDate(simpleDateFormat.parse(update.getStartDate()));
        getMarketPost.updateLocation(update.getLocation());
        getMarketPost.updateDong(townShip);
        marketPostRepository.save(getMarketPost);
        return new DataResponse<>().success("게시글 수정 성공");

    }

    public DataResponse updateState(Long postId,Status status, String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getMarketPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketPost.updateStatus(status);
        marketPostRepository.save(getMarketPost);
        return new DataResponse<>().success("게시글 상태 수정 성공");
    }
    public DataResponse<PostResponseDTO.TransactionPostWithComment> getTransactionPostWithComment(Long postId,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        List<TransactionComment> transactionComments = transactionCommentRepository.findByMarketPostOrderByCreateDateDesc(getMarketPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> commentResponses = transactionComments.stream().map(transactionComment -> {
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(postId)
                    .commentId(transactionComment.getTransactionCommentId())
                    .commentNickName(transactionComment.getUser().getProfile().getNickname())
                    .commentUserAddress(transactionComment.getUser().getProfile().getTownship().getName())
                    .isPostWriter(getMarketPost.getUser() == transactionComment.getUser())
                    .createdTime(transactionComment.getCreateDate())
                    .commentContent(transactionComment.getContent()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        PostResponseDTO.TransactionPostWithComment singlePostResponse = postResponseDTO.newTransactionPostWithComment(user,getMarketPost,commentResponses);
        return new DataResponse<>(singlePostResponse).success(singlePostResponse, "id에 따른 게시글 정보조회 + 댓글");

    }
    public DataResponse<List<PostResponseDTO.PostResponse>> findByFilter(String keyword, Long categoryId, MarketType marketType, Status status, String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Township townShip = user.getProfile().getTownship();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, townShip);
        }
        MarketCategory marketCategory = marketCategoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid categoryId"));
        List<MarketPost> marketPosts = getMarketPosts(marketCategory, townShip, marketType, status);
        List<PostResponseDTO.PostResponse> postResponses = getAllPostResponse(marketPosts);

        return new DataResponse<>(postResponses).success(postResponses, "상태별 데이터 조회 성공");
    }

    private List<MarketPost> getMarketPosts(MarketCategory marketCategory, Township townShip, MarketType marketType, Status status) {
        if (marketType == MarketType.PROVIDE_HELP) {
            if (status == Status.RECRUITING) {
                return marketPostRepository.findByMarketCategoryAndTownshipAndMarketTypeAndStatusOrderByCreateDateDesc(marketCategory, townShip, MarketType.PROVIDE_HELP, status).orElse(Collections.emptyList());
            } else {
                return marketPostRepository.findByMarketCategoryAndTownshipAndMarketTypeOrderByCreateDateDesc(marketCategory, townShip, MarketType.PROVIDE_HELP).orElse(Collections.emptyList());
            }
        } else if (marketType == MarketType.REQUEST_HELP) {
            if (status == Status.RECRUITING) {
                return marketPostRepository.findByMarketCategoryAndTownshipAndMarketTypeAndStatusOrderByCreateDateDesc(marketCategory, townShip, MarketType.REQUEST_HELP, status).orElse(Collections.emptyList());
            } else {
                return marketPostRepository.findByMarketCategoryAndTownshipAndMarketTypeOrderByCreateDateDesc(marketCategory, townShip, MarketType.REQUEST_HELP).orElse(Collections.emptyList());
            }
        } else {
            if (status == Status.RECRUITING) {
                return marketPostRepository.findByMarketCategoryAndTownshipAndStatusOrderByCreateDateDesc(marketCategory, townShip, status).orElse(Collections.emptyList());
            } else {
                return marketPostRepository.findByMarketCategoryAndTownshipOrderByCreateDateDesc(marketCategory, townShip).orElse(Collections.emptyList());
            }
        }
    }

    private DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<Scrap> scraps = scrapRepository.findByUserOrderByCreateDateDesc(user).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (Scrap scrap : scraps) {
            MarketPost marketPost = scrap.getMarketPost();
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(marketPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return new DataResponse<>(postResponseArrayList).success(postResponseArrayList, "내가 스크랩한 게시글 조회)");
    }
    private List<PostResponseDTO.PostResponse> getAllPostResponse(List<MarketPost> marketPosts){
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (MarketPost marketPost : marketPosts) {
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(marketPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return postResponseArrayList;
    }


    private DataResponse<List<PostResponseDTO.PostResponse>> getMyPosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<MarketPost> marketPosts = marketPostRepository.findByUserOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return new DataResponse<>(transactionPostDTOs).success(transactionPostDTOs,"내가 작성한 거래게시글 조회 성공");
    }

    private DataResponse<List<PostResponseDTO.PostResponse>> findByKeyWord(String keyWord, Township getTownship) {
//        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<MarketPost> marketPosts = marketPostRepository.findByTownshipAndTitleContainingOrderByCreateDateDesc(getTownship, keyWord).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return new DataResponse<>(transactionPostDTOs).success(transactionPostDTOs,"키워드 조회");
    }


    public DataResponse<List<PostResponseDTO.PostResponse>> findByServiceType(ServiceType serviceType, String email) {
        if(serviceType == ServiceType.scrap){
            return findByScrap(email);
        } else if (serviceType == ServiceType.market) {
            return getMyPosts(email);
        }else if(serviceType == ServiceType.apply){
            return getMyApplyList(email);
        }
        return null;
    }

    private DataResponse<List<PostResponseDTO.PostResponse>> getMyApplyList(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<Apply> applies = applyRepository.findByUser(getUser).orElse(Collections.emptyList());
        List<MarketPost> marketPosts = new ArrayList<>();
        for(Apply apply : applies){
            MarketPost marketPost = apply.getMarketPost();
            marketPosts.add(marketPost);
        }
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(marketPosts);
        return new DataResponse<>(transactionPostDTOs).success(transactionPostDTOs, "나의 거래게시글 신청 리스트");
    }
    public void updateStatusCompleted(Long chatRoomId){
        MarketPost marketPost = chatRoomRepository.findById(chatRoomId).orElseThrow(()->new NullPointerException("invalid chatRoomdId")).getMarketPost();
        marketPost.updateStatus(Status.TRANSACTION_COMPLETED);
        marketPostRepository.save(marketPost);
    }
}

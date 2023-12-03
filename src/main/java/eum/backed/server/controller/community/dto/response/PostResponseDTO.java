package eum.backed.server.controller.community.dto.response;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MarketPostResponse{
        private Long postId;
        private String title;
        private String content;
        private String  createdDate;
        //        마감시간은 없애고, 시간은 오전, 오후, 상관없음.
        private Status status;
        private String startTime;
        private Slot slot;
        private String location;
        private Long pay;
        private int volunteerTime;
        private MarketType marketType;
        private int maxNumOfPeople;
        private String category;
        private int commentCount;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "거래 게시글 모음 ")
    public static class PostResponse {
        private Long postId;
        private String title;
        private String location;
        private Long pay;
        private int volunteerTime;
        private MarketType marketType;
        private String category;
        private Status status;
        private String createdDate;
        private int commentCount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "id 별 게시글 + 댓글 ")
    public static class TransactionPostWithComment {
        private ProfileResponseDTO.UserInfo writerInfo;
        private UserCurrentStatus userCurrentStatus;
        private MarketType marketType;
        private Long postId;
        private String title;
        private String content;
        private Slot slot;
        private String startDate;
        private Long pay;
        private int volunteerTime;
        private String location;
        private int currentApplicant;
        private int maxNumOfPeople;
        private String category;
        private Status status;
        private String createdDate;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    @Builder
    @Getter
    @Setter
    private static class UserCurrentStatus{
        private Boolean isWriter;
        private Boolean isApplicant;
        private Boolean isScrap;
    }
    public static PostResponseDTO.MarketPostResponse singleMarketPost(MarketPost marketPost){
        LocalDateTime createUTC = LocalDateTime.parse(marketPost.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
        Instant instant = marketPost.getStartDate().toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime startUTC = LocalDateTime.parse(localDateTime.toString(), DateTimeFormatter.ISO_DATE_TIME);

        // UTC 시간을 한국 시간대로 변환
        ZonedDateTime koreaZonedCreateime = createUTC.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime koreaZonedStartTime = startUTC.atZone(ZoneId.of("Asia/Seoul"));

        // 한국 시간대로 포맷팅
        String formattedCreateTime = koreaZonedCreateime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        String formattedStartTime = koreaZonedStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return MarketPostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(formattedCreateTime)
                .startTime(formattedStartTime)
                .slot(marketPost.getSlot())
                .content(marketPost.getContents())
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(0)
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .build();
    }
    public static PostResponseDTO.PostResponse newPostResponse(MarketPost marketPost){
        LocalDateTime utcDateTime = LocalDateTime.parse(marketPost.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);

        // UTC 시간을 한국 시간대로 변환
        ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));

        // 한국 시간대로 포맷팅
        String formattedDateTime = koreaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return PostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(formattedDateTime)
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(marketPost.getMarketComments().size())
                .build();
    }
    public TransactionPostWithComment newTransactionPostWithComment(Users user, MarketPost marketPost, List<CommentResponseDTO.CommentResponse> commentResponses, Boolean isApply, Boolean isScrap){
        UserCurrentStatus userCurrentStatus = UserCurrentStatus.builder().isScrap(isScrap)
                .isWriter(user == marketPost.getUser())
                .isApplicant(isApply).build();
        LocalDateTime createUTC = LocalDateTime.parse(marketPost.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
        Instant instant = marketPost.getStartDate().toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime startUTC = LocalDateTime.parse(localDateTime.toString(), DateTimeFormatter.ISO_DATE_TIME);

        // UTC 시간을 한국 시간대로 변환
        ZonedDateTime koreaZonedCreateime = createUTC.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime koreaZonedStartTime = startUTC.atZone(ZoneId.of("Asia/Seoul"));

        // 한국 시간대로 포맷팅
        String formattedCreateTime = koreaZonedCreateime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        String formattedStartTime = koreaZonedStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return TransactionPostWithComment.builder()
                .writerInfo(ProfileResponseDTO.toUserInfo(marketPost.getUser()))
                .userCurrentStatus(userCurrentStatus)
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .content(marketPost.getContents())
                .startDate(formattedCreateTime)
                .createdDate(formattedStartTime)
                .pay(marketPost.getPay())
                .location(marketPost.getLocation())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .currentApplicant(marketPost.getCurrentAcceptedPeople())
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .slot(marketPost.getSlot())
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .build();
    }
}

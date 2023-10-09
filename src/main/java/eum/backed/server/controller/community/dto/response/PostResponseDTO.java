package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.post.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
@Component
@RequiredArgsConstructor
public class PostResponseDTO {
    private final Time time;

    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "전체 데이터 정렬")
    public static class PostResponse {
        private Long postId;
        private String title;
        private String content;
        private String startDate;
        private String endDate;
        private int pay;
        private String location;
        private int volunteerTime;
        private boolean needHelper;
        private int maxNumOfPeople;
        private String category;
        private Status status;
        private String customCreatedTime;
    }
    public PostResponseDTO.PostResponse newPostResponse(Post post){
        Date date = Date.from(post.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREAN);
        return PostResponseDTO.PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContents())
                .startDate(simpleDateFormat.format(post.getStartDate()))
                .endDate(simpleDateFormat.format(post.getEndDate()))
                .customCreatedTime(time.calculateTime(date))
                .pay(post.getPay())
                .location(post.getLocation())
                .volunteerTime(post.getVolunteerTime())
                .needHelper(post.getNeedHelper())
                .maxNumOfPeople(post.getMaxNumOfPeople())
                .category(post.getCategory().getContents())
                .status(post.getStatus())
                .build();
    }
}

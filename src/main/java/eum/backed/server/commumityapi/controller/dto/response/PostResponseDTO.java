package eum.backed.server.commumityapi.controller.dto.response;

import eum.backed.server.commumityapi.controller.dto.request.PostRequestDTO;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.post.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Locale;
@Component
public class PostResponseDTO {

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
        private boolean isHelper;
        private int maxNumOfPeople;
        private String category;
        private Status status;
    }
    public PostResponseDTO.PostResponse newPostResponse(Post post){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREAN);
        return PostResponseDTO.PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContents())
                .startDate(simpleDateFormat.format(post.getStartDate()))
                .endDate(simpleDateFormat.format(post.getEndDate()))
                .pay(post.getPay())
                .location(post.getLocation())
                .volunteerTime(post.getVolunteerTime())
                .isHelper(post.getIsHelper())
                .maxNumOfPeople(post.getMaxNumOfPeople())
                .category(post.getCategory().getContents())
                .status(post.getStatus())
                .build();
    }
}

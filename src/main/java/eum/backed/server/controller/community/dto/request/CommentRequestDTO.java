package eum.backed.server.controller.community.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class CommentRequestDTO {
    @Setter
    @Getter
    public static class Create{
        @ApiModelProperty(example = "좋은 글이에요",value = "댓글 내용")
        @NotEmpty
        private String content;
    }

    @Getter
    @Setter
    public static class Update{
        @ApiModelProperty(example = "좋은 글이에요",value = "댓글 내용")
        @NotEmpty
        private String content;

    }

}

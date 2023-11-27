package eum.backed.server.controller.community.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ApplyRequestDTO {
    @Getter
    @Setter
    public static class Apply{

        @ApiModelProperty(example = "저는 요리를 잘합니다",value = "지원 한마디")
        private String introduction;

    }


}

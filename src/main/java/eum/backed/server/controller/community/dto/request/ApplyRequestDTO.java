package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ApplyRequestDTO {
    @Getter
    @Setter
    public static class Apply{
        @NotEmpty
        private Long postId;
        private String introduction;
    }

    @Getter
    @Setter
    public static class AcceptList{
        private List<Long> applyIds;
    }


}

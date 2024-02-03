package eum.backed.server.controller.community.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ApplyRequestDTO {
    @Getter
    @Setter
    public static class Apply{

        private String introduction;

    }

    @Getter
    @Setter
    public static class AcceptList {
        private List<Long> applyIds;
    }
}

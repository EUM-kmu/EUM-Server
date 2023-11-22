package eum.backed.server.controller.community.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RegionResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class Region{
        List<String> region;
    }
}

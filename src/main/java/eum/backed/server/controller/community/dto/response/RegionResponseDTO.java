package eum.backed.server.controller.community.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class RegionResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class Region{
        List<Map<Long,String>> region;
    }
}

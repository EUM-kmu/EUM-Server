package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.enums.RegionType;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.region.DONG.TownshipRepository;
import eum.backed.server.domain.community.region.GU.Town;
import eum.backed.server.domain.community.region.GU.TownRepository;
import eum.backed.server.domain.community.region.SI.City;
import eum.backed.server.domain.community.region.SI.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final CityRepository cityRepository;
    private final TownRepository townRepository;
    private final TownshipRepository townshipRepository;

    public DataResponse<RegionResponseDTO.Region> getRegionByType(String si, String gu) {
        if(si == null || si.isBlank()){
            return getCity();
        } else if (gu == null || gu.isBlank()) {
            return getGu(si);
        }
        return getDong(gu);
    }

    private DataResponse<RegionResponseDTO.Region> getDong(String gu) {
        Town town = townRepository.findByName(gu).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        List<Township> townshipList = townshipRepository.findByTown(town).orElse(Collections.emptyList());
        List<String> nameList = townshipList.stream().map(township -> township.getName()).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return new DataResponse<>(region).success(region,"동 리스트 조회");
    }

    private DataResponse<RegionResponseDTO.Region> getGu(String si) {
        City city = cityRepository.findByName(si).orElseThrow(()->new NullPointerException("초기 데이터 미 설정"));
        List<Town> townList = townRepository.findByCity(city).orElse(Collections.emptyList());
        List<String> nameList = townList.stream().map(town -> town.getName()).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return new DataResponse<>(region).success(region,"구 리스트 조회");
    }

    private DataResponse<RegionResponseDTO.Region> getCity(){
        List<City> cityList = cityRepository.findAll();
        List<String> nameList = cityList.stream().map(city -> city.getName()).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return new DataResponse<>(region).success(region,"시 리스트 조회");
    }
}

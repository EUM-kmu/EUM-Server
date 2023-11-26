package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.region.DONG.TownshipRepository;
import eum.backed.server.domain.community.region.GU.Town;
import eum.backed.server.domain.community.region.GU.TownRepository;
import eum.backed.server.domain.community.region.SI.City;
import eum.backed.server.domain.community.region.SI.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final CityRepository cityRepository;
    private final TownRepository townRepository;
    private final TownshipRepository townshipRepository;

    public APIResponse<RegionResponseDTO.Region> getRegionByType(Long si, Long gu) {
        if(si == null ){
            return getCity();
        } else if (gu == null ) {
            return getGu(si);
        }
        return getDong(gu);
    }

    private APIResponse<RegionResponseDTO.Region> getDong(Long gu) {
        Town town = townRepository.findById(gu).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        List<Township> townshipList = townshipRepository.findByTown(town).orElse(Collections.emptyList());
        List<Map<Long, String>> nameList = townshipList.stream()
                .map(township -> {
                    Map < Long, String > map = new HashMap<>();
                    map.put(township.getTownshipId(), township.getName());
                    return map;}
                ).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,region);
    }

    private APIResponse<RegionResponseDTO.Region> getGu(Long si) {
        City city = cityRepository.findById(si).orElseThrow(()->new NullPointerException("초기 데이터 미 설정"));
        List<Town> townList = townRepository.findByCity(city).orElse(Collections.emptyList());
        List<Map<Long, String>> nameList = townList.stream()
                .map(town -> {
                    Map < Long, String > map = new HashMap<>();
                    map.put(town.getTownId(), town.getName());
                    return map;}
                ).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,region);
    }

    private APIResponse<RegionResponseDTO.Region> getCity(){
        List<City> cityList = cityRepository.findAll();
        List<Map<Long, String>> nameList = cityList.stream()
                .map(city -> {
                    Map < Long, String > map = new HashMap<>();
                    map.put(city.getCityId(), city.getName());
                    return map;}
                ).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,region);
    }
}

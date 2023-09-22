//package com.klasha.service;
//
//
//import com.klasha.dto.responseDto.BaseResponse;
//import com.klasha.location.LocationClient;
//import com.klasha.dto.CityResponseDto;
//import com.klasha.dto.LocationFilter;
//import com.klasha.dto.LocationResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class LocationsService {
//    private final LocationClient client;
//    public BaseResponse<List<LocationResponseDto>> filterLocations(LocationFilter locationFilter){
//        return client.filterLocation(locationFilter);
//    }
//    public BaseResponse<List<CityResponseDto>> getCities(){
//        return
//                client.getCities();
//    }
//}

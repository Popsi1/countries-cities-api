package com.dto.resquestDto.city;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityRequest {
    private String country;
    private String state;
}

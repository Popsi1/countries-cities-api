package com.dto.responseDto.state;

import lombok.Data;

import java.util.List;

@Data
public class StateCitiesResponse {
    private String state;
    private List<String> cities;
}

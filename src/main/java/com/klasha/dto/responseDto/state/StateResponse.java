package com.klasha.dto.responseDto.state;

import lombok.Data;

import java.util.List;

@Data
public class StateResponse {
    private String name;
    private String iso3;
    private List<State> states;
}

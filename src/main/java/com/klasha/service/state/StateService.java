package com.klasha.service.state;

import com.klasha.dto.responseDto.HttpBaseResponse;
import com.klasha.dto.responseDto.state.StateResponse;
import com.klasha.dto.resquestDto.FilterCountry;

public interface StateService {
    public HttpBaseResponse<StateResponse> getStates(FilterCountry filterCountry);
}

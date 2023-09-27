package com.service.state;

import com.dto.responseDto.HttpBaseResponse;
import com.dto.responseDto.state.StateResponse;
import com.dto.resquestDto.FilterCountry;

public interface StateService {
    public HttpBaseResponse<StateResponse> getStates(FilterCountry filterCountry);
}

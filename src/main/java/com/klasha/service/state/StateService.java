package com.klasha.service.state;

import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.responseDto.state.StateResponse;
import com.klasha.dto.resquestDto.FilterCountry;

public interface StateService {
    public BaseResponse<StateResponse> getStates(FilterCountry filterCountry);
}

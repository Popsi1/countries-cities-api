package com.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ApiDataResponseDto {
    private boolean status;
    private int code;
    private Object data;
    private String message;
    private Object timestamp;
}

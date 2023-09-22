package com.klasha.dto.responseDto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean error;
    private String msg;
    private  T data;
}

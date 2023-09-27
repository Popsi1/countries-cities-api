package com.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpBaseResponse<T> {
    private boolean error;
    private String msg;
    private  T data;
}

package com.utils;

import com.dto.responseDto.ApiDataResponseDto;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class DataResponseUtils {
        public static ApiDataResponseDto successResponse(String message, Object data) {
                return ApiDataResponseDto.builder()
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .message(message)
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .build();
        }

        public static ApiDataResponseDto errorResponse(HttpStatus status, String message) {
                return ApiDataResponseDto.builder()
                        .timestamp(LocalDateTime.now())
                        .message(message)
                        .status(false)
                        .code(status.value())
                        .build();
        }
}

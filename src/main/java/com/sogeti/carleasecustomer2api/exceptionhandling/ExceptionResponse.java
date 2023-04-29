package com.sogeti.carleasecustomer2api.exceptionhandling;

import lombok.Data;

@Data
public class ExceptionResponse {
    private int status;
    private String message;
    private long timestamp;
}

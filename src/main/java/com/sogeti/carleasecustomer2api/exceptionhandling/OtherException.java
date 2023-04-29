package com.sogeti.carleasecustomer2api.exceptionhandling;

public class OtherException extends RuntimeException{
    public OtherException() {
        super();
    }
    public OtherException(String message) {
        super(message);
    }

    public OtherException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtherException(Throwable cause) {
        super(cause);
    }
}

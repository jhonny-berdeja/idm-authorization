package com.jberdeja.idm_authorization.exception;

public class JwtValidationIdmException extends RuntimeException{
    public JwtValidationIdmException(String message, Throwable e){
        super(message, e);
    }
}

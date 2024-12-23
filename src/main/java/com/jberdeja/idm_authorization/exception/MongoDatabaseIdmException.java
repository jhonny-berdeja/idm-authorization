package com.jberdeja.idm_authorization.exception;

public class MongoDatabaseIdmException extends RuntimeException{
    public MongoDatabaseIdmException(String message, Throwable e) {
        super(message, e);
    }
}

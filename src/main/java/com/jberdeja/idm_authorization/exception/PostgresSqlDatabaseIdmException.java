package com.jberdeja.idm_authorization.exception;

public class PostgresSqlDatabaseIdmException extends RuntimeException{
    public PostgresSqlDatabaseIdmException(String message, Throwable e) {
        super(message, e);
    }
}

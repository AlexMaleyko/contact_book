package com.itechart.maleiko.contact_book.business.dao.exceptions;

public class DataSourceInitializationException extends RuntimeException {
    public DataSourceInitializationException(String message){
        super(message);
    }
    public DataSourceInitializationException(String message, Throwable cause){
        super(message, cause);
    }
}

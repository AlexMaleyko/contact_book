package com.itechart.maleiko.contact_book.business.dao.exceptions;

/**
 * Created by Alexey on 16.08.2017.
 */
public class UnsupportedDBMSException extends RuntimeException {
    public UnsupportedDBMSException(){
        super();
    }

    public UnsupportedDBMSException(String message){
        super(message);
    }

    public UnsupportedDBMSException(Throwable cause){
        super(cause);
    }

    public UnsupportedDBMSException(String message, Throwable cause){
        super(message, cause);
    }
}

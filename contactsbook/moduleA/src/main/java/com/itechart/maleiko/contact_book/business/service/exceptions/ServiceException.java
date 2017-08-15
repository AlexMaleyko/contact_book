package com.itechart.maleiko.contact_book.business.service.exceptions;

public class ServiceException extends Exception {
    public ServiceException(){
        super();
    }

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(Throwable cause){
        super(cause);
    }

    public ServiceException(String message, Throwable cause){
        super(message, cause);
    }
}

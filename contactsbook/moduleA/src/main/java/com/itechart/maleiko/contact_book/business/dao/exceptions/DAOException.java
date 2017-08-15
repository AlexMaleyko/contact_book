package com.itechart.maleiko.contact_book.business.dao.exceptions;


public class DAOException extends Exception {
    public DAOException(){
        super();
    }

    public DAOException(String message){
        super(message);
    }

    public DAOException(Throwable cause){
        super(cause);
    }

    public DAOException(String message, Throwable cause){
        super(message, cause);
    }

}

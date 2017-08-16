package com.itechart.maleiko.contact_book.business.utils;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    private PropertiesLoader(){}
    public static Properties load(String fileName) throws DAOException{
        try {
            Properties properties = new Properties();
            properties.load(PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName));
            return properties;
        }catch (IOException e){
            throw new DAOException("Failed loading properties file: " + fileName, e);
        }
    }
}

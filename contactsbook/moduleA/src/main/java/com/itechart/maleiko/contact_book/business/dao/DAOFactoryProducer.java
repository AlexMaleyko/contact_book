package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.exceptions.UnsupportedDBMSException;
import com.itechart.maleiko.contact_book.business.dao.mysql.MySQLDAOFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOFactoryProducer {
    private DAOFactory daoFactory;

    private static DAOFactoryProducer factoryProducerInstance = new DAOFactoryProducer();

    private DAOFactoryProducer() { }

    public static DAOFactoryProducer getInstance(){
        return factoryProducerInstance;
    }

    public DAOFactory createDAOFactory(){
        if(daoFactory == null){
            instantiateDaoFactory();
        }
        return daoFactory;
    }

    private void instantiateDaoFactory(){
        String currentDBMS = determineCurrentDBMS();
        switch (currentDBMS) {
            case "mysql": {
                daoFactory = new MySQLDAOFactory();
                break;
            }
            default : {
                throw new UnsupportedDBMSException("specified DBMS is not supported: " + currentDBMS);
            }
        }
    }
    private String determineCurrentDBMS() {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
        try{
            properties.load(inputStream);
        }catch (IOException e){
            throw new RuntimeException("Can not determine current DBMS");
        }
        return properties.getProperty("currentDBMS").toLowerCase();
    }


}

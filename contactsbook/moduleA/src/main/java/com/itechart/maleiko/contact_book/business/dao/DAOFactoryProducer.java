package com.itechart.maleiko.contact_book.business.dao;

//import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOFactoryProducer {
    private final String DATABASE_PROPERTIES_FILE_NAME = "database.properties";
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
        switch (determineCurrentDBMS()) {
            case "mysql": {
                daoFactory = new MySQLDAOFactory();
                break;
            }
            default : {
                throw new RuntimeException("specified DBMS is not supported: " + determineCurrentDBMS());
            }
        }
    }
    private String determineCurrentDBMS() {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_FILE_NAME);
        try{
            properties.load(inputStream);
        }catch (IOException e){
            throw new RuntimeException("Can not determine current DBMS");
        }
        return properties.getProperty("currentDBMS").toLowerCase();
    }


}

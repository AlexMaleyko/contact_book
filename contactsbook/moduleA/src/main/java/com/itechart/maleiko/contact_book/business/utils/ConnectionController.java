package com.itechart.maleiko.contact_book.business.utils;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionController {
    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);

    private static final String MYSQL_URL = "jdbc:mysql://";

    private static ConnectionController instance = new ConnectionController();

    public static ConnectionController getInstance() {
        return instance;
    }

    private ConnectionController() {
    }

    private DataSource dataSource;
    public Connection provideConnection() throws DAOException{
        Connection connection;
       try {
           if (dataSource == null) {
               initializeDataSource();
           }
           connection = dataSource.getConnection();
       }catch (SQLException e){
           String message = "Error retrieving pooled connection." +
                   " SQLState: " + e.getSQLState() + " Error Code: " + e.getErrorCode();
           throw new DAOException(message, e);
       }
        return connection;
    }

    private void initializeDataSource() throws DAOException {
        if(dataSource == null){
            dataSource = new DataSource();
            Properties properties = getDataSourceProperties();
            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String userName = properties.getProperty("user");
            String password = properties.getProperty("password");
            String dbName = properties.getProperty("db_name");
            String dataSourceURL = MYSQL_URL + host + ":" + port + "/" + dbName+"?useSSL=false";
            dataSource.setUrl(dataSourceURL);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        }
    }

    private Properties getDataSourceProperties() throws DAOException {
        try {
            Properties properties = new Properties();
            properties.load(ConnectionController.class.getClassLoader().getResourceAsStream("database.properties"));
            return properties;
        }catch (IOException e){
            throw new DAOException("Failed loading database properties. ", e);
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        }catch (SQLException e){
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }

    public void rollback(Connection connection){
        try {
            LOGGER.error("Transaction is being rolled back");
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }
}

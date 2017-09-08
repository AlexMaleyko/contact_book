package com.itechart.maleiko.contact_book.business.utils;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DataSourceInitializationException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionController {
    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);

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

    private void initializeDataSource(){
        if(dataSource == null){
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:comp/env");
                dataSource = (DataSource) envContext.lookup("jdbc/contactBook");
            }catch(NamingException e) {
                throw new DataSourceInitializationException("Failed to initialize DataSource. Message: " + e.getMessage(), e);
            }catch (ClassCastException e) {
                String message = "Failed to initialize DataSource." +
                        " Specified datasource configuration was invalid. Message: " + e.getMessage();
                throw new DataSourceInitializationException(message, e);
            }
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        }catch (SQLException e){
            LOGGER.error("Error closing connection. SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }

    public void rollback(Connection connection){
        try {
            LOGGER.error("Transaction is being rolled back");
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.error("Failed rollback transaction. SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }
}

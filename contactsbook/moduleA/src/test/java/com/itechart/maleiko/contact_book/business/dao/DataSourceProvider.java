package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.mysql.PhoneNumberDAOImplTest;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.IOException;
import java.util.Properties;

public class DataSourceProvider {
    private static final String MYSQL_URL = "jdbc:mysql://";

    private static DataSourceProvider instance = new DataSourceProvider();

    public static DataSourceProvider getInstance() {
        return instance;
    }

    private DataSourceProvider() {
    }

    DataSource dataSource;

    public DataSource getDataSource() throws IOException {
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
        return dataSource;
    }

    private Properties getDataSourceProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(PhoneNumberDAOImplTest.class.getClassLoader().getResourceAsStream("database.properties"));
        return properties;
    }
}

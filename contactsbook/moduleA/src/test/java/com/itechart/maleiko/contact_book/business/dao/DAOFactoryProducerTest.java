package com.itechart.maleiko.contact_book.business.dao;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class DAOFactoryProducerTest {
    private DAOFactoryProducer factoryProducer;

    @Before
    public void instantiate() {
        factoryProducer = DAOFactoryProducer.getInstance();
    }

    // createDAOFactory() method uses database.properties. database.properties file specifies mysql as currentDBMS
    @Test
    public void testCreateDAOFactory() throws Exception {
        assertTrue("Created factory must be of type MySQLDAOFactory",
                factoryProducer.createDAOFactory() instanceof MySQLDAOFactory);
    }
}

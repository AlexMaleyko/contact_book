package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.mysql.MySQLDAOFactory;
import org.junit.Before;
import org.junit.Test;

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

package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.DataSourceProvider;
import com.itechart.maleiko.contact_book.business.dao.PhoneNumberDAO;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PhoneNumberDAOImplTest {

    private final String FIND_ALL_QUERY = "SELECT number_id, country_code, operator_code,"+
            "number, type, comment, contact_id FROM phone_number WHERE deletion_date IS NULL";

    private final String DELETE_ALL_QUERY = "DELETE FROM phone_number";

    private PhoneNumberDAO phoneNumberDAO = new PhoneNumberDAOImpl();
    private final Long PERMANENT_CONTACT_ID = 1L;
    private static DataSource dataSource;

    private class MockPhoneNumber extends PhoneNumber{
        @Override
        public boolean equals(Object obj) {
            if(! (obj instanceof PhoneNumber)){
                return false;
            }
            PhoneNumber number = (PhoneNumber) obj;
            return getCountryCode().equals(number.getCountryCode()) &&
                    getOperatorCode().equals(number.getOperatorCode()) &&
                    getNumber().equals(number.getNumber()) &&
                    getType().equals(number.getType()) &&
                    getContactId() == number.getContactId() &&
                    getComment().equals(number.getComment());
        }
    }

    @BeforeClass
    public static void initializeDataSource() throws IOException, Exception {
        DataSourceProvider dataSourceProvider = DataSourceProvider.getInstance();
        dataSource = dataSourceProvider.getDataSource();
    }

    @Before
    @After
    public void cleanPhoneNumberTable() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            deleteAllPhoneNumbers(connection);
        }
    }

    private void deleteAllPhoneNumbers(Connection connection) throws Exception {
        try(PreparedStatement stmt=connection.prepareStatement(DELETE_ALL_QUERY)){
            stmt.executeUpdate();
        }
    }

    @Test
    public void testSave() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            List<PhoneNumber> sampleNumbers = createSamplePhoneNumbers();
            phoneNumberDAO.setConnection(connection);
            phoneNumberDAO.save(sampleNumbers);

            List<PhoneNumber> actualNumbers = retrieveAllPhoneNumbers();

            assertNotNull(actualNumbers);
            assertEquals(actualNumbers.size(), sampleNumbers.size());
            assertArrayEquals(sampleNumbers.toArray(), actualNumbers.toArray());
            connection.close();
        }
    }

    @Test
    public void testUpdate() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            List<PhoneNumber> sampleNumbers = createSamplePhoneNumbers();
            phoneNumberDAO.setConnection(connection);
            phoneNumberDAO.save(sampleNumbers);
            List<PhoneNumber> sampleNumbersWithIds = retrieveAllPhoneNumbers();
            for (int i = 0; i < sampleNumbersWithIds.size(); i++) {
                PhoneNumber number = sampleNumbersWithIds.get(i);
                number.setCountryCode(String.valueOf(i));
                number.setOperatorCode(String.valueOf(i));
                number.setNumber(String.valueOf(i));
                number.setType(String.valueOf("h"));
                number.setComment(String.valueOf(i));
            }
            phoneNumberDAO.update(sampleNumbersWithIds);
            List<PhoneNumber> updatedSampleNumbers = retrieveAllPhoneNumbers();
            assertNotNull(updatedSampleNumbers);
            assertEquals(sampleNumbers.size(), updatedSampleNumbers.size());
            assertArrayEquals(sampleNumbersWithIds.toArray(), updatedSampleNumbers.toArray());
        }
    }

    @Test
    public void testFindByContactId() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            List<PhoneNumber> sampleNumbers = createSamplePhoneNumbers();
            phoneNumberDAO.setConnection(connection);
            phoneNumberDAO.save(sampleNumbers);
            List<PhoneNumber> retrievedNumbers = phoneNumberDAO.findByContactId(PERMANENT_CONTACT_ID);
            assertNotNull(retrievedNumbers);
            assertEquals(sampleNumbers.size(), retrievedNumbers.size());
            assertArrayEquals(sampleNumbers.toArray(), retrievedNumbers.toArray());
        }
    }

    @Test
    public void testDeleteByContactId() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            List<PhoneNumber> sampleNumbers = createSamplePhoneNumbers();
            phoneNumberDAO.setConnection(connection);
            //save method is considered to be working according to testSave() method
            phoneNumberDAO.save(sampleNumbers);
            phoneNumberDAO.deleteByContactId(PERMANENT_CONTACT_ID);
            List<PhoneNumber> retrievedNumbers = retrieveAllPhoneNumbers();
            assertTrue(retrievedNumbers.isEmpty());
        }
    }

    @Test
    public void testDeleteById() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            List<PhoneNumber> sampleNumbers = createSamplePhoneNumbers();
            phoneNumberDAO.setConnection(connection);
            phoneNumberDAO.save(sampleNumbers);
            List<PhoneNumber> sampleNumbersWithId = retrieveAllPhoneNumbers();
            List<Long> ids = sampleNumbersWithId.stream().map(PhoneNumber::getNumberId).collect(Collectors.toList());
            phoneNumberDAO.deleteById(ids);
            List<PhoneNumber> deletionResult = retrieveAllPhoneNumbers();
            assertTrue(deletionResult.isEmpty());
        }
    }

    private List<PhoneNumber> retrieveAllPhoneNumbers() throws Exception{
        List<PhoneNumber> phoneNumbers=new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(FIND_ALL_QUERY)
        ){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                PhoneNumber number = ((PhoneNumberDAOImpl) phoneNumberDAO).generateEntityObjectFromResultSetRow(rs);
                phoneNumbers.add(number);
            }
        }
        return phoneNumbers;
    }

    private List<PhoneNumber> createSamplePhoneNumbers(){
        PhoneNumber sampleNumber1 = new MockPhoneNumber();
        sampleNumber1.setCountryCode("+375");
        sampleNumber1.setOperatorCode("29");
        sampleNumber1.setNumber("1234567");
        sampleNumber1.setType("m");
        sampleNumber1.setContactId(PERMANENT_CONTACT_ID);
        sampleNumber1.setComment("comment1");

        PhoneNumber sampleNumber2 = new MockPhoneNumber();
        sampleNumber2.setCountryCode("+375");
        sampleNumber2.setOperatorCode("17");
        sampleNumber2.setNumber("7654321");
        sampleNumber2.setType("h");
        sampleNumber2.setContactId(PERMANENT_CONTACT_ID);
        sampleNumber2.setComment("comment2");

        List<PhoneNumber> sampleNumbers = new ArrayList<>();
        sampleNumbers.add(sampleNumber1);
        sampleNumbers.add(sampleNumber2);
        return sampleNumbers;
    }
}

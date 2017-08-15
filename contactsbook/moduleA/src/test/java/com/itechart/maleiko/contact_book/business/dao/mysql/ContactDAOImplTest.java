package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.ContactDAO;
import com.itechart.maleiko.contact_book.business.dao.DataSourceProvider;
import com.itechart.maleiko.contact_book.business.dao.PairResultSize;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContactDAOImplTest{
    private ContactDAO contactDAO;
    private final String FIND_ALL_QUERY = "SELECT contact_id, name, surname, patronymic, birth, " +
            "gender, citizenship, marital_status, website, email, " +
            "job, country, city, street, postal_code, profile_picture " +
            "FROM contact WHERE deletion_date IS NULL AND contact_id != 1";
    private final String DELETE_ALL_QUERY = "DELETE FROM contact WHERE contact_id != 1";
    private static DataSource dataSource;

    @BeforeClass
    public static void initializeDataSource() throws IOException, SQLException {
        DataSourceProvider dataSourceProvider = DataSourceProvider.getInstance();
        dataSource = dataSourceProvider.getDataSource();
    }

    @Before
    public void initializeContactDAO(){
        contactDAO = new ContactDAOImpl(new AttachmentDAOImpl(), new PhoneNumberDAOImpl());
    }

    @Before
    @After
    public void cleanContactTable() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            deleteTestContacts(connection);
            deleteTestFolder();
        }
    }

    private void deleteTestContacts(Connection connection) throws SQLException {
        try(PreparedStatement stmt=connection.prepareStatement(DELETE_ALL_QUERY)){
            stmt.executeUpdate();
        }
    }

    private void deleteTestFolder() throws IOException {
        Properties properties = getFileStorageProperties();
        File testFolder = new File(properties.getProperty("profileImageContainer"));
        if (testFolder.exists()) {
            FileUtils.forceDelete(testFolder);
        }
    }

    private Properties getFileStorageProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(PhoneNumberDAOImplTest.class.getClassLoader().getResourceAsStream("fileStorage.properties"));
        return properties;
    }
    @Test
    public void testSave() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            contactDAO.setConnection(connection);
            List<Contact> sampleContacts = createSampleContacts();
            for (Contact contact : sampleContacts) {
                contactDAO.save(contact);
            }
            List<Contact> savedContacts = retrieveAllContacts();
            assertEquals(sampleContacts.size(), savedContacts.size());
            for (int i = 0; i < savedContacts.size(); i++) {
                assertTrue("contacts should be the same: \n"
                                + sampleContacts.get(i) + "\n" + savedContacts.get(i)
                        , sampleContacts.get(i).equalsWithoutIdBirthImage(savedContacts.get(i)));
                Path savedImage = Paths.get(savedContacts.get(i).getProfilePicturePath());
                assertTrue("Image should exist", Files.exists(savedImage));
                assertTrue("Image should be readable", Files.isReadable(savedImage));
            }
        }
    }

    @Test
    public void testUpdate() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            contactDAO.setConnection(connection);
            List<Contact> sampleContacts = createSampleContacts();
            for (Contact contact : sampleContacts) {
                contactDAO.save(contact);
            }

            List<Contact> sampleContactsWithIds = retrieveAllContacts();
            for (int i = 0; i < sampleContactsWithIds.size(); i++) {
                sampleContacts.get(i).setName(String.valueOf(i));
                sampleContacts.get(i).setSurname(String.valueOf(i));
                sampleContacts.get(i).setPatronymic(String.valueOf(i));
            }
            //1st contact will also update its image
            FileItemFactory factory = new DiskFileItemFactory();
            FileItem newImageFile = factory.createItem("fieldName", "image/jpeg", true, "image.jpg");
            OutputStream os = newImageFile.getOutputStream();
            os.write("sadlghdkjfghdfhgsdljkfhgsd;gjw".getBytes());
            os.close();
            sampleContactsWithIds.get(0).setProfileImage(newImageFile);
            for (Contact contact : sampleContactsWithIds) {
                contactDAO.update(contact);
            }

            List<Contact> updatedContacts = retrieveAllContacts();
            assertEquals(sampleContacts.size(), updatedContacts.size());
            for (int i = 0; i < sampleContactsWithIds.size(); i++) {
                assertTrue("contacts should be the same: \n"
                                + sampleContactsWithIds.get(i) + "\n" + updatedContacts.get(i)
                        , sampleContactsWithIds.get(i).equalsWithoutIdBirthImage(updatedContacts.get(i)));
            }
            //check that 1st contact updated its image
            Path oldImage = Paths.get(sampleContactsWithIds.get(0).getProfilePicturePath());
            Path newImage = Paths.get(updatedContacts.get(0).getProfilePicturePath());
            assertTrue("Previous image should be deleted", Files.notExists(oldImage));
            assertTrue("New image should exist", Files.exists(newImage));
            assertTrue("New image should be readable", Files.isReadable(newImage));
        }
    }

    @Test
    public void testFindByGivedParameters() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            contactDAO.setConnection(connection);
            List<Contact> sampleContacts = createSampleContacts();
            for (Contact contact : sampleContacts) {
                contactDAO.save(contact);
            }
            //1st search
            Map<String, Object> fieldValueMaping = new HashMap<>();
            fieldValueMaping.put("fname", "ald");
            fieldValueMaping.put("lname", "apach");
            PairResultSize searchResult =
                    contactDAO.findByGivenParameters(fieldValueMaping);
            assertEquals(1, searchResult.getResultSetSize());
            assertTrue(sampleContacts.get(0).equalsWithoutIdBirthImage(searchResult.getContactList().get(0)));

            //2nd search
            fieldValueMaping.clear();
            fieldValueMaping.put("citizenship", "usa");
            PairResultSize searchResult2 =
                    contactDAO.findByGivenParameters(fieldValueMaping);
            assertEquals(2, searchResult2.getResultSetSize());
            for (int i = 0; i < 2; i++) {
                assertTrue(sampleContacts.get(i).equalsWithoutIdBirthImage(searchResult2.getContactList().get(i)));
            }
            //3rd search
            fieldValueMaping.clear();
            fieldValueMaping.put("fname", "Ald");
            fieldValueMaping.put("lname", "Apach");
            fieldValueMaping.put("patronymic", "ohn");
            fieldValueMaping.put("comparator", "less");
            fieldValueMaping.put("birth", new LocalDate(System.currentTimeMillis() + 10000000000L));//future date
            fieldValueMaping.put("gender", "m");
            fieldValueMaping.put("citizenship", "us");
            fieldValueMaping.put("status", "divorced");
            fieldValueMaping.put("web", "basterds.com");
            fieldValueMaping.put("email", "gmail.com");
            fieldValueMaping.put("job", "veteran");
            fieldValueMaping.put("country", "USA");
            fieldValueMaping.put("city", "LA");
            fieldValueMaping.put("street", "street");
            fieldValueMaping.put("postalCode", "123");
            PairResultSize searchResult3 =
                    contactDAO.findByGivenParameters(fieldValueMaping);
            assertEquals(1, searchResult3.getResultSetSize());
            assertTrue(sampleContacts.get(0).equalsWithoutIdBirthImage(searchResult3.getContactList().get(0)));
        }
    }

    private List<Contact> retrieveAllContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(FIND_ALL_QUERY)
            ){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Contact contact = ((ContactDAOImpl) contactDAO).generateEntityObjectFromResultSetRow(rs);
                contacts.add(contact);
            }
        }
        return contacts;
    }

    private List<Contact> createSampleContacts() throws Exception{
        FileItemFactory factory = new DiskFileItemFactory();
        String stringUsedForExtractingBytes = "uaishfioqegqiweigoweqgfuyqewfboqwbeqywgfbiqweufbhsbvhsadbfoqbeqwui";
        String originalFileName = "originalFileName.jpg";
        String mimeType = "image/jpeg";
        String fieldName = "formFieldName";
        List<FileItem> sampleImages = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            FileItem sampleImage = factory.createItem(fieldName, mimeType, true, originalFileName);
            OutputStream os = sampleImage.getOutputStream();
            os.write(stringUsedForExtractingBytes.getBytes());
            os.close();
            sampleImages.add(sampleImage);
        }

        Contact contact1 = new Contact();
        contact1.setName("Aldo");
        contact1.setSurname("Apache");
        contact1.setPatronymic("John");
        contact1.setBirth(new Date(System.currentTimeMillis()));
        contact1.setGender("m");
        contact1.setCitizenship("USA");
        contact1.setMaritalStatus("divorced");
        contact1.setWebsite("www.inglorious.basterds.com");
        contact1.setEmail("aldo@gmail.com");
        contact1.setJob("veteran");
        contact1.setCountry("USA");
        contact1.setCity("LA");
        contact1.setStreet("AldoStreet");
        contact1.setPostalCode("12345");

        Contact contact2 = new Contact();
        contact2.setName("David");
        contact2.setSurname("Webb");
        contact2.setPatronymic("John");
        contact2.setBirth(new Date(System.currentTimeMillis()));
        contact2.setGender("m");
        contact2.setCitizenship("USA");
        contact2.setMaritalStatus("unmarried");
        contact2.setWebsite("www.usa.gov.com");
        contact2.setEmail("webb@gmail.com");
        contact2.setJob("unemployed");
        contact2.setCountry("Germany");
        contact2.setCity("Berlin");
        contact2.setStreet("");
        contact2.setPostalCode("");

        Contact contact3 = new Contact();
        contact3.setName("Linda");
        contact3.setSurname("Taylor");
        contact3.setPatronymic("");
        contact3.setBirth(new Date(System.currentTimeMillis()));
        contact3.setGender("f");
        contact3.setCitizenship("UK");
        contact3.setMaritalStatus("unmarried");
        contact3.setWebsite("www.linda.com");
        contact3.setEmail("linda@gmail.com");
        contact3.setJob("MI6");
        contact3.setCountry("England");
        contact3.setCity("London");
        contact3.setStreet("");
        contact3.setPostalCode("");


        List<Contact> sampleContacts = new ArrayList<>();
        sampleContacts.add(contact1);
        sampleContacts.add(contact2);
        sampleContacts.add(contact3);
        for(int i = 0; i < sampleContacts.size(); i++){
            sampleContacts.get(i).setProfileImage(sampleImages.get(i));
        }
        return sampleContacts;
    }
}

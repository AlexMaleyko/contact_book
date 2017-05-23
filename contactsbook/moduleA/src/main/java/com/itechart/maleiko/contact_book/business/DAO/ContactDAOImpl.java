package com.itechart.maleiko.contact_book.business.DAO;


import com.itechart.maleiko.contact_book.business.entity.Contact;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.LocalDate;
import com.itechart.maleiko.contact_book.business.service.ContactController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Alexey on 19.03.2017.
 */
public class ContactDAOImpl implements ContactDAO {
    private Properties properties;
    private String propFileName = "fileStorage.properties";

    private Map<String, String> sqlStrings = new HashMap<>();
    {
        sqlStrings.put("fname", " name LIKE ? ");
        sqlStrings.put("lname"," surname LIKE ? ");
        sqlStrings.put("patronymic"," patronymic LIKE ? ");
        sqlStrings.put("gender", " gender LIKE ? ");
        sqlStrings.put("citizenship", " citizenship LIKE ? ");
        sqlStrings.put("status", " marital_status LIKE ? ");
        sqlStrings.put("email", " email LIKE ? ");
        sqlStrings.put("web", " web LIKE ? ");
        sqlStrings.put("job", " job LIKE ? ");
        sqlStrings.put("country", " country LIKE ? ");
        sqlStrings.put("city", " city LIKE ? ");
        sqlStrings.put("street", " street LIKE ? " );
        sqlStrings.put("postalCode", " postal_code LIKE ? ");
    }

    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(ContactDAOImpl.class);

    @Override
    public List<Contact> getAll(Connection conn, int skip, int limit) throws SQLException {

        LOGGER.info("method: getAll({})", conn);

        List<Contact> contactList= new ArrayList<>();
        String query = "SELECT contact_id, name, surname, patronymic, birth, "+
                       "gender, citizenship, marital_status, website, email, "+
                       "job, country, city, street, postal_code, profile_picture "+
                        "FROM contact WHERE deletion_date IS NULL ORDER BY surname, name, patronymic ASC LIMIT ?, ?";

        try(
                PreparedStatement stmt = conn.prepareStatement(query)
        ){
            stmt.setInt(1, skip);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Contact contact  = new Contact();
                contact.setContactId(rs.getLong(1));
                contact.setName(rs.getString(2));
                contact.setSurname(rs.getString(3));
                contact.setPatronymic(rs.getString(4));
                contact.setBirth(rs.getDate(5));
                contact.setGender(rs.getString(6));
                contact.setCitizenship(rs.getString(7));
                contact.setMaritalStatus(rs.getString(8));
                contact.setWebsite(rs.getString(9));
                contact.setEmail(rs.getString(10));
                contact.setJob(rs.getString(11));
                contact.setCountry(rs.getString(12));
                contact.setCity(rs.getString(13));
                contact.setStreet(rs.getString(14));
                contact.setPostalCode(rs.getString(15));
                contact.setProfilePictureName(rs.getString(16));
                contactList.add(contact);
            }
        }

        return contactList;
    }

    @Override
    public Contact findById(Connection conn, long id) throws SQLException {

        LOGGER.info("method: findById({}, {})", conn, id);

        Contact contact=null;
        String query = "SELECT contact_id, name, surname, patronymic, birth, "+
                "gender, citizenship, marital_status, website, email, "+
                "job, country, city, street, postal_code, profile_picture "+
                "FROM contact WHERE contact_id = ?  AND deletion_date IS NULL";

        try(
                PreparedStatement stmt=conn.prepareStatement(query)
        ){
           stmt.setLong(1,id);
           ResultSet rs = stmt.executeQuery();

           if (rs.next()) {
               contact  = new Contact();
               contact.setContactId(rs.getLong(1));
               contact.setName(rs.getString(2));
               contact.setSurname(rs.getString(3));
               contact.setPatronymic(rs.getString(4));
               contact.setBirth(rs.getDate(5));
               contact.setGender(rs.getString(6));
               contact.setCitizenship(rs.getString(7));
               contact.setMaritalStatus(rs.getString(8));
               contact.setWebsite(rs.getString(9));
               contact.setEmail(rs.getString(10));
               contact.setJob(rs.getString(11));
               contact.setCountry(rs.getString(12));
               contact.setCity(rs.getString(13));
               contact.setStreet(rs.getString(14));
               contact.setPostalCode(rs.getString(15));
               contact.setProfilePictureName(rs.getString(16));
           }
        }
        return contact;
    }

    @Override
    public void save(Connection conn, Contact contact) throws Exception {

        LOGGER.info("method: save({}, {})", conn, contact.getClass().getSimpleName());

        String saveString = "INSERT  INTO contact (name, surname, patronymic, birth, gender, citizenship, marital_status, "+
                            "website, email, job, country, city, street, postal_code) "+
                               "VALUES (/*1 name*/ ?, /*2 surname*/ ?, /*3 patronymic*/?, /*4 birth*/?, "+
                "/*5 gender*/?, /*6 citizenship*/?, /*7 marital_status*/?, /*8 website*/?, /*9 email*/?, "+
                "/*10 job*/?, /*11 country*/?, /* 12 city*/?, /*13 street*/?, /*14 postal_code*/?)";
        String saveImagePath = "UPDATE contact SET profile_picture = ?  WHERE contact_id = ?";

        try(
                PreparedStatement stmt = conn.prepareStatement(saveString);
                PreparedStatement savePath = conn.prepareStatement(saveImagePath);
        ){
            stmt.setString(1,contact.getName());
            stmt.setString(2,contact.getSurname());
            stmt.setString(3,contact.getPatronymic());
            stmt.setDate(4,contact.getBirth());
            stmt.setString(5,contact.getGender());
            stmt.setString(6,contact.getCitizenship());
            stmt.setString(7,contact.getMaritalStatus());
            stmt.setString(8,contact.getWebsite());
            stmt.setString(9,contact.getEmail());
            stmt.setString(10,contact.getJob());
            stmt.setString(11,contact.getCountry());
            stmt.setString(12,contact.getCity());
            stmt.setString(13,contact.getStreet());
            stmt.setString(14,contact.getPostalCode());
            stmt.executeUpdate();

            FileItem profileImage = contact.getProfileImage();
            if(profileImage.getSize() > 0) {
                properties = new Properties();
                InputStream inputStream = ContactController.class.getClassLoader().getResourceAsStream(propFileName);
                properties.load(inputStream);

                Path destination = Paths.get(properties.getProperty("profileImageContainer"))
                        .resolve(Long.toString(getMaxContactId(conn)));
                String uploadPath = destination.toAbsolutePath().toString();
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    boolean dirsCreated = uploadDir.mkdirs();
                    if (!dirsCreated) {
                        LOGGER.error("directory creation failed");
                        throw new Exception("Image saving failed");
                    }
                }
                String fileName = getMaxContactId(conn) + "."
                        + FilenameUtils.getExtension((new File(contact.getProfileImage().getName())).getName());
                String filePath = uploadPath + File.separator + fileName;
                File storeFile = new File(filePath);
                profileImage.write(storeFile);

                savePath.setString(1, filePath);
                savePath.setLong(2, getMaxContactId(conn));
                savePath.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Connection conn, Contact contact) throws SQLException {

        LOGGER.info("method: delete({}, {})", conn, contact.getClass().getSimpleName());

        String deleteString="UPDATE contact SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id=?";

        try(
                PreparedStatement stmt=conn.prepareStatement(deleteString)
        ){
            stmt.setLong(1,contact.getContactId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Connection conn, Contact contact) throws Exception {

        LOGGER.info("method: update({}, {})", conn, contact.getClass().getSimpleName());

        String updateString="UPDATE contact "+
                "SET name=?, surname=?, patronymic=?, birth= ? , "+
                "gender=?, citizenship=?, marital_status=?, website=?, email=?, "+
                "job=?, country=?, city=?, street=?, postal_code=?"+
                "WHERE contact_id=?";
        String updateImagePath = "UPDATE contact SET profile_picture = ?  WHERE contact_id = ?";

        try(
                PreparedStatement stmt=conn.prepareStatement(updateString);
                PreparedStatement updatePath = conn.prepareStatement(updateImagePath);
        ){
            properties = new Properties();
            InputStream inputStream = ContactController.class.getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);

            FileItem profileImage = contact.getProfileImage();
            if(profileImage.getSize() > 0 && profileImage != null) {
                Path destination = Paths.get(properties.getProperty("profileImageContainer"))
                        .resolve(Long.toString(contact.getContactId()));
                String uploadPath = destination.toAbsolutePath().toString();
                File uploadDir = new File(uploadPath);
                FileUtils.deleteDirectory(uploadDir);
                if (!uploadDir.exists()) {
                    boolean dirsCreated = uploadDir.mkdirs();
                    if (!dirsCreated) {
                        throw new Exception("Image saving failed");
                    }
                }
                String fileName = contact.getContactId() + "."
                        + FilenameUtils.getExtension((new File(contact.getProfileImage().getName())).getName());
                String filePath = uploadPath + File.separator + fileName;
                File storeFile = new File(filePath);
                profileImage.write(storeFile);
                contact.setProfilePictureName(filePath);
                updatePath.setString(1, contact.getProfilePictureName());
                updatePath.setLong(2, contact.getContactId());
                updatePath.execute();
            }

            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getSurname());
            stmt.setString(3, contact.getPatronymic());
            stmt.setDate(4, contact.getBirth());
            stmt.setString(5, contact.getGender());
            stmt.setString(6, contact.getCitizenship());
            stmt.setString(7, contact.getMaritalStatus());
            stmt.setString(8, contact.getWebsite());
            stmt.setString(9, contact.getEmail());
            stmt.setString(10, contact.getJob());
            stmt.setString(11, contact.getCountry());
            stmt.setString(12, contact.getCity());
            stmt.setString(13, contact.getStreet());
            stmt.setString(14, contact.getPostalCode());
            stmt.setLong(15, contact.getContactId());
            stmt.executeUpdate();


        }
    }

    @Override
    public void deleteByContactId(Connection conn, long id) throws SQLException, IOException {

        LOGGER.info("method: deleteByContactId({}, {})", conn, id);

        String deleteString="UPDATE contact SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id=?";
        String getImagePath = "SELECT profile_picture FROM contact WHERE  contact_id = ?";

        try(
                PreparedStatement stmt=conn.prepareStatement(deleteString);
                PreparedStatement getPath = conn.prepareStatement(getImagePath);
        ){
            getPath.setLong(1, id);
            ResultSet rs = getPath.executeQuery();
            rs.next();
            String path = rs.getString(1);
            if (path != null) {
                FileUtils.deleteDirectory(Paths.get(path).getParent().toFile());
            }
            stmt.setLong(1, id);
            stmt.executeUpdate();

        }
    }
    @Override
    public PairResultSize findByGivenParameters(Connection conn, Map<String, Object> fieldValue) throws SQLException {
        LOGGER.info("method: findByGivenParameters({}, {})", conn, fieldValue.getClass().getSimpleName());
        List<Contact> contactList = new ArrayList<>();
        PairResultSize pair = new PairResultSize();
        try {

            Map<Integer, Object> preparedParameters = new HashMap<>();
            String searchQuery = "SELECT contact_id, name, surname, patronymic, birth, " +
                    "gender, citizenship, marital_status, website, email, " +
                    "job, country, city, street, postal_code, profile_picture " +
                    "FROM contact WHERE deletion_date IS NULL ";

            String resultSetSize = "SELECT COUNT(*) FROM contact WHERE deletion_date IS NULL ";

            String searchParameters = " ";
            int num = 1;
            Set<String> keySet = fieldValue.keySet();

            for (String parameter : keySet) {
                if(parameter.equals("comparator")){
                    continue;
                }
                Object o = fieldValue.get(parameter);
                searchParameters += " AND ";
                if (o instanceof LocalDate) {
                    org.joda.time.LocalDate ld = (LocalDate) o;
                    java.sql.Date date = new Date(ld.toDateTimeAtStartOfDay().getMillis());
                    preparedParameters.put(num, date);
                    searchParameters += " birth ";
                    String comparator = (String) fieldValue.get("comparator");
                    if (comparator.toLowerCase().equals("equals")) {
                        searchParameters += " = ";
                    } else if (comparator.toLowerCase().equals("less")) {
                        searchParameters += " < ";
                    } else {
                        searchParameters += " > ";
                    }
                    searchParameters += " ? ";
                    keySet.remove("comparator");

                } else {
                    preparedParameters.put(num, (String) o);
                    searchParameters += sqlStrings.get(parameter);
                }
                num++;
            }
            searchQuery += searchParameters;
            searchQuery += " ORDER BY surname, name, patronymic ASC ";
            resultSetSize += searchParameters;

            try (
                    PreparedStatement stmt = conn.prepareStatement(searchQuery);
                    PreparedStatement rsSize = conn.prepareStatement(resultSetSize);
            ) {
                for (int i : preparedParameters.keySet()) {
                    if (preparedParameters.get(i) instanceof java.sql.Date) {
                        stmt.setDate(i, (java.sql.Date) preparedParameters.get(i));
                        rsSize.setDate(i, (java.sql.Date) preparedParameters.get(i));
                    } else {
                        stmt.setString(i, "%" + (String) preparedParameters.get(i) + "%");
                        rsSize.setString(i, "%" + (String) preparedParameters.get(i) + "%");
                    }
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Contact contact  = new Contact();
                    contact.setContactId(rs.getLong(1));
                    contact.setName(rs.getString(2));
                    contact.setSurname(rs.getString(3));
                    contact.setPatronymic(rs.getString(4));
                    contact.setBirth(rs.getDate(5));
                    contact.setGender(rs.getString(6));
                    contact.setCitizenship(rs.getString(7));
                    contact.setMaritalStatus(rs.getString(8));
                    contact.setWebsite(rs.getString(9));
                    contact.setEmail(rs.getString(10));
                    contact.setJob(rs.getString(11));
                    contact.setCountry(rs.getString(12));
                    contact.setCity(rs.getString(13));
                    contact.setStreet(rs.getString(14));
                    contact.setPostalCode(rs.getString(15));
                    contact.setProfilePictureName(rs.getString(16));
                    contactList.add(contact);
                }
                ResultSet size = rsSize.executeQuery();
                size.next();
                long rowNumber = size.getLong(1);
                pair.setResultSetSize(rowNumber);
            }
        }catch (NullPointerException e){
            LOGGER.error("{}",e);
        }
        pair.setContactList(contactList);
        return pair;
    }
    public long getMaxContactId(Connection conn) throws SQLException {
        LOGGER.info("method: getMaxContactId({})", conn);
        long maxContactId = 0;
        String query = "SELECT MAX(contact_id) FROM contact";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            maxContactId = rs.getLong(1);
        }
        return maxContactId;
    }
    public long getLastContactId(Connection conn) throws SQLException {
        LOGGER.info("method: getNewPhoneId({})", conn);
        long lastContactId = 0;
        String query = "SELECT (auto_increment - 1) " +
                "FROM information_schema.tables " +
                "WHERE table_name = 'contact' " +
                "AND table_schema = 'contact_book'";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            lastContactId = rs.getLong(1);
        }
        return lastContactId;
    }

    @Override
    public int getNumberOfContacts(Connection conn) throws SQLException {
        LOGGER.info("method: getNumberOfContacts({})", conn);
        String query = "SELECT COUNT(*) FROM contact WHERE deletion_date is null";
        int numberOfContacts;
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            numberOfContacts = rs.getInt(1);
        }
        return numberOfContacts;
    }
}

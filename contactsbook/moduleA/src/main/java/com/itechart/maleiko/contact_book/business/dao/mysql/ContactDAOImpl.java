package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.ContactDAO;
import com.itechart.maleiko.contact_book.business.dao.PairResultSize;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.Image;
import com.itechart.maleiko.contact_book.business.utils.PropertiesLoader;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ContactDAOImpl implements ContactDAO {
    private final String FILE_STORAGE_PROPERTIES_FILE_NAME;
    private final String FIND_ALL_CONTACTS_QUERY;
    private final String FIND_ALL_CONTACTS_SORT_AND_LIMIT_QUERY;
    private final String FIND_CONTACT_BY_ID_QUERY;
    private final String FIND_IMAGE_PATH_BY_CONTACT_ID_QUERY;
    private final String SET_IMAGE_PATH_TO_NULL_QUERY;
    private final String SAVE_CONTACT_QUERY;
    private final String DELETE_CONTACT_BY_ID_QUERY;
    private final String UPDATE_CONTACT_BY_ID_QUERY;
    private final String UPDATE_IMAGE_PATH_QUERY;
    private final String COUNT_NOT_DELETED_CONTACTS_QUERY;
    private Connection conn;
    private AttachmentDAOImpl attachmentDAO;
    private PhoneNumberDAOImpl phoneNumberDAO;

    ContactDAOImpl(){
        FILE_STORAGE_PROPERTIES_FILE_NAME = "fileStorage.properties";

        FIND_ALL_CONTACTS_QUERY = "SELECT contact_id, name, surname, patronymic, birth, " +
                "gender, citizenship, marital_status, website, email, " +
                "job, country, city, street, postal_code, profile_picture " +
                "FROM contact WHERE deletion_date IS NULL ";

        FIND_ALL_CONTACTS_SORT_AND_LIMIT_QUERY = "SELECT contact_id, name, surname, patronymic, birth, " +
                "gender, citizenship, marital_status, website, email, " +
                "job, country, city, street, postal_code, profile_picture " +
                "FROM contact WHERE deletion_date IS NULL ORDER BY surname, name, patronymic ASC LIMIT ?, ?";

        FIND_CONTACT_BY_ID_QUERY = "SELECT contact_id, name, surname, patronymic, birth, " +
                "gender, citizenship, marital_status, website, email, " +
                "job, country, city, street, postal_code, profile_picture " +
                "FROM contact WHERE contact_id = ?  AND deletion_date IS NULL";

        FIND_IMAGE_PATH_BY_CONTACT_ID_QUERY = "SELECT profile_picture FROM contact WHERE  contact_id in ";

        SET_IMAGE_PATH_TO_NULL_QUERY = "UPDATE contact SET profile_picture = NULL WHERE contact_id = ?";

        SAVE_CONTACT_QUERY = "INSERT  INTO contact (name, surname, patronymic, birth, gender, citizenship, " +
                "marital_status, website, email, job, country, city, street, postal_code) " +
                "VALUES (/*1 name*/ ?, /*2 surname*/ ?, /*3 patronymic*/ ?, /*4 birth*/ ?, " +
                "/*5 gender*/ ?, /*6 citizenship*/ ?, /*7 marital_status*/ ?, /*8 website*/ ?, /*9 email*/ ?, " +
                "/*10 job*/ ?, /*11 country*/ ?, /* 12 city*/ ?, /*13 street*/ ?, /*14 postal_code*/ ?)";

        DELETE_CONTACT_BY_ID_QUERY = "UPDATE contact SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id=?";

        UPDATE_CONTACT_BY_ID_QUERY = "UPDATE contact " +
                "SET name=?, surname=?, patronymic=?, birth= ? , gender=?, citizenship=?, marital_status=?," +
                " website=?, email=?, job=?, country=?, city=?, street=?, postal_code=?" +
                "WHERE contact_id=?";

        UPDATE_IMAGE_PATH_QUERY = "UPDATE contact SET profile_picture = ?  WHERE contact_id = ?";

        COUNT_NOT_DELETED_CONTACTS_QUERY = "SELECT COUNT(*) FROM contact WHERE deletion_date IS NULL ";
    }

    ContactDAOImpl(AttachmentDAOImpl attachmentDAO, PhoneNumberDAOImpl phoneNumberDAO) {
        this();
        this.attachmentDAO = attachmentDAO;
        this.phoneNumberDAO = phoneNumberDAO;
    }

    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    private Map<String, String> sqlStrings = new HashMap<>();

    {
        sqlStrings.put("fname", " name LIKE ? ");
        sqlStrings.put("lname", " surname LIKE ? ");
        sqlStrings.put("patronymic", " patronymic LIKE ? ");
        sqlStrings.put("gender", " gender LIKE ? ");
        sqlStrings.put("citizenship", " citizenship LIKE ? ");
        sqlStrings.put("status", " marital_status LIKE ? ");
        sqlStrings.put("email", " email LIKE ? ");
        sqlStrings.put("web", " website LIKE ? ");
        sqlStrings.put("job", " job LIKE ? ");
        sqlStrings.put("country", " country LIKE ? ");
        sqlStrings.put("city", " city LIKE ? ");
        sqlStrings.put("street", " street LIKE ? ");
        sqlStrings.put("postalCode", " postal_code LIKE ? ");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactDAOImpl.class);

    @Override
    public List<Contact> getAll(int skip, int limit) throws DAOException {
        List<Contact> contacts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(FIND_ALL_CONTACTS_SORT_AND_LIMIT_QUERY)) {
            stmt.setInt(1, skip);
            stmt.setInt(2, limit);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Contact contact = generateEntityObjectFromResultSetRow(rs);
                    contacts.add(contact);
                }
            }
        } catch (SQLException e) {
            String message = "Error retrieving contacts. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return contacts;
    }

    @Override
    public Contact findById(long id) throws DAOException {
        Contact contact = null;
        try (PreparedStatement stmt = conn.prepareStatement(FIND_CONTACT_BY_ID_QUERY)) {
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contact = generateEntityObjectFromResultSetRow(rs);
                }
            }
        } catch (SQLException e) {
            String message = "Error finding contact by id. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return contact;
    }

    @Override
    public void save(Contact contact) throws DAOException {
        attachmentDAO.setConnection(conn);
        phoneNumberDAO.setConnection(conn);
        Contact savedContact = saveContact(contact);
        if (contact.getProfileImage() != null) {
            saveProfileImage(savedContact);
        }
        //setContactId refreshes attachments' and numbers' contactId fields
        if (savedContact.getAttachments() != null) {
            attachmentDAO.save(savedContact.getAttachments());
        }
        if (savedContact.getPhoneNumbers() != null) {
            phoneNumberDAO.save(savedContact.getPhoneNumbers());
        }
    }

    private Contact saveContact(Contact contact) throws DAOException {
        try (PreparedStatement stmt = conn.prepareStatement(SAVE_CONTACT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
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
            stmt.executeUpdate();
            contact.setContactId(getInsertedRowId(stmt));
        } catch (SQLException e) {
            String message = "Error saving contacts. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return contact;
    }

    private void saveProfileImage(Contact contact) throws DAOException {
        String fullPath = saveProfileImageToFileSystem(contact);
        addImageInfoToContactRecord(fullPath, contact);
    }

    private String saveProfileImageToFileSystem(Contact contact) throws DAOException {
        File fullPath;
        try {
            FileItem profileImage = contact.getProfileImage();
            String destinationPath = generateDestinationPathForStoringProfileImage(contact);
            createDirectoriesForStoringImage(destinationPath);
            String prefix = "img";
            String suffix = "." + FilenameUtils.getExtension(profileImage.getName());
            fullPath = File.createTempFile(prefix, suffix, new File(destinationPath));
            profileImage.write(fullPath);
        } catch (Exception e) {
            throw new DAOException("Failed saving image to file system", e);
        }
        return fullPath.toString();
    }

    private String generateDestinationPathForStoringProfileImage(Contact contact) throws DAOException {
        Properties properties = PropertiesLoader.load(FILE_STORAGE_PROPERTIES_FILE_NAME);
        Path destination = Paths.get(properties.getProperty("profileImageContainer"))
                .resolve(Long.toString(contact.getContactId()));
        return destination + File.separator;
    }

    private void createDirectoriesForStoringImage(String destinationPath) throws DAOException {
        File uploadDir = new File(destinationPath);
        if (!uploadDir.exists()) {
            boolean dirsCreated = uploadDir.mkdirs();
            if (!dirsCreated) {
                throw new DAOException("Failed creating directories for profile image");
            }
        }
    }

    private void addImageInfoToContactRecord(String fullPath, Contact contact) throws DAOException {
        try (PreparedStatement savePath = conn.prepareStatement(UPDATE_IMAGE_PATH_QUERY)) {
            savePath.setString(1, fullPath);
            savePath.setLong(2, contact.getContactId());
            savePath.executeUpdate();
        } catch (SQLException e) {
            String message = "Error updating contact image info. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public void update(Contact contact) throws DAOException {
        updateContactInfo(contact);
        if (contact.getProfileImage() != null) {
            List<Long> ids = new ArrayList<>();
            ids.add(contact.getContactId());
            deleteContainingDirectories(findImagesPathsByContactIds(ids));
            saveProfileImage(contact);
        }
    }

    private void updateContactInfo(Contact contact) throws DAOException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_CONTACT_BY_ID_QUERY)) {
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
        } catch (SQLException e) {
            String message = "Error updating contact. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public void deleteByContactIds(List<Long> ids) throws DAOException {
        LOGGER.info("deleting contacts, {}", ids);
        List<String> fullPaths = findImagesPathsByContactIds(ids);
        deleteContainingDirectories(fullPaths);
        attachmentDAO.setConnection(conn);
        for (Long id : ids) {
            attachmentDAO.deleteFilesFromFileSystemByContactId(id);
        }
        deleteContactsByIds(ids);
    }

    private List<String> findImagesPathsByContactIds(List<Long> ids) throws DAOException {
        List<String> fullPaths = new ArrayList<>();
        String processedQuery = fillSqlInOperatorWithPlaceholders(ids.size(), FIND_IMAGE_PATH_BY_CONTACT_ID_QUERY);
        try (PreparedStatement getPaths = conn.prepareStatement(processedQuery)) {
            supplyIdsForPreparedStatement(getPaths, ids);
            try(ResultSet rs = getPaths.executeQuery()) {
                while (rs.next()) {
                    fullPaths.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            String message = "Error finding images' paths. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return fullPaths;
    }

    private String fillSqlInOperatorWithPlaceholders(int numberOfPlaceholders, String sqlQuery) {
        StringBuilder builder = new StringBuilder(sqlQuery);
        builder.append(" ( ");
        for (int i = 0; i < numberOfPlaceholders; i++) {
            if (i != numberOfPlaceholders - 1) {
                builder.append(" ?,");
            } else {
                builder.append(" ?)");
            }
        }
        return builder.toString();
    }

    private void supplyIdsForPreparedStatement(PreparedStatement stmt, List<Long> ids) throws SQLException {
        for (int i = 0; i < ids.size(); i++) {
            stmt.setLong(i + 1, ids.get(i));
        }
    }

    private void deleteContainingDirectories(List<String> fullPaths) throws DAOException {
        try {
            for (String fullPath : fullPaths) {
                if (fullPath != null) {
                    FileUtils.forceDelete(Paths.get(fullPath).getParent().toFile());
                }
            }
        } catch (IOException e) {
            throw new DAOException("Failed deleting directories.", e);
        }
    }

    private void deleteContactsByIds(List<Long> ids) throws DAOException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_CONTACT_BY_ID_QUERY)) {
            for (Long id : ids) {
                stmt.setLong(1, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            String message = "Error deleting contacts by ids. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public PairResultSize findByGivenParameters(Map<String, Object> fieldValue) throws DAOException {
        List<Contact> contactList = new ArrayList<>();
        PairResultSize pair = new PairResultSize();
        try {

            Map<Integer, Object> preparedParameters = new HashMap<>();
            String searchQuery = FIND_ALL_CONTACTS_QUERY;
            String resultSetSize = COUNT_NOT_DELETED_CONTACTS_QUERY;

            String searchParameters = " ";
            int num = 1;
            Set<String> keySet = fieldValue.keySet();

            for (String parameter : keySet) {
                if (parameter.equals("comparator")) {
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
                    // keySet.remove("comparator");

                } else if (!parameter.equals("comparator")) {
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
                try(ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Contact contact = generateEntityObjectFromResultSetRow(rs);
                        contactList.add(contact);
                    }
                }
                try(ResultSet size = rsSize.executeQuery()) {
                    size.next();
                    long rowNumber = size.getLong(1);
                    pair.setResultSetSize(rowNumber);
                }
            }
        } catch (SQLException e) {
            String message = "Error searching contacts. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        pair.setContactList(contactList);
        return pair;
    }


    private long getInsertedRowId(Statement statement) throws DAOException {
        long generatedId = 0;
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            String message = "Error retrieving id of generated record. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return generatedId;
    }

    @Override
    public int getNumberOfContacts() throws DAOException {
        int numberOfContacts;
        try (PreparedStatement stmt = conn.prepareStatement(COUNT_NOT_DELETED_CONTACTS_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            numberOfContacts = rs.getInt(1);
        } catch (SQLException e) {
            String message = "Error retrieving quantity of contacts. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return numberOfContacts;
    }

    Contact generateEntityObjectFromResultSetRow(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
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
        contact.setProfilePicturePath(rs.getString(16));
        return contact;
    }

    public Image getProfileImageByContactId(long id) throws DAOException {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        Path image;
        List<String> imagePaths = findImagesPathsByContactIds(ids);
        if (!imagePaths.isEmpty()) {
            String imagePath = imagePaths.get(0);
            if (imagePath == null) {
                Properties properties = PropertiesLoader.load(FILE_STORAGE_PROPERTIES_FILE_NAME);
                image = Paths.get(properties.getProperty("defaultPicturePath")).toAbsolutePath();
                if (Files.notExists(image)) {
                    String message = "Default picture on path: " + image.toString() + " doesn't exist";
                    throw new DAOException(message);
                }
            } else {
                image = Paths.get(imagePath);
                if (Files.notExists(image)) {
                    LOGGER.error("Profile image of contact(id = {}) doesn't exist. Contact info is being updated", id);
                    setContactProfileImagePathToNull(id);

                    Properties properties = PropertiesLoader.load(FILE_STORAGE_PROPERTIES_FILE_NAME);
                    image = Paths.get(properties.getProperty("defaultPicturePath")).toAbsolutePath();
                    if (Files.notExists(image)) {
                        String message = "Default picture on path: " + image.toString() + " doesn't exist";
                        throw new DAOException(message);
                    }
                }
            }
            return createImage(image);
        } else if (id == 0) {
            Properties properties = PropertiesLoader.load(FILE_STORAGE_PROPERTIES_FILE_NAME);
            image = Paths.get(properties.getProperty("defaultPicturePath")).toAbsolutePath();
            if (Files.notExists(image)) {
                String message = "Default picture on path: " + image.toString() + " doesn't exist";
                throw new DAOException(message);
            }
            return createImage(image);
        }
        return null;
    }

    private Image createImage(Path imagePath) throws DAOException {
        try {
            byte[] byteRepresentation = Files.readAllBytes(imagePath);
            String imageName = imagePath.getFileName().toString();
            String fileSize = String.valueOf(Files.size(imagePath));
            return new Image(imageName, fileSize, byteRepresentation);
        } catch (IOException e) {
            throw new DAOException("Error converting image to byte array", e);
        }
    }

    private void setContactProfileImagePathToNull(long contactId) throws DAOException {
        try (PreparedStatement stmt = conn.prepareStatement(SET_IMAGE_PATH_TO_NULL_QUERY)) {
            stmt.setLong(1, contactId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String message = "Error retrieving quantity of contacts. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }
}
package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.AttachmentDAO;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AttachmentDAOImpl implements AttachmentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentDAOImpl.class);
    private final String FILE_STORAGE_PROPERTIES_FILE_NAME;
    private final String SAVE_ATTACHMENT_QUERY;
    private final String FIND_ATTACHMENTS_BY_CONTACT_ID_QUERY;
    private final String FIND_ATTACHMENT_FILE_PATH_QUERY;
    private final String FIND_ATTACHMENTS_FILE_PATH_BY_CONTACT_ID_QUERY;
    private final String DELETE_ATTACHMENTS_BY_CONTACT_ID_QUERY;
    private final String DELETE_ATTACHMENT_BY_ID_QUERY;
    private final String UPDATE_ATTACHMENT_QUERY;
    private Connection conn;

    {
        FILE_STORAGE_PROPERTIES_FILE_NAME = "fileStorage.properties";
        SAVE_ATTACHMENT_QUERY = "INSERT INTO attachment (file_path, file_name, comment, contact_id) VALUES (?, ?, ?, ?)";
        FIND_ATTACHMENTS_BY_CONTACT_ID_QUERY = "SELECT attachment_id, file_path, file_name, upload_date, comment " +
                "FROM attachment WHERE contact_id= ? AND deletion_date IS NULL";
        FIND_ATTACHMENT_FILE_PATH_QUERY = "SELECT file_path FROM attachment WHERE attachment_id in ";
        FIND_ATTACHMENTS_FILE_PATH_BY_CONTACT_ID_QUERY = "SELECT file_path FROM attachment WHERE contact_id=?";
        DELETE_ATTACHMENTS_BY_CONTACT_ID_QUERY = "UPDATE attachment SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id=?";
        DELETE_ATTACHMENT_BY_ID_QUERY = "UPDATE attachment SET deletion_date=CURRENT_TIMESTAMP WHERE attachment_id=?";
        UPDATE_ATTACHMENT_QUERY = "UPDATE attachment SET file_name=?, comment=? WHERE attachment_id=?";
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(List<Attachment> attachments) throws DAOException{
        LOGGER.info("method: save({})", attachments.getClass().getSimpleName());

        try (PreparedStatement stmt = conn.prepareStatement(SAVE_ATTACHMENT_QUERY)) {
            for (Attachment attachment : attachments) {
                String fullPath = saveUploadedFileOnDisk(attachment);

                stmt.setString(1, fullPath);
                stmt.setString(2, attachment.getFileName());
                stmt.setString(3, attachment.getComment());
                stmt.setLong(4, attachment.getContactId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }catch (SQLException e){
            String message = "Error saving attachment info. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    private String saveUploadedFileOnDisk(Attachment attachment) throws DAOException{
        File fullPath;
        try {
            String containingDirectoryName = Long.toString(attachment.getContactId());
            String destinationPath = generateDestinationPathForStoringUploadedFile(containingDirectoryName);
            createDirectoriesForStoringUploadedFile(destinationPath);
            String suffix = "." + FilenameUtils.getExtension(attachment.getFile().getName());
            fullPath = File.createTempFile("file", suffix, new File(destinationPath));
            attachment.getFile().write(fullPath);
        } catch(DAOException e){
            throw e;
        } catch (Exception e){
            throw new DAOException("Failed creating file", e);
        }
        return fullPath.toString();
    }

    private String generateDestinationPathForStoringUploadedFile(String containingDirectoryName) throws DAOException {
        Path destination;
        try {
            Properties properties = new Properties();
            InputStream inputStream = AttachmentDAO.class.getClassLoader()
                    .getResourceAsStream(FILE_STORAGE_PROPERTIES_FILE_NAME);
            properties.load(inputStream);
            destination = Paths.get(properties.getProperty("contactFilesContainer")).resolve(containingDirectoryName);
        }catch (IOException e){
            throw new DAOException("Error accessing file storage properties", e);
        }
        return destination.toString() + File.separator;
    }

    private void createDirectoriesForStoringUploadedFile(String destinationPath) throws DAOException {
        File uploadDir = new File(destinationPath);
        if (!uploadDir.exists()) {
            boolean dirsCreated = uploadDir.mkdirs();
            if (!dirsCreated) {
                throw new DAOException("Failed creating directories for uploaded file");
            }
        }
    }

    @Override
    public void update(List<Attachment> attachments) throws DAOException {
        LOGGER.info("method: update({})", attachments.getClass().getSimpleName());
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_ATTACHMENT_QUERY)) {
            for (Attachment attachment : attachments) {
                stmt.setString(1, attachment.getFileName());
                stmt.setString(2, attachment.getComment());
                stmt.setLong(3, attachment.getAttachmentId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e){
            String message = "Error updating attachment info. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }


    @Override
    public List<Attachment> findByContactId(long contactId) throws DAOException{
        LOGGER.info("method: findByContactId({})", contactId);

        List<Attachment> attachments = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(FIND_ATTACHMENTS_BY_CONTACT_ID_QUERY)) {
            stmt.setLong(1, contactId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(rs.getInt(1));
                attachment.setFilePath(rs.getString(2));
                attachment.setFileName(rs.getString(3));
                attachment.setUploadDate(rs.getTimestamp(4));
                attachment.setComment(rs.getString(5));
                attachment.setContactId(contactId);

                attachments.add(attachment);
            }
        }catch (SQLException e){
            String message = "Error finding attachment info. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        deleteAttachmentsNotPresentInFileSystem(attachments);
        return attachments;
    }

    private void deleteAttachmentsNotPresentInFileSystem(List<Attachment> attachments) throws DAOException {
            List<Long> attachmentIdsForDeletion = new ArrayList<>();
            for (Attachment attachment : attachments) {
                Path fullPath = Paths.get(attachment.getFilePath());
                if (StringUtils.isBlank(attachment.getFilePath()) || Files.notExists(fullPath)) {
                    attachmentIdsForDeletion.add(attachment.getAttachmentId());
                    attachments.remove(attachment);
                }
            }
            deleteMetaInfoByAttachmentIds(attachmentIdsForDeletion);
    }

    @Override
    public void deleteByContactId(long id) throws DAOException{
        LOGGER.info("method: deleteByContactId({})", id);
        deleteFilesFromFileSystemByContactId(id);
        deleteMetaInfoByContactId(id);
    }

    void deleteFilesFromFileSystemByContactId(long contact_id) throws DAOException {
        try (PreparedStatement findFullPaths = conn.prepareStatement(FIND_ATTACHMENTS_FILE_PATH_BY_CONTACT_ID_QUERY)) {
            findFullPaths.setLong(1, contact_id);
            ResultSet rs = findFullPaths.executeQuery();
            performFilesDeletion(rs);
        }catch (SQLException e){
            String message = "Error finding attachment file path. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    private void deleteMetaInfoByContactId(long id) throws DAOException{
        try (PreparedStatement deleteMetaInfo = conn.prepareStatement(DELETE_ATTACHMENTS_BY_CONTACT_ID_QUERY)) {
            deleteMetaInfo.setLong(1, id);
            deleteMetaInfo.executeUpdate();
        }catch (SQLException e){
            String message = "Error deleting attachment info. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) throws DAOException {
        LOGGER.info("method: delete({}, {})", conn, ids);
        deleteFilesFromFileSystemByAttachmentIds(ids);
        deleteMetaInfoByAttachmentIds(ids);
    }

    private void deleteFilesFromFileSystemByAttachmentIds(List<Long> ids) throws DAOException {
        String findAttachmentFilePathQueryWithPlaceholders = fillFindAttachmentFilePathQueryWithPlaceholders(ids.size());
        try (PreparedStatement findFullPath = conn.prepareStatement(findAttachmentFilePathQueryWithPlaceholders)){
            supplyValuesForFindFullPathStatement(findFullPath, ids);
            ResultSet rs = findFullPath.executeQuery();
            performFilesDeletion(rs);
        }catch (SQLException e){
            String message = "Error finding attachment full path. " +
                    "SQLState: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    private String fillFindAttachmentFilePathQueryWithPlaceholders(int numberOfIds) {
        StringBuilder builder = new StringBuilder(FIND_ATTACHMENT_FILE_PATH_QUERY);
        builder.append(" ( ");
        for (int i = 0; i < numberOfIds; i++) {
            if (i != numberOfIds - 1) {
                builder.append(" ?,");
            } else {
                builder.append(" ?)");
            }
        }
        return builder.toString();
    }

    private void supplyValuesForFindFullPathStatement(PreparedStatement stmt, List<Long> ids) throws SQLException {
        for (int i = 0; i < ids.size(); i++) {
            stmt.setLong(i + 1, ids.get(i));
        }
    }

    private void deleteMetaInfoByAttachmentIds(List<Long> ids) throws DAOException {
        try (PreparedStatement deleteAttachments = conn.prepareStatement(DELETE_ATTACHMENT_BY_ID_QUERY)) {
            for(Long id : ids) {
                deleteAttachments.setLong(1, id);
                deleteAttachments.addBatch();
            }
            deleteAttachments.executeBatch();
        }catch (SQLException e){
            String message = "Error deleting attachment info. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    private void performFilesDeletion(ResultSet rs) throws SQLException, DAOException {
        try {
            while (rs.next()) {
                String fullPath = rs.getString(1);
                File fileForDeletion = new File(fullPath);
                if (fileForDeletion.exists()) {
                    FileUtils.forceDelete(fileForDeletion);
                }
            }
        }catch (IOException e){
            throw new DAOException("Failed deleting files from file system", e);
        }
    }

    public Attachment getFile(long attachmentId) throws DAOException{
        List<Long> ids = new ArrayList<>();
        ids.add(attachmentId);
        String attachmentPath;
        Path attachment;
        String findAttachmentFilePathQueryWithPlaceholders = fillFindAttachmentFilePathQueryWithPlaceholders(ids.size());
        try (PreparedStatement findFullPath = conn.prepareStatement(findAttachmentFilePathQueryWithPlaceholders)) {
            supplyValuesForFindFullPathStatement(findFullPath, ids);
            ResultSet rs = findFullPath.executeQuery();
            if (rs.next()) {
                attachmentPath = rs.getString("file_path");
                attachment = Paths.get(attachmentPath);
                if (Files.exists(attachment)) {
                    byte[] bytes = Files.readAllBytes(attachment);
                    String fileName = attachment.getFileName().toString();
                    String fileSize = String.valueOf(Files.size(attachment));
                    return new Attachment(fileName, fileSize, bytes);
                } else {
                    throw new DAOException("Error loading file");
                }
            }
        } catch (SQLException e) {
            String message = "Error deleting attachment info. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        } catch (IOException e) {
            throw new DAOException("Error loading file: " + e.getMessage(), e);
        }
        return null;
    }

}
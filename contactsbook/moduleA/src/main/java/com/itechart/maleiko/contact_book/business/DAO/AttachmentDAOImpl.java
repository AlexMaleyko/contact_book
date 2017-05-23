package com.itechart.maleiko.contact_book.business.DAO;

import com.itechart.maleiko.contact_book.business.entity.Attachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import com.itechart.maleiko.contact_book.business.service.ContactController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alexey on 19.03.2017.
 */
public class AttachmentDAOImpl implements AttachmentDAO {
    private Properties properties;
    private String propFileName = "fileStorage.properties";
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(AttachmentDAOImpl.class);

    @Override
    public void save(Connection conn, Attachment attachment) throws Exception {

        LOGGER.info("method: save({}, {})", conn, attachment.getClass().getSimpleName());

        String saveStatement ="INSERT INTO attachment (file_path, file_name, comment, contact_id)"+
                              "VALUES (?, ?, ?, ?)";

        try(
                PreparedStatement stmt = conn.prepareStatement(saveStatement)
        ){
            properties = new Properties();
            InputStream inputStream = ContactController.class.getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);

            FileItem file = attachment.getFile();
            Path destination = Paths.get(properties.getProperty("contactFilesContainer"))
                    .resolve(Long.toString(attachment.getContactId()));
            String uploadPath = destination.toAbsolutePath().toString();
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                boolean dirsCreated = uploadDir.mkdirs();
                if(!dirsCreated){
                    LOGGER.error("Directory creation failed");
                    throw new RuntimeException("File saving failed");
                }
            }
            String fileName = getNewAttachmentId(conn) + "." +
                    FilenameUtils.getExtension((new File(attachment.getFile().getName())).getName());
            String filePath = uploadPath + File.separator + fileName;
            File storeFile = new File(filePath);
            file.write(storeFile);
            stmt.setString(1, filePath);
            stmt.setString(2, attachment.getFileName());
            stmt.setString(3, attachment.getComment());
            stmt.setLong(4, attachment.getContactId());
            stmt.executeUpdate();

        }
    }

    @Override
    public List<Attachment> findByContactId(Connection conn, long contactId) throws SQLException {

        LOGGER.info("method: findByContactId({}, {})", conn, contactId);

        List<Attachment> attachmentList=new ArrayList<>();
        String query = "SELECT attachment_id, file_path, file_name, upload_date, comment "+
                       "FROM attachment WHERE contact_id= ? AND deletion_date IS NULL";

        try(
                PreparedStatement stmt=conn.prepareStatement(query)
        ){
            stmt.setLong(1,contactId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(rs.getInt(1));
                attachment.setFilePath(rs.getString(2));
                attachment.setFileName(rs.getString(3));
                attachment.setUploadDate(rs.getTimestamp(4));
                attachment.setComment(rs.getString(5));
                attachment.setContactId(contactId);
                attachmentList.add(attachment);
            }
        }

        return attachmentList;
    }

    @Override
    public void deleteByContactId(Connection conn, long id) throws SQLException, IOException {

        LOGGER.info("method: deleteByContactId({}, {})", conn, id);

        String deleteString= "UPDATE attachment SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id=?";
        String getPath = "SELECT file_path FROM attachment WHERE contact_id=?";

        try(
                PreparedStatement delete = conn.prepareStatement(deleteString);
                PreparedStatement path = conn.prepareStatement(getPath)
        ){
            path.setLong(1, id);
            ResultSet rs = path.executeQuery();
            while(rs.next()) {
                String filePath = rs.getString(1);
                File deleteFile = new File(filePath);

                if (deleteFile.exists()) {
                    FileUtils.forceDelete(deleteFile);
                }
            }
            delete.setLong(1, id);
            delete.executeUpdate();

        }
    }

    public void deleteById(Connection conn, long id) throws SQLException, IOException {
        LOGGER.info("method: deleteById({}, {})", conn, id);

        String deleteString= "UPDATE attachment SET deletion_date=CURRENT_TIMESTAMP WHERE attachment_id=?";
        String getPath = "SELECT file_path FROM attachment WHERE attachment_id=?";

        try(
                PreparedStatement delete = conn.prepareStatement(deleteString);
                PreparedStatement path = conn.prepareStatement(getPath)
        ){
            path.setLong(1, id);
            ResultSet rs = path.executeQuery();
            rs.next();
            String filePath = rs.getString(1);
            File deleteFile = new File(filePath);
            if (deleteFile.exists()) {
                FileUtils.forceDelete(deleteFile);
            }

            delete.setLong(1, id);
            delete.executeUpdate();

        }
    }

    public void update(Connection conn, Attachment attachment) throws SQLException{
        LOGGER.info("method: update({}, {})", conn, attachment.getClass().getSimpleName());

        String updateString="UPDATE attachment "+
                "SET file_name=?, comment=? "+
                "WHERE attachment_id=?";

        try(
                PreparedStatement stmt=conn.prepareStatement(updateString)
        ){
            stmt.setString(1, attachment.getFileName());
            stmt.setString(2, attachment.getComment());
            stmt.setLong(3, attachment.getAttachmentId());
            stmt.executeUpdate();
        }
    }

    public long getNewAttachmentId(Connection conn) throws SQLException {
        LOGGER.info("method: getNewAttachmentId({})", conn);
        long newAttachmentId = 0;
        String query = "SELECT (auto_increment) " +
                "FROM information_schema.tables " +
                "WHERE table_name = 'attachment' " +
                "AND table_schema = 'contact_book'";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            newAttachmentId = rs.getLong(1);
        }
        return newAttachmentId;
    }
}

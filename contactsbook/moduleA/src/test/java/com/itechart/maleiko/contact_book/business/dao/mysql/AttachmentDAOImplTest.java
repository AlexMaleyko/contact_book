package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.AttachmentDAO;
import com.itechart.maleiko.contact_book.business.dao.DataSourceProvider;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttachmentDAOImplTest {
    private static DataSource dataSource;

    private final String FIND_ALL_META_INFO_QUERY =
            "SELECT attachment_id, file_path, file_name, upload_date, contact_id, comment FROM attachment " +
                    "WHERE deletion_date IS NULL";

    private final String DELETE_ALL_QUERY = "DELETE FROM attachment";

    private AttachmentDAO attachmentDAO = new AttachmentDAOImpl();
    private final Long PERMANENT_CONTACT_ID = 1L;

    @BeforeClass
    public static void initializeDataSource() throws IOException, SQLException {
        DataSourceProvider dataSourceProvider = DataSourceProvider.getInstance();
        dataSource = dataSourceProvider.getDataSource();
    }

    @Before
    @After
    public void cleanPhoneNumberTable() throws SQLException, IOException {
        try(Connection connection = dataSource.getConnection()) {
            deleteAllAttachmentMetaInfo(connection);
            deleteTestFolder();
        }
    }

    private void deleteAllAttachmentMetaInfo(Connection connection) throws SQLException {
        try(PreparedStatement stmt=connection.prepareStatement(DELETE_ALL_QUERY)){
            stmt.executeUpdate();
        }
    }

    private void deleteTestFolder() throws IOException {
        Properties properties = getFileStorageProperties();
        File testFolder = new File(properties.getProperty("contactFilesContainer"));
        if (testFolder.exists()) {
            FileUtils.forceDelete(testFolder);
        }
    }

    @Test
    public void testSave() throws Exception {
        List<Attachment> expectedAttachments = createSampleAttachments();
        try (Connection connection = dataSource.getConnection()){
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(expectedAttachments);
            List<Attachment> actualMetaInfo = retrieveAllMetaInfo();
            assertEquals(expectedAttachments.size(), actualMetaInfo.size());
            for (int i = 0; i < expectedAttachments.size(); i++) {
                Attachment expected = expectedAttachments.get(i);
                Attachment actual = actualMetaInfo.get(i);
                assertEquals(expected.getContactId(), actual.getContactId());
                assertEquals(expected.getFileName(), actual.getFileName());
                assertEquals(expected.getComment(), actual.getComment());
                Path fullPath = Paths.get(actual.getFilePath());
                assertTrue("file should exist", Files.exists(fullPath));
                assertTrue("file should be readable", Files.isReadable(fullPath));
            }
        }
    }

    @Test
    public void testUpdate() throws Exception{
        List<Attachment> sampleAttachments = createSampleAttachments();
        try(Connection connection = dataSource.getConnection()) {
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(sampleAttachments);

            List<Attachment> expectedMetaInfo = retrieveAllMetaInfo();
            assertEquals(sampleAttachments.size(), expectedMetaInfo.size());

            for (int i = 0; i < expectedMetaInfo.size(); i++) {
                Attachment info = expectedMetaInfo.get(i);
                info.setComment(String.valueOf(i));
                info.setFileName(String.valueOf(i));
            }
            attachmentDAO.update(expectedMetaInfo);

            List<Attachment> actualMetaInfo = retrieveAllMetaInfo();
            assertEquals(expectedMetaInfo.size(), actualMetaInfo.size());
            for (int i = 0; i < actualMetaInfo.size(); i++) {
                Attachment expected = expectedMetaInfo.get(i);
                Attachment actual = actualMetaInfo.get(i);
                assertEquals(expected.getFileName(), actual.getFileName());
                assertEquals(expected.getComment(), actual.getComment());
            }
        }
    }

    @Test
    public void testFindByContactId() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            List<Attachment> expectedAttachments = createSampleAttachments();
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(expectedAttachments);
            List<Attachment> retrievedAttachments = attachmentDAO.findByContactId(PERMANENT_CONTACT_ID);
            compareMetaInfo(expectedAttachments, retrievedAttachments);
        }
    }

    @Test
    public void shouldNotReturnAttachmentsNotPresentInFileSystem() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            List<Attachment> sampleAttachments = createSampleAttachments();
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(sampleAttachments);
            List<Attachment> retrievedAttachments = attachmentDAO.findByContactId(PERMANENT_CONTACT_ID);
            assertEquals(sampleAttachments.size(), retrievedAttachments.size());
            File fileForDeletion = new File(retrievedAttachments.get(0).getFilePath());
            if (fileForDeletion.exists()) {
                FileUtils.forceDelete(fileForDeletion);
            }
            sampleAttachments.remove(0);
            List<Attachment> retrievedAttachmentsAfterFileDeletion =
                    attachmentDAO.findByContactId(PERMANENT_CONTACT_ID);
            compareMetaInfo(sampleAttachments, retrievedAttachmentsAfterFileDeletion);
        }
    }

    private void compareMetaInfo(List<Attachment> expectedAttachments, List<Attachment> actualAttachments){
        assertEquals(expectedAttachments.size(), actualAttachments.size());
        for(int i = 0; i < expectedAttachments.size(); i++){
            Attachment expected = expectedAttachments.get(i);
            Attachment retrieved = actualAttachments.get(i);
            assertEquals(expected.getContactId(), retrieved.getContactId());
            assertEquals(expected.getFileName(), retrieved.getFileName());
            assertEquals(expected.getComment(), retrieved.getComment());
        }
    }

    @Test
    public void testDeleteByContactId() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(createSampleAttachments());
            List<Attachment> attachmentsBeforeDeletion = retrieveAllMetaInfo();
            attachmentDAO.deleteByContactId(PERMANENT_CONTACT_ID);
            List<Attachment> attachmentsLeftAfterDeletion = retrieveAllMetaInfo();
            assertEquals(0, attachmentsLeftAfterDeletion.size());
            for (Attachment attachment : attachmentsBeforeDeletion) {
                Path fullPath = Paths.get(attachment.getFilePath());
                assertTrue("file should not exist in file system", Files.notExists(fullPath));
            }
        }
    }

    @Test
    public void testDeleteByIds() throws Exception{
        try(Connection connection = dataSource.getConnection()) {
            attachmentDAO.setConnection(connection);
            attachmentDAO.save(createSampleAttachments());
            List<Attachment> attachmentsBeforeDeletion = retrieveAllMetaInfo();

            List<Long> attachmentIdsForDeletion = new ArrayList<>();
            Attachment attachmentForDeletion = attachmentsBeforeDeletion.get(0);
            attachmentIdsForDeletion.add(attachmentForDeletion.getAttachmentId());
            attachmentDAO.deleteByIds(attachmentIdsForDeletion);

            List<Attachment> attachmentsAfterDeletion = retrieveAllMetaInfo();
            int expectedNumberOfAttachments = attachmentsBeforeDeletion.size() - attachmentIdsForDeletion.size();
            assertEquals(expectedNumberOfAttachments, attachmentsAfterDeletion.size());
            assertTrue("should not contain deleted record", !attachmentsAfterDeletion.contains(attachmentForDeletion));
            Path fullPath = Paths.get(attachmentForDeletion.getFilePath());
            assertTrue("file should not exits in file system", Files.notExists(fullPath));
        }
    }

    private List<Attachment> createSampleAttachments() throws IOException {
        FileItemFactory factory = new DiskFileItemFactory();

        String sampleFileText1 = "text1";
        Attachment sampleAttachment1 = new Attachment();
        sampleAttachment1.setFileName("attachment1");
        sampleAttachment1.setUploadDate((new Timestamp(System.currentTimeMillis())));
        sampleAttachment1.setComment("comment1");
        sampleAttachment1.setContactId(PERMANENT_CONTACT_ID);
        FileItem mockFile1 = factory.createItem("formFieldName", "text/plain", true, "file1.txt");
        OutputStream os1 = mockFile1.getOutputStream();
        os1.write(sampleFileText1.getBytes());
        os1.close();
        sampleAttachment1.setFile(mockFile1);

        String sampleFileText2 = "secondFileText";
        Attachment sampleAttachment2 = new Attachment();
        sampleAttachment2.setFileName("attachment2");
        sampleAttachment2.setUploadDate((new Timestamp(System.currentTimeMillis())));
        sampleAttachment2.setComment("comment2");
        sampleAttachment2.setContactId(PERMANENT_CONTACT_ID);
        FileItem mockFile2 = factory.createItem("formFieldName", "text/plain", true, "file2.txt");
        OutputStream os2 = mockFile2.getOutputStream();
        os2.write(sampleFileText2.getBytes());
        os2.close();
        sampleAttachment2.setFile(mockFile2);

        List<Attachment> sampleAttachments = new ArrayList<>();
        sampleAttachments.add(sampleAttachment1);
        sampleAttachments.add(sampleAttachment2);
        return sampleAttachments;
    }

    private Properties getFileStorageProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(PhoneNumberDAOImplTest.class.getClassLoader().getResourceAsStream("fileStorage.properties"));
        return properties;
    }

    private List<Attachment> retrieveAllMetaInfo() throws SQLException{
        List<Attachment> attachments = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(FIND_ALL_META_INFO_QUERY)
        ){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(rs.getLong("attachment_id"));
                attachment.setFilePath(rs.getString("file_path"));
                attachment.setContactId(rs.getLong("contact_id"));
                attachment.setFileName(rs.getString("file_name"));
                attachment.setUploadDate(rs.getTimestamp("upload_date"));
                attachment.setComment(rs.getString("comment"));
                attachments.add(attachment);
            }
        }
        return attachments;
    }
}

package com.itechart.maleiko.contact_book.business.DAO;

import com.itechart.maleiko.contact_book.business.entity.Attachment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alexey on 15.03.2017.
 */
public interface AttachmentDAO {
    void save(Connection conn, Attachment attachment) throws Exception;

    List<Attachment> findByContactId(Connection conn, long contactId) throws SQLException;

    void deleteByContactId(Connection conn, long id) throws SQLException, IOException;

    long getNewAttachmentId(Connection conn) throws SQLException;

    void deleteById(Connection conn, long id) throws SQLException, IOException;

    void update(Connection conn, Attachment attachment) throws SQLException;
}

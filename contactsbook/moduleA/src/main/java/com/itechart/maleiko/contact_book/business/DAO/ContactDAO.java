package com.itechart.maleiko.contact_book.business.DAO;

import com.itechart.maleiko.contact_book.business.entity.Contact;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey on 15.03.2017.
 */
public interface ContactDAO {
    List<Contact> getAll(Connection conn, int skip, int limit) throws SQLException;

    Contact findById(Connection conn, long id) throws SQLException;

    void save(Connection conn, Contact contact) throws Exception;

    void delete(Connection conn, Contact contact) throws SQLException;

    void update(Connection conn, Contact contact) throws Exception;

    void deleteByContactId(Connection conn, long id) throws SQLException, IOException;

    PairResultSize findByGivenParameters(Connection conn, Map<String, Object> fieldValue) throws SQLException;

    long getMaxContactId(Connection conn) throws SQLException;

    long getLastContactId(Connection conn) throws SQLException;

    int getNumberOfContacts(Connection conn) throws SQLException;
}

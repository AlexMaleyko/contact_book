package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.PhoneNumberDAO;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberDAOImpl implements PhoneNumberDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberDAOImpl.class);

    private Connection conn;

    private static final String SAVE_QUERY = "INSERT into phone_number "+
                "(country_code, operator_code, number, type, comment, contact_id) "+
                "VALUES (?,?,?,?,?,?)";

    private static final String FIND_BY_CONTACT_ID_QUERY =
            "SELECT number_id, country_code, operator_code, number,type, comment, contact_id "+
                "FROM phone_number WHERE contact_id = ? AND deletion_date IS NULL";

    private static final String UPDATE_QUERY = "UPDATE phone_number "+
                "SET country_code = ?, operator_code = ?, number = ?, type = ?, comment = ? "+
                "WHERE number_id = ?";

    private static final String DELETE_BY_ID_QUERY =
                "UPDATE phone_number SET deletion_date = CURRENT_TIMESTAMP WHERE number_id = ?";

    private static final String DELETE_BY_CONTACT_ID_QUERY =
                "UPDATE phone_number SET deletion_date = CURRENT_TIMESTAMP WHERE contact_id = ?";

    public void setConnection(Connection connection){
        this.conn = connection;
    }

    @Override
    public void save(List<PhoneNumber> phoneNumbers) throws DAOException {
        try(PreparedStatement savePhoneNumber = conn.prepareStatement(SAVE_QUERY)){
            for(PhoneNumber number : phoneNumbers) {
                savePhoneNumber.setString(1, number.getCountryCode());
                savePhoneNumber.setString(2, number.getOperatorCode());
                savePhoneNumber.setString(3, number.getNumber());
                savePhoneNumber.setString(4, number.getType());
                savePhoneNumber.setString(5, number.getComment());
                savePhoneNumber.setLong(6, number.getContactId());
                savePhoneNumber.addBatch();
            }
           savePhoneNumber.executeBatch();
        } catch (SQLException e){
            String message = "Error saving phone number. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public void update(List<PhoneNumber> numbers) throws DAOException{
        try(PreparedStatement stmt=conn.prepareStatement(UPDATE_QUERY)){
            for(PhoneNumber number : numbers) {
                stmt.setString(1, number.getCountryCode());
                stmt.setString(2, number.getOperatorCode());
                stmt.setString(3, number.getNumber());
                stmt.setString(4, number.getType());
                stmt.setString(5, number.getComment());
                stmt.setLong(6, number.getNumberId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e){
            String message = "Error updating phone number. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public List<PhoneNumber> findByContactId(long contactId) throws DAOException {
        List<PhoneNumber> numbers=new ArrayList<>();
        try(PreparedStatement stmt=conn.prepareStatement(FIND_BY_CONTACT_ID_QUERY)){
            stmt.setLong(1,contactId);
            try(ResultSet rs=stmt.executeQuery()) {
                while (rs.next()) {
                    PhoneNumber number = generateEntityObjectFromResultSetRow(rs);
                    numbers.add(number);
                }
            }
        } catch (SQLException e){
            String message = "Error finding phone number by id. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
        return numbers;
    }

    PhoneNumber generateEntityObjectFromResultSetRow(ResultSet rs) throws SQLException{
        PhoneNumber number=new PhoneNumber();
        number.setNumberId(rs.getInt(1));
        number.setCountryCode(rs.getString(2));
        number.setOperatorCode(rs.getString(3));
        number.setNumber(rs.getString(4));
        number.setType(rs.getString(5));
        number.setComment(rs.getString(6));
        number.setContactId(rs.getInt(7));
        return number;
    }

    @Override
    public void deleteByIds(List<Long> ids) throws DAOException{
        try(PreparedStatement stmt=conn.prepareStatement(DELETE_BY_ID_QUERY)){
            for(long id : ids) {
                stmt.setLong(1, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }catch (SQLException e){
            String message = "Error deleting phone numbers by ids. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() +  " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }

    @Override
    public void deleteByContactId(long id) throws DAOException{
        try(PreparedStatement stmt=conn.prepareStatement(DELETE_BY_CONTACT_ID_QUERY)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }catch (SQLException e){
            String message = "Error deleting phone number by contact id. " +
                    "SQLState: " + e.getSQLState() + " Error code: " + e.getErrorCode() + " Message: " + e.getMessage();
            throw new DAOException(message, e);
        }
    }
}
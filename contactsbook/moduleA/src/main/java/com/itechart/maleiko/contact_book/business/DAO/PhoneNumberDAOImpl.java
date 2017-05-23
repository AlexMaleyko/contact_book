package com.itechart.maleiko.contact_book.business.DAO;

import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 17.03.2017.
 */
public class PhoneNumberDAOImpl implements PhoneNumberDAO {

    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(PhoneNumberDAOImpl.class);

    @Override
    public void save(Connection conn,PhoneNumber phoneNumber) throws SQLException {

        LOGGER.info("method: save({}, {})", conn, phoneNumber.getClass().getSimpleName());

        String saveString="INSERT into contact_book.phone_number "+
                          "(country_code, operator_code,number,type,comment,contact_id) "+
                          "VALUES (?,?,?,?,?,?)";
        LOGGER.info("dao comment {}", phoneNumber.getComment());

        try(
                java.sql.PreparedStatement saveStatement=
                    conn.prepareStatement(saveString)
            ){
           saveStatement.setString(1,phoneNumber.getCountryCode());
           saveStatement.setString(2,phoneNumber.getOperatorCode());
           saveStatement.setString(3,phoneNumber.getNumber());
           saveStatement.setString(4,phoneNumber.getType());
           saveStatement.setString(5,phoneNumber.getComment());
           saveStatement.setLong(6,phoneNumber.getContactId());
           saveStatement.executeUpdate();
        }

    }

    @Override
    public List<PhoneNumber> findByContactId(Connection conn, long contactId) throws SQLException {

        LOGGER.info("method: findByContactId({}, {})", conn, contactId);

        List<PhoneNumber> numberList=new ArrayList<>();
        String query= "SELECT number_id,country_code,operator_code,number,type,comment "+
                      "FROM phone_number WHERE contact_id= ? AND deletion_date IS NULL";

        try(
                PreparedStatement stmt=conn.prepareStatement(query)
            ){
            stmt.setLong(1,contactId);
            ResultSet rs=stmt.executeQuery();

            while(rs.next()){
                PhoneNumber number=new PhoneNumber();
                number.setNumberId(rs.getInt(1));
                number.setCountryCode(rs.getString(2));
                number.setOperatorCode(rs.getString(3));
                number.setNumber(rs.getString(4));
                number.setType(rs.getString(5));
                number.setComment(rs.getString(6));
                number.setContactId(contactId);
                numberList.add(number);
            }
        }

        return numberList;
    }

    @Override
    public List<PhoneNumber> getAll(Connection conn) throws  SQLException{

        LOGGER.info("method: getAll({})", conn);

        List<PhoneNumber> numberList=new ArrayList<>();
        String query= "SELECT number_id, country_code, operator_code,"+
                      "number, type, comment, contact_id FROM phone_number WHERE deletion_date IS NULL";

        try(
                PreparedStatement stmt=conn.prepareStatement(query)
        ){
            ResultSet rs=stmt.executeQuery();

            while(rs.next()){
                PhoneNumber number=new PhoneNumber();
                number.setNumberId(rs.getInt(1));
                number.setCountryCode(rs.getString(2));
                number.setOperatorCode(rs.getString(3));
                number.setNumber(rs.getString(4));
                number.setType(rs.getString(5));
                number.setComment(rs.getString(6));
                number.setContactId(rs.getInt(7));
                numberList.add(number);
            }
        }

        return numberList;
    }

    @Override
    public void update(Connection conn, PhoneNumber phoneNumber) throws SQLException{

        LOGGER.info("method: update({}, {})", conn, phoneNumber.getClass().getSimpleName());

        String updateString="UPDATE phone_number "+
                            "SET country_code=?, operator_code=?, number=?, type=?, comment=? "+
                            "WHERE number_id=?";

        try(
                PreparedStatement stmt=conn.prepareStatement(updateString)
        ){
            stmt.setString(1, phoneNumber.getCountryCode());
            stmt.setString(2, phoneNumber.getOperatorCode());
            stmt.setString(3, phoneNumber.getNumber());
            stmt.setString(4, phoneNumber.getType());
            stmt.setString(5, phoneNumber.getComment());
            stmt.setLong(6, phoneNumber.getNumberId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Connection conn, PhoneNumber phoneNumber) throws  SQLException{

        LOGGER.info("method: delete({}, {})", conn, phoneNumber.getClass().getSimpleName());

        String deleteString="UPDATE phone_number SET deletion_date=CURRENT_TIMESTAMP WHERE number_id= ?";

        try(
                PreparedStatement stmt=conn.prepareStatement(deleteString)
        ){
            stmt.setLong(1, phoneNumber.getNumberId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteByContactId(Connection conn, long id) throws SQLException {

        LOGGER.info("method: deleteByContactId({}, {})", conn, id);

        String deleteString="UPDATE phone_number SET deletion_date=CURRENT_TIMESTAMP WHERE contact_id= ?";

        try(
                PreparedStatement stmt=conn.prepareStatement(deleteString)
        ){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void deleteById(Connection conn, long id) throws  SQLException{
        LOGGER.info("method: deleteById({}, {})", conn, id);

        String deleteString="UPDATE phone_number SET deletion_date=CURRENT_TIMESTAMP WHERE number_id= ?";

        try(
                PreparedStatement stmt=conn.prepareStatement(deleteString)
        ){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

}

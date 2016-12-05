package model.dao.impl;

import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Payment;
import model.entities.TypeOfPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dao object for {@link model.entities.Payment}
 * @author kara.vladimir2@gmail.com.
 */
public class PaymentDaoImpl extends AbstractDaoImpl{

    public static final String QUERY_SAVE = "INSERT INTO payment(Now(),"+Fields.PAY_AMOUNT+","
            +Fields.PAY_TYPE+","+Fields.PAY_ID_SENDER_CLIENT+","+Fields.PAY_ID_SENDER_ACCOUNT+","
            +Fields.PAY_ID_SENDER_CARD+","+Fields.PAY_ID_RECIP_ACCOUNT+")" + " VALUES(?,?,?,?,?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM payment " +
            "LEFT JOIN account ON("+Fields.PAY_ID_SENDER_ACCOUNT+" = "+Fields.ACC_ID+" OR "
                +Fields.PAY_ID_RECIP_ACCOUNT+" = "+Fields.ACC_ID+")" +
            "LEFT JOIN client ON("+Fields.PAY_ID_SENDER_CLIENT+" = "+Fields.CL_ID+")" +
            "left join creditCard ON("+Fields.PAY_ID_SENDER_CARD+" = "+Fields.CARD_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+"WHERE "+ Fields.PAY_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.PAY_ID+" = ?;";
    //empty update. Update for payments table not supported
    public static final String QUERY_UPDATE = "UPDATE payment SET "+Fields.PAY_ID+" = "+Fields.PAY_ID+"" +
            " WHERE "+Fields.PAY_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM account WHERE "+Fields.PAY_ID+" = ?;";

    public PaymentDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSaveQuery() {
        return QUERY_SAVE;
    }

    @Override
    public String getLastInsertQuery() {
        return QUERY_LAST_INSERT;
    }

    @Override
    public String getByPKQuery() {
        return QUERY_FIND_BY_PK;
    }

    @Override
    public String getUpdateQuery() {
        return QUERY_UPDATE;
    }

    @Override
    public String getDeleteQuery() {
        return QUERY_DELETE;
    }

    @Override
    public String getSelectAllQuery() {
        return QUERY_SELECT_ALL;
    }

    @Override
    public PreparedStatement prepareStatementForUpdate(PreparedStatement preparedStatement, Identified identified) {
        Payment payment = (Payment) identified;
        try {
            preparedStatement.setInt(1, payment.getID());
        } catch (SQLException e) {
            throw new DaoException(ERR_UPDATE,e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) {
        Payment payment = (Payment) identified;
        try {
            preparedStatement.setBigDecimal(1,payment.getAmount() );
            preparedStatement.setInt(2, payment.getTypeOfPayment().ordinal());
            preparedStatement.setInt(3, payment.getSenderClient().getID());
            preparedStatement.setInt(4, payment.getSenderAccount().getID());
            preparedStatement.setInt(5, payment.getSenderCard().getID());
            preparedStatement.setInt(6, payment.getRecipientAccount().getID());
        } catch (SQLException e) {
            throw new DaoException(ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<Payment> parseResultSet(ResultSet rs) {
        Set<Payment> set = new HashSet<>();
        List<Payment> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                listResult.add(parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
        return listResult;
    }

    @Override
    public Payment parseResult(ResultSet rs) {
        try {
            return new Payment(rs.getInt(Fields.PAY_ID),rs.getTimestamp(Fields.PAY_TIME),rs.getBigDecimal(Fields.PAY_AMOUNT),
                    TypeOfPayment.values()[rs.getInt(Fields.PAY_TYPE)]);
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
    }
}

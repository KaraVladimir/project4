package model.dao.impl;

import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Dao object for {@link model.entities.Payment}
 * @author kara.vladimir2@gmail.com.
 */
public class PaymentDaoImpl extends AbstractDaoImpl{
    private static final Logger LOG = Logger.getLogger(PaymentDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO payment("+Fields.PAY_AMOUNT+","
            +Fields.PAY_TYPE+","+Fields.PAY_ID_SENDER_CLIENT+","+Fields.PAY_ID_SENDER_ACCOUNT+","
            +Fields.PAY_ID_SENDER_CARD+","+Fields.PAY_ID_RECIP_ACCOUNT+")" + " VALUES(?,?,?,?,?,?)";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM payment " +
            "LEFT JOIN account  ON("+Fields.PAY_ID_SENDER_ACCOUNT+" = "+Fields.ACC_ID+")" +
            "LEFT JOIN account as accR ON("+Fields.PAY_ID_SENDER_ACCOUNT+" = accR."+Fields.ACC_ID+")" +
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
    public PreparedStatement prepareStatementForUpdate(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Payment payment = (Payment) identified;
        try {
            preparedStatement.setInt(1, payment.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_UPDATE,e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Payment payment = (Payment) identified;
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        try {
            preparedStatement.setBigDecimal(1,payment.getAmount() );
            preparedStatement.setInt(2, payment.getTypeOfPayment().ordinal());
            if (payment.getSenderClient() != null) {
                preparedStatement.setInt(3, payment.getSenderClient().getID());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }

            if (payment.getSenderAccount()!=null){
                preparedStatement.setInt(4, payment.getSenderAccount().getID());
            }else {
                preparedStatement.setNull(4, Types.INTEGER);
            }
            if (payment.getSenderCard()!=null){
                preparedStatement.setInt(5, payment.getSenderCard().getID());
            }else {
                preparedStatement.setNull(5, Types.INTEGER);
            }
            preparedStatement.setInt(6, payment.getRecipientAccount().getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<Payment> parseResultSet(ResultSet rs) throws DaoException {
        ClientDaoImpl clientDao = new ClientDaoImpl();
        AccountDaoImpl accountDao = new AccountDaoImpl();
        CreditCardDaoImpl cardDao = new CreditCardDaoImpl();

        Map<Number, Payment> paymentMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();
        Map<Number, CreditCard> cardMap = new HashMap<>();

        List<Payment> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                paymentMap.put(rs.getInt(Fields.PAY_ID), parseResult(rs));
                clientMap.put(rs.getInt(Fields.CL_ID), clientDao.parseResult(rs));
                cardMap.put(rs.getInt(Fields.CARD_ID), cardDao.parseResult(rs));
                clientMap.put(rs.getInt(Fields.CL_ID), clientDao.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderClient(
                        clientMap.get(rs.getInt(Fields.PAY_ID_SENDER_CLIENT)));
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderCard(
                        cardMap.get(rs.getInt(Fields.PAY_ID_SENDER_CARD)));
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderClient(
                        clientMap.get(rs.getInt(Fields.PAY_ID_SENDER_CLIENT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return new ArrayList<>(paymentMap.values());
    }

    @Override
    public Payment parseResult(ResultSet rs) throws DaoException {
        try {
            return new Payment(rs.getInt(Fields.PAY_ID),rs.getTimestamp(Fields.PAY_TIME),rs.getBigDecimal(Fields.PAY_AMOUNT),
                    TypeOfPayment.values()[rs.getInt(Fields.PAY_TYPE)]);
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

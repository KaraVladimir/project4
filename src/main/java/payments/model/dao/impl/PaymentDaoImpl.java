package payments.model.dao.impl;

import payments.helper.Msgs;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import payments.model.dao.PaymentDao;
import payments.model.entities.*;

import java.sql.*;
import java.util.*;

import static payments.model.dao.impl.Fields.*;

/**
 * Dao object for {@link Payment}
 *
 * @author kara.vladimir2@gmail.com.
 */
public class PaymentDaoImpl extends AbstractDaoImpl implements PaymentDao {
    private static final Logger LOG = Logger.getLogger(PaymentDaoImpl.class);

    private static final String QUERY_SAVE = "INSERT INTO payment(" + PAY_AMOUNT + "," + PAY_TYPE + ","
            + PAY_ID_SENDER_ACCOUNT + "," + PAY_ID_RECIP_ACCOUNT + ")" + " VALUES(?,?,?,?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM payment " +
            "LEFT JOIN account  as accS ON(" + PAY_ID_SENDER_ACCOUNT + " = accS." + ACC_ID + ")" +
            "LEFT JOIN account as accR ON(" + PAY_ID_SENDER_ACCOUNT + " = accR." + ACC_ID + ")";
    private static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL + "WHERE " + PAY_ID + " = ?;";
    //empty update. Update for payments table not supported
    private static final String QUERY_UPDATE = "UPDATE payment SET " + PAY_ID + " = " + PAY_ID + "" +
            " WHERE " + PAY_ID + "= ?;";
    private static final String QUERY_DELETE = "DELETE * FROM account WHERE " + PAY_ID + " = ?;";
    private static final String QUERY_PAYMENTS_BY_USER = QUERY_SELECT_ALL +
            "join (SELECT " + ACC_ID + " FROM user " +
            "inner join client on(" + USR_ID_CLIENT + " = " + CL_ID + ") " +
            "where " + USR_ID + " = ?) as listAcc " +
            "      on (listAcc." + ACC_ID + "=accR." + ACC_ID + " or listAcc." + ACC_ID + "=accS." + ACC_ID + ")";


    public PaymentDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSaveQuery() {
        return QUERY_SAVE;
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
            throw new DaoException(getLogger(), Msgs.ERR_UPDATE, e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Payment payment = (Payment) identified;
        try {
            preparedStatement.setBigDecimal(1, payment.getAmount());
            preparedStatement.setInt(2, payment.getTypeOfPayment().ordinal());
            if (payment.getSenderAccount() != null) {
                preparedStatement.setInt(3, payment.getSenderAccount().getID());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            preparedStatement.setInt(4, payment.getRecipientAccount().getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SAVE_QUERY, e);
        }
        return preparedStatement;
    }

    @Override
    protected List<Payment> parseResultSet(ResultSet rs) throws DaoException {
        Map<Number, Payment> paymentMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();
        try {
            while (rs.next()) {
                paymentMap.put(rs.getInt(PAY_ID), parseResult(rs));
                accountMap.put(rs.getInt(ACC_ID), AccountDaoImpl.parseResult("accS", rs));
                accountMap.put(rs.getInt(ACC_ID), AccountDaoImpl.parseResult("accR", rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                paymentMap.get(rs.getInt(PAY_ID)).setSenderAccount(
                        accountMap.get(rs.getInt(PAY_ID_SENDER_ACCOUNT)));
                paymentMap.get(rs.getInt(PAY_ID)).setRecipientAccount(
                        accountMap.get(rs.getInt(PAY_ID_RECIP_ACCOUNT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_PARSING, e);
        }
        return new ArrayList<>(paymentMap.values());
    }

    private static Payment parseResult(String alias, ResultSet rs) throws DaoException {
        alias = (alias.isEmpty()) ? alias : alias + ".";
        try {
            return new Payment(rs.getInt(alias + PAY_ID), rs.getTimestamp(alias + PAY_TIME),
                    rs.getBigDecimal(alias + PAY_AMOUNT), TypeOfPayment.values()[rs.getInt(alias + PAY_TYPE)]);
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ERR_PARSING, e);
        }
    }

    private static Payment parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public List<Payment> findPaymentsByClient(Number usrId) throws DaoException {
        List<Payment> payments;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_PAYMENTS_BY_USER)) {
            preparedStatement.setInt(1, (Integer) usrId);
            payments = parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_PAYMENTS_BY_USER, e);
        }
        return payments;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}

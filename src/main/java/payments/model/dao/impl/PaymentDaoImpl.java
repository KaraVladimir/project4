package payments.model.dao.impl;

import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import payments.model.dao.PaymentDao;
import payments.model.entities.*;

import java.sql.*;
import java.util.*;

/**
 * Dao object for {@link Payment}
 * @author kara.vladimir2@gmail.com.
 */
public class PaymentDaoImpl extends AbstractDaoImpl implements PaymentDao {
    private static final Logger LOG = Logger.getLogger(PaymentDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO payment("+Fields.PAY_AMOUNT+","
            +Fields.PAY_TYPE+","+Fields.PAY_ID_SENDER_CLIENT+","+Fields.PAY_ID_SENDER_ACCOUNT+","
            +Fields.PAY_ID_SENDER_CARD+","+Fields.PAY_ID_RECIP_ACCOUNT+")" + " VALUES(?,?,?,?,?,?)";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM payment " +
            "LEFT JOIN account  as accS ON("+Fields.PAY_ID_SENDER_ACCOUNT+" = accS."+Fields.ACC_ID+")" +
            "LEFT JOIN account as accR ON("+Fields.PAY_ID_SENDER_ACCOUNT+" = accR."+Fields.ACC_ID+")" +
            "LEFT JOIN client ON("+Fields.PAY_ID_SENDER_CLIENT+" = "+Fields.CL_ID+")" +
            "left join creditCard ON("+Fields.PAY_ID_SENDER_CARD+" = "+Fields.CARD_ID+")";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.PAY_ID+" = ?;";
    //empty update. Update for payments table not supported
    public static final String QUERY_UPDATE = "UPDATE payment SET "+Fields.PAY_ID+" = "+Fields.PAY_ID+"" +
            " WHERE "+Fields.PAY_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM account WHERE "+Fields.PAY_ID+" = ?;";
    private static final String QUERY_PAYMENTS_BY_USER = QUERY_SELECT_ALL+
            "join (select idAcc from user inner join client on(FK_Usr_idCl = idCl) " +
            "      left join creditcard on (idCl=FK_Cc_idCl)" +
            "      left join account on(FK_Cc_idAcc=idAcc) where idUsr = ?) as listAcc " +
            "      on (listAcc.idAcc=accR.idAcc or listAcc.idAcc=accS.idAcc)";


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

        Map<Number, Payment> paymentMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();
        Map<Number, CreditCard> cardMap = new HashMap<>();

        List<Payment> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                paymentMap.put(rs.getInt(Fields.PAY_ID), parseResult(rs));
                clientMap.put(rs.getInt(Fields.CL_ID), ClientDaoImpl.parseResult(rs));
                cardMap.put(rs.getInt(Fields.CARD_ID), CreditCardDaoImpl.parseResult(rs));
                accountMap.put(rs.getInt(Fields.ACC_ID), AccountDaoImpl.parseResult("accS",rs));
                accountMap.put(rs.getInt(Fields.ACC_ID), AccountDaoImpl.parseResult("accR",rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderClient(
                        clientMap.get(rs.getInt(Fields.PAY_ID_SENDER_CLIENT)));
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderCard(
                        cardMap.get(rs.getInt(Fields.PAY_ID_SENDER_CARD)));
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setSenderAccount(
                        accountMap.get(rs.getInt(Fields.PAY_ID_SENDER_ACCOUNT)));
                paymentMap.get(rs.getInt(Fields.PAY_ID)).setRecipientAccount(
                        accountMap.get(rs.getInt(Fields.PAY_ID_RECIP_ACCOUNT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return new ArrayList<>(paymentMap.values());
    }

    public static Payment parseResult(String alias,ResultSet rs) throws DaoException {
        alias = (alias.isEmpty())?alias:alias + ".";
        try {
            return new Payment(rs.getInt(alias+Fields.PAY_ID),rs.getTimestamp(alias+Fields.PAY_TIME),
                    rs.getBigDecimal(alias+Fields.PAY_AMOUNT), TypeOfPayment.values()[rs.getInt(alias+Fields.PAY_TYPE)]);
        } catch (SQLException e) {
            throw new DaoException(LOG,ERR_PARSING, e);
        }
    }

    public static Payment parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public List<Payment> findPaymentsByClient(Number usrId) throws DaoException {
        List<Payment> payments;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_PAYMENTS_BY_USER)){
            preparedStatement.setInt(1, (Integer) usrId);
            payments = (List<Payment>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_GET_PAYMENTS_BY_USER,e);
        }
        return payments;
    }
    
    @Override
    public Logger getLogger() {
        return LOG;
    }
}

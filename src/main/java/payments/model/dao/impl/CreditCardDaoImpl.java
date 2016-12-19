package payments.model.dao.impl;

import payments.helper.Msgs;
import payments.model.dao.CreditCardDao;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import payments.model.entities.Account;
import payments.model.entities.Client;
import payments.model.entities.CreditCard;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static payments.model.dao.impl.Fields.*;

/**
 * Dao object for {@link CreditCard}
 *
 * @author kara.vladimir2@gmail.com.
 */
public class CreditCardDaoImpl extends AbstractDaoImpl implements CreditCardDao {
    private static final Logger LOG = Logger.getLogger(CreditCardDaoImpl.class);

    private static final String QUERY_SAVE = "INSERT INTO creditCard(" + CARD_NUMBER +
            "," + CARD_ID_CLIENT + "," + CARD_ID_ACCOUNT + ")" + " VALUES(?,?,?);";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM creditCard " +
            "LEFT JOIN client ON(" + CARD_ID_CLIENT + "=" + CL_ID + ")" +
            "LEFT JOIN account ON(" + CARD_ID_ACCOUNT + "=" + ACC_ID + ")";
    private static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL + "WHERE " + CARD_ID + " = ?;";
    public static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL + "WHERE " + CARD_NUMBER + " = ?;";
    private static final String QUERY_FIND_BY_ACCOUNT = QUERY_SELECT_ALL + "WHERE "
            + CARD_ID_ACCOUNT + " = ?";
    private static final String QUERY_UPDATE = "UPDATE creditCard SET " + CARD_NUMBER + "= ?, "
            + CARD_ID_CLIENT + " = ?, " + CARD_ID_ACCOUNT + " = ? WHERE " + CARD_ID + "= ?;";
    private static final String QUERY_DELETE = "DELETE * FROM creditCard WHERE " + CARD_ID + " = ?;";

    public CreditCardDaoImpl(Connection connection) {
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
        CreditCard card = (CreditCard) identified;
        try {
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setInt(2, card.getClient().getID());
            preparedStatement.setInt(3, card.getAccount().getID());
            preparedStatement.setInt(4, card.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_UPDATE, e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        CreditCard card = (CreditCard) identified;
        try {
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setInt(2, card.getClient().getID());
            preparedStatement.setInt(3, card.getAccount().getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SAVE_QUERY, e);
        }
        return preparedStatement;
    }

    @Override
    protected List<CreditCard> parseResultSet(ResultSet rs) throws DaoException {

        Map<Number, CreditCard> cardMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();

        try {
            while (rs.next()) {
                cardMap.put(rs.getInt(CARD_ID), parseResult(rs));
                clientMap.put(rs.getInt(CL_ID), ClientDaoImpl.parseResult(rs));
                accountMap.put(rs.getInt(ACC_ID), AccountDaoImpl.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                cardMap.get(rs.getInt(CARD_ID)).setClient(
                        clientMap.get(rs.getInt(CARD_ID_CLIENT)));
                cardMap.get(rs.getInt(CARD_ID)).setAccount(
                        accountMap.get(rs.getInt(CARD_ID_ACCOUNT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_PARSING, e);
        }
        return new ArrayList<>(cardMap.values());
    }

    private static CreditCard parseResult(String alias, ResultSet rs) throws DaoException {
        alias = (alias.isEmpty()) ? alias : alias + ".";
        try {
            return new CreditCard(rs.getInt(alias + CARD_ID), rs.getString(alias + CARD_NUMBER));
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ERR_PARSING, e);
        }
    }

    public static CreditCard parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public CreditCard findCreditCardByAccount(Account account) throws DaoException {
        List<CreditCard> cardList;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_ACCOUNT)) {
            preparedStatement.setInt(1, account.getID());
            cardList = (List<CreditCard>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_ACCOUNT_QUERY, e);
        }
        if (cardList == null || cardList.size() == 0 || cardList.size() > 1) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_ACCOUNT_QUERY);
        }
        return cardList.get(0);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}

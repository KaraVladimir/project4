package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Account;
import model.entities.Client;
import model.entities.CreditCard;
import model.entities.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dao object for {@link CreditCard}
 * @author kara.vladimir2@gmail.com.
 */
public class CreditCardDaoImpl extends AbstractDaoImpl implements HaveUniqueField{
    private static final Logger LOG = Logger.getLogger(CreditCardDaoImpl.class);


    public static final String QUERY_SAVE = "INSERT INTO creditCard("+Fields.CARD_NUMBER+
            ","+Fields.CARD_ID_CLIENT+","+Fields.CARD_ID_ACCOUNT+")" + " VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM creditCard " +
            "LEFT JOIN client ON("+Fields.CARD_ID_CLIENT+"="+Fields.CL_ID+")" +
            "LEFT JOIN account ON("+Fields.CARD_ID_ACCOUNT+"="+Fields.ACC_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+
            "WHERE "+Fields.CARD_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.CARD_ID+" = ?;";
    public static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL+"WHERE "+Fields.CARD_NUMBER+" = ?;";
    public static final String QUERY_FIND_BY_ACCOUNT = QUERY_SELECT_ALL + "WHERE "
            + Fields.CARD_ID_ACCOUNT + " = ?";
    public static final String QUERY_UPDATE = "UPDATE creditCard SET "+Fields.CARD_NUMBER+"= ?, "
            +Fields.CARD_ID_CLIENT+" = ?, "+Fields.CARD_ID_ACCOUNT+" = ? WHERE "+Fields.CARD_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM creditCard WHERE "+Fields.CARD_ID+" = ?;";

    private static final String ERR_GET_BY_ACCOUNT_QUERY = "find by account failed";

    public CreditCardDaoImpl() {
    }

    public CreditCardDaoImpl(Connection connection) {
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
            CreditCard card = (CreditCard) identified;
            try {
                preparedStatement.setString(1, card.getCardNumber());
                preparedStatement.setInt(2, card.getClient().getID());
                preparedStatement.setInt(3, card.getAccount().getID());
                preparedStatement.setInt(4,card.getID());
            } catch (SQLException e) {
                throw new DaoException(getLogger(),ERR_UPDATE,e);
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
            throw new DaoException(getLogger(),ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<Identified> parseResultSetGen(ResultSet resultSet) throws DaoException {
        List<Identified> list = (List<Identified>) parseResultSet(resultSet);
        return list;
    }

    @Override
    public String getQueryFindByUniqueField() {
        return QUERY_FIND_BY_NUMBER;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<? super CreditCard> parseResultSet(ResultSet rs) throws DaoException {
        ClientDaoImpl clientDao = new ClientDaoImpl();
        AccountDaoImpl accountDao = new AccountDaoImpl();

        Map<Number, CreditCard> cardMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();

        try {
            while (rs.next()) {
                cardMap.put(rs.getInt(Fields.CARD_ID), parseResult(rs));
                clientMap.put(rs.getInt(Fields.CL_ID), clientDao.parseResult(rs));
                accountMap.put(rs.getInt(Fields.ACC_ID), accountDao.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                cardMap.get(rs.getInt(Fields.CARD_ID)).setClient(
                        clientMap.get(rs.getInt(Fields.CARD_ID_CLIENT)));
                cardMap.get(rs.getInt(Fields.CARD_ID)).setAccount(
                        accountMap.get(rs.getInt(Fields.CARD_ID_ACCOUNT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return new ArrayList<>(cardMap.values());
    }

    @Override
    public CreditCard parseResult(ResultSet rs) throws DaoException {
        try {
            return new CreditCard(rs.getInt(Fields.CARD_ID),rs.getString(Fields.CARD_NUMBER));
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
    }

    public CreditCard findCreditCardByAccount(Account account) throws DaoException {
        List<CreditCard> cardList;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_ACCOUNT);
            preparedStatement.setInt(1, account.getID());
            cardList = (List<CreditCard>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_GET_BY_ACCOUNT_QUERY,e);
        }
        if (cardList == null || cardList.size() == 0||cardList.size()>1) {
            throw new DaoException(getLogger(),ERR_GET_BY_ACCOUNT_QUERY);
        }
        return cardList.get(0);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

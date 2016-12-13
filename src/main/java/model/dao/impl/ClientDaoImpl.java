package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Account;
import model.entities.Client;
import model.entities.CreditCard;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Dao object for {@link Client}
 * @author kara.vladimir2@gmail.com.
 */
public class ClientDaoImpl extends AbstractDaoImpl implements HaveUniqueField {
    private static final Logger LOG = Logger.getLogger(ClientDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO client("+Fields.CL_FNAME+
            ","+Fields.CL_NAME+","+Fields.CL_EMAIL+") VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM client " +
            "LEFT JOIN creditCard ON("+Fields.CL_ID+"="+Fields.CARD_ID_CLIENT +")" +
            "left join account ON("+Fields.CARD_ID_ACCOUNT+"="+Fields.ACC_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+" WHERE "+Fields.CL_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+" WHERE "+Fields.CL_ID+" = ?;";
    public static final String QUERY_FIND_BY_EMAIL = QUERY_SELECT_ALL +" WHERE " + Fields.CL_EMAIL + "=?";
    public static final String QUERY_UPDATE = "UPDATE client SET "+Fields.CL_FNAME+"= ?, "+Fields.CL_NAME+" = ?, "
            +Fields.CL_EMAIL+" = ? WHERE "+Fields.CL_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM client WHERE "+Fields.CL_ID+" = ?;";

    public ClientDaoImpl() {
    }

    public ClientDaoImpl(Connection connection) {
        super(connection);
    }

    public List<Identified> parseResultSetGen(ResultSet resultSet) throws DaoException {
        List<Identified> clients = (List<Identified>) parseResult(resultSet);
        return clients;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getQueryFindByUniqueField() {
        return QUERY_FIND_BY_EMAIL;
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
    public PreparedStatement prepareStatementForUpdate
            (PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Client client = (Client) identified;
        try {
            preparedStatement.setString(1, client.getFamilyName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setInt(4, client.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_UPDATE,e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave
            (PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Client client = (Client) identified;
        try {
            preparedStatement.setString(1, client.getFamilyName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getEmail());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<? super Client> parseResultSet(ResultSet rs) throws DaoException {
        List<Client> clients = new ArrayList<>();

        CreditCardDaoImpl cardDao = new CreditCardDaoImpl();
        AccountDaoImpl accountDao = new AccountDaoImpl();
        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, CreditCard> cardMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();

        CreditCardDaoImpl creditCardDao = new CreditCardDaoImpl(connection);
        try {
            while (rs.next()) {
                clientMap.put(rs.getInt(Fields.CL_ID),parseResult(rs));
                cardMap.put(rs.getInt(Fields.CARD_ID),cardDao.parseResult(rs));
                accountMap.put(rs.getInt(Fields.ACC_ID),accountDao.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                cardMap.get(rs.getInt(Fields.CARD_ID)).setAccount(
                        accountMap.get(rs.getInt(Fields.CARD_ID_ACCOUNT)));
                cardMap.get(rs.getInt(Fields.CARD_ID)).setClient(
                        clientMap.get(rs.getInt(Fields.CARD_ID_CLIENT)));
            }
            cardMap.forEach((k,v)->{
                v.getClient().addCardToList(v);
            });
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return new ArrayList<>(clientMap.values());
    }

    @Override
    public Client parseResult(ResultSet rs) throws DaoException {
        try {
            return new Client(rs.getInt(Fields.CL_ID),rs.getString(Fields.CL_FNAME),
                    rs.getString(Fields.CL_NAME),rs.getString(Fields.CL_EMAIL));
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

package payments.model.dao.impl;

import payments.model.dao.ClientDao;
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
import java.util.*;

/**
 * Dao object for {@link Client}
 * @author kara.vladimir2@gmail.com.
 */
public class ClientDaoImpl extends AbstractDaoImpl implements ClientDao {
    private static final Logger LOG = Logger.getLogger(ClientDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO client("+Fields.CL_FNAME+
            ","+Fields.CL_NAME+","+Fields.CL_EMAIL+") VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM client " +
            "LEFT JOIN creditCard ON("+Fields.CL_ID+"="+Fields.CARD_ID_CLIENT +")" +
            "left join account ON("+Fields.CARD_ID_ACCOUNT+"="+Fields.ACC_ID+")";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+" WHERE "+Fields.CL_ID+" = ?;";
    public static final String QUERY_FIND_BY_EMAIL = QUERY_SELECT_ALL +" WHERE " + Fields.CL_EMAIL + "=?";
    public static final String QUERY_UPDATE = "UPDATE client SET "+Fields.CL_FNAME+"= ?, "+Fields.CL_NAME+" = ?, "
            +Fields.CL_EMAIL+" = ? WHERE "+Fields.CL_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM client WHERE "+Fields.CL_ID+" = ?;";

    public ClientDaoImpl(Connection connection) {
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

        Map<Number, Client> clientMap = new HashMap<>();
        Map<Number, CreditCard> cardMap = new HashMap<>();
        Map<Number, Account> accountMap = new HashMap<>();

        CreditCardDaoImpl creditCardDao = new CreditCardDaoImpl(connection);
        try {
            while (rs.next()) {
                clientMap.put(rs.getInt(Fields.CL_ID),parseResult(rs));
                cardMap.put(rs.getInt(Fields.CARD_ID),CreditCardDaoImpl.parseResult(rs));
                accountMap.put(rs.getInt(Fields.ACC_ID),AccountDaoImpl.parseResult(rs));
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

    public static Client parseResult(String alias,ResultSet rs) throws DaoException {
        alias = (alias.isEmpty())?alias:alias + ".";
        try {
            return new Client(rs.getInt(alias+Fields.CL_ID),rs.getString(alias+Fields.CL_FNAME),
                    rs.getString(alias+Fields.CL_NAME),rs.getString(alias+Fields.CL_EMAIL));
        } catch (SQLException e) {
            throw new DaoException(LOG,ERR_PARSING, e);
        }
    }

    public static Client parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public Client findClientByEmail(String email) throws DaoException {
        Client client = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_EMAIL)){
            client = ((List<Client>)parseResultSet(preparedStatement.executeQuery())).get(0);
        } catch (SQLException e) {
            throw new DaoException(getLogger(), ERR_FIND_BY_EMAIL,e);
        }
        return client;
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

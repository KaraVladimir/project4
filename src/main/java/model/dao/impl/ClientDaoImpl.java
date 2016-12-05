package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Client;

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
    public static final String QUERY_SAVE = "INSERT INTO client("+Fields.CL_FNAME+
            ","+Fields.CL_NAME+","+Fields.CL_EMAIL+") VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM client " +
            "LEFT JOIN creditCard ON("+Fields.CL_ID+"="+Fields.CARD_ID_CLIENT +")" +
            "left join account ON("+Fields.CARD_ID_ACCOUNT+"="+Fields.ACC_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+" WHERE "+Fields.CL_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+" WHERE "+Fields.CL_ID+" = ?;";
    public static final String QUERY_FIND_BY_EMAIL = QUERY_SELECT_ALL +" WHERE " + Fields.CL_EMAIL + "=?";
    public static final String QUERY_UPDATE = "UPDATE client SET "+Fields.CL_FNAME+"= ? "+Fields.CL_NAME+" = ? "
            +Fields.CL_EMAIL+" = ?";
    public static final String QUERY_DELETE = "DELETE * FROM client WHERE "+Fields.CL_ID+" = ?;";

    public ClientDaoImpl(Connection connection) {
        super(connection);
    }

    public List<Identified> parseResultSetGen(ResultSet resultSet) {
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
            (PreparedStatement preparedStatement, Identified identified) {
        Client client = (Client) identified;
        try {
            preparedStatement.setString(1, client.getFamilyName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getEmail());
        } catch (SQLException e) {
            throw new DaoException(ERR_UPDATE,e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave
            (PreparedStatement preparedStatement, Identified identified) {
        Client client = (Client) identified;
        try {
            preparedStatement.setString(1, client.getFamilyName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setInt(4, client.getID());
        } catch (SQLException e) {
            throw new DaoException(ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<? super Client> parseResultSet(ResultSet rs) {
        List<Client> clients = new ArrayList<>();
        try {
            while (rs.next()) {
                clients.add(parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
        return clients;
    }

    @Override
    public Client parseResult(ResultSet rs) {
        try {
            return new Client(rs.getInt(Fields.CL_ID),rs.getString(Fields.CL_FNAME),
                    rs.getString(Fields.CL_NAME),rs.getString(Fields.CL_EMAIL));
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
    }

}

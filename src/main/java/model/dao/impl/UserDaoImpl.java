package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Client;
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
 * Dao object for {@link User}
 * @author kara.vladimir2@gmail.com.
 */
public class UserDaoImpl extends AbstractDaoImpl implements HaveUniqueField{
    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO user("+ Fields.USR_LOGIN+
            ","+Fields.USR_PASS+","+Fields.USR_IS_ADMIN+","+Fields.USR_ID_CLIENT+")" + " VALUES(?,?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM user " +
            "LEFT JOIN client ON("+Fields.USR_ID_CLIENT+"="+Fields.CL_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+"WHERE "+ Fields.USR_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.USR_ID+" = ?;";
    public static final String QUERY_FIND_BY_LOGIN = QUERY_SELECT_ALL+"WHERE "+Fields.USR_LOGIN+" = ?;";

    public static final String QUERY_UPDATE = "UPDATE account SET " +Fields.ACC_NUMBER+"= ? "
            +Fields.ACC_BALANCE+" = ? "+Fields.ACC_IS_BLOCKED+" = ? WHERE "+Fields.ACC_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM account WHERE "+Fields.ACC_ID+" = ?;";

    public UserDaoImpl(Connection connection) {
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
    public PreparedStatement prepareStatementForUpdate
            (PreparedStatement preparedStatement, Identified identified) throws DaoException {
        User user = (User) identified;
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3,user.isAdmin());
            preparedStatement.setInt(4,user.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_UPDATE, e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        User user = (User) identified;
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3,user.isAdmin());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SAVE, e);
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
        return QUERY_FIND_BY_LOGIN;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<? super User> parseResultSet(ResultSet rs) throws DaoException {
        ClientDaoImpl clientDao = new ClientDaoImpl();
        Map<Number, User> userMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        try {
            while (rs.next()) {
                userMap.put(rs.getInt(Fields.USR_ID), parseResult(rs));
                clientMap.put(rs.getInt(Fields.CL_ID), clientDao.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                userMap.get(rs.getInt(Fields.USR_ID)).setClient(
                        clientMap.get(rs.getInt(Fields.USR_ID_CLIENT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User parseResult(ResultSet rs) throws DaoException {
        try {
            return new User(rs.getInt(Fields.USR_ID),rs.getString(Fields.USR_LOGIN),
                    rs.getString(Fields.USR_PASS),rs.getBoolean(Fields.USR_IS_ADMIN));
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

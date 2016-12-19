package payments.model.dao.impl;

import payments.helper.Msgs;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import payments.model.entities.Client;
import payments.model.entities.User;
import org.apache.log4j.Logger;
import payments.model.dao.UserDao;

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
 * Dao object for {@link User}
 *
 * @author kara.vladimir2@gmail.com.
 */
public class UserDaoImpl extends AbstractDaoImpl implements UserDao {
    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class);

    private static final String QUERY_SAVE = "INSERT INTO user(" + USR_LOGIN +
            "," + USR_PASS + "," + USR_IS_ADMIN + "," + USR_ID_CLIENT + ")" + " VALUES(?,?,?,?);";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM user " +
            "LEFT JOIN client ON(" + USR_ID_CLIENT + "=" + CL_ID + ")";
    private static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL + "WHERE " + USR_ID + " = ?;";
    private static final String QUERY_FIND_BY_LOGIN = QUERY_SELECT_ALL + "WHERE " + USR_LOGIN + " = ?;";

    private static final String QUERY_UPDATE = "UPDATE account SET " + ACC_NUMBER + "= ? "
            + ACC_BALANCE + " = ? " + ACC_IS_BLOCKED + " = ? WHERE " + ACC_ID + "= ?;";
    private static final String QUERY_DELETE = "DELETE * FROM account WHERE " + ACC_ID + " = ?;";

    public UserDaoImpl(Connection connection) {
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
        User user = (User) identified;
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, user.isAdmin());
            preparedStatement.setInt(4, user.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_UPDATE, e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) throws DaoException {
        User user = (User) identified;
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, user.isAdmin());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SAVE_QUERY, e);
        }
        return preparedStatement;
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws DaoException {
        Map<Number, User> userMap = new HashMap<>();
        Map<Number, Client> clientMap = new HashMap<>();
        try {
            while (rs.next()) {
                userMap.put(rs.getInt(USR_ID), parseResult(rs));
                clientMap.put(rs.getInt(CL_ID), ClientDaoImpl.parseResult(rs));
            }
            rs.beforeFirst();
            while (rs.next()) {
                userMap.get(rs.getInt(USR_ID)).setClient(
                        clientMap.get(rs.getInt(USR_ID_CLIENT)));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_PARSING, e);
        }
        return new ArrayList<>(userMap.values());
    }

    private static User parseResult(String alias, ResultSet rs) throws DaoException {
        alias = (alias.isEmpty()) ? alias : alias + ".";
        try {
            return new User(rs.getInt(alias + USR_ID), rs.getString(alias + USR_LOGIN),
                    rs.getString(alias + USR_PASS), rs.getBoolean(alias + USR_IS_ADMIN));
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ERR_PARSING, e);
        }
    }

    private static User parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public User findUserByLogin(String login) throws DaoException {
        User user;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = ((List<User>) parseResultSet(resultSet)).get(0);

        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_FIND_BY_LOGIN, e);
        }
        return user;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}

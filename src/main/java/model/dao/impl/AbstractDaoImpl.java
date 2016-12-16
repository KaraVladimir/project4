package model.dao.impl;

import model.dao.GenericDao;
import model.dao.Identified;
import model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import web.commands.impl.AbstractCommand;

import java.sql.*;
import java.util.List;

/**
 * Standard CRUD functionality
 * @author kara.vladimir2@gmail.com.
 */
public abstract class AbstractDaoImpl<T extends Identified<PK>,PK extends Number> implements GenericDao{
    private static final Logger LOG = Logger.getLogger(AbstractDaoImpl.class);

    protected Connection connection;

    public static final String ERR_SAVE_QUERY = "save failed";
    public static final String ERR_GET_BY_PK_QUERY = "get by PK failed";
    public static final String ERR_UPDATE_QUERY = "update failed";
    public static final String ERR_DELETE_QUERY = "delete failed";
    public static final String ERR_SELECT_ALL_QUERY = "select all failed";

    public static final String ERR_PARSING = "parsing failed";
    public static final String ERR_UPDATE = "update failed";
    public static final String ERR_SAVE = "save failed";

    public static final String ERR_GET_BY_ACCOUNT_QUERY = "find credit card by account failed";
    public static final String ERR_GET_BLOCKED_QUERY = "find blocked accounts failed";
    public static final String ERR_GET_PAYMENTS_BY_USER = "Get payments by user failed";
    public static final String ERR_FIND_BY_NUMBER = "Find by account number failed";
    public static final String ERR_FIND_BY_EMAIL = "Find client by email failed";
    public static final String ERR_FIND_BY_LOGIN = "Find user by login failed";


    public AbstractDaoImpl() {}

    public AbstractDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public abstract String getSaveQuery();

    public abstract String getByPKQuery();

    public abstract String getUpdateQuery();

    public abstract String getDeleteQuery();

    public abstract String getSelectAllQuery();

    public abstract PreparedStatement prepareStatementForUpdate
            (PreparedStatement preparedStatement, Identified identified) throws DaoException;

    public abstract PreparedStatement prepareStatementForSave
            (PreparedStatement preparedStatement, Identified identified) throws DaoException;


    public abstract List<T> parseResultSet(ResultSet rs) throws DaoException;

    public Identified<PK> save(Identified identified) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSaveQuery(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement = prepareStatementForSave(preparedStatement, identified);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                identified.setID(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SAVE_QUERY,e);
        }
        return identified;
    }

    public Identified read(Number prKey) throws DaoException {
        List<T> tList;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getByPKQuery());
            preparedStatement.setInt(1, (Integer) prKey);
            tList = parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_GET_BY_PK_QUERY,e);
        }
        if (tList == null || tList.size() == 0||tList.size()>1) {
            throw new DaoException(getLogger(),ERR_GET_BY_PK_QUERY);
        }
        return tList.get(0);
    }

    public void update(Identified identified) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery());
            preparedStatement = prepareStatementForUpdate(preparedStatement,identified);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_UPDATE_QUERY,e);
        }
    }

    public void delete(Identified identified) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getDeleteQuery());
            if (identified.getID() == null) {
                throw new DaoException(getLogger(),ERR_DELETE_QUERY);
            }
            preparedStatement.setInt(1, (Integer) identified.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_DELETE_QUERY, e);
        }
    }

    public List<T> readAll() throws DaoException {
        List<T> tList;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSelectAllQuery());
            tList = parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SELECT_ALL_QUERY, e);
        }
        return tList;
    }

    public abstract Logger getLogger();
}

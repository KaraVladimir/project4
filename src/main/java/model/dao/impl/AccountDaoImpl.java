package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.Account;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao object for {@link Account}
 * @author kara.vladimir2@gmail.com.
 */
public class AccountDaoImpl extends AbstractDaoImpl implements HaveUniqueField {
    private static final Logger LOG = Logger.getLogger(AccountDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO account("+ Fields.ACC_NUMBER+
            ","+Fields.ACC_BALANCE+","+Fields.ACC_IS_BLOCKED+")" + " VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM account ";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+"WHERE "+ Fields.ACC_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_ID+" = ?;";
    public static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_NUMBER+" = ?;";
    public static final String QUERY_FIND_BLOCKED = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_IS_BLOCKED+" = true;";
    public static final String QUERY_UPDATE = "UPDATE account SET " +Fields.ACC_NUMBER+"= ?, "
            +Fields.ACC_BALANCE+" = ?, "+Fields.ACC_IS_BLOCKED+" = ? WHERE "+Fields.ACC_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM account WHERE "+Fields.ACC_ID+" = ?;";


    public AccountDaoImpl() {}

    public AccountDaoImpl(Connection connection) {
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
        Account account = (Account) identified;
        try {
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setBigDecimal(2, account.getAccountBalance());
            preparedStatement.setBoolean(3,account.isBlocked());
            preparedStatement.setInt(4,account.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_UPDATE, e);
        }
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave
            (PreparedStatement preparedStatement, Identified identified) throws DaoException {
        Account account = (Account) identified;
        try {
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setBigDecimal(2, account.getAccountBalance());
            preparedStatement.setBoolean(3,account.isBlocked());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_SAVE, e);
        }
        return preparedStatement;
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
    public List<Identified> parseResultSetGen(ResultSet resultSet) throws DaoException {
        List<Identified> accounts = (List<Identified>) parseResultSet(resultSet);
        return accounts;

    }

    @Override
    public List<? super Account> parseResultSet(ResultSet rs) throws DaoException {
        List<Account> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                listResult.add(parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return listResult;
    }

    @Override
    public Account parseResult(ResultSet rs) throws DaoException {
        try {
            return new Account(rs.getInt(Fields.ACC_ID),rs.getString(Fields.ACC_NUMBER),
                    rs.getBigDecimal(Fields.ACC_BALANCE),rs.getBoolean(Fields.ACC_IS_BLOCKED));
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
    }

    public List<Account> findBlockedAccounts() throws DaoException {
        List<Account> accounts;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BLOCKED);
            accounts = (List<Account>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_GET_BLOCKED_QUERY,e);
        }
        return accounts;
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

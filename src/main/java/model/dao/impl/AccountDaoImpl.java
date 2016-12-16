package model.dao.impl;

import model.dao.AccountDao;
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
public class AccountDaoImpl extends AbstractDaoImpl implements AccountDao {
    private static final Logger LOG = Logger.getLogger(AccountDaoImpl.class);

    public static final String QUERY_SAVE = "INSERT INTO account("+ Fields.ACC_NUMBER+
            ","+Fields.ACC_BALANCE+","+Fields.ACC_IS_BLOCKED+")" + " VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM account ";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_ID+" = ?;";
    public static final String QUERY_FIND_BY_PK_FOR_UPDATE = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_ID+" = ? FOR UPDATE;";
    public static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_NUMBER+" = ?;";
    public static final String QUERY_FIND_BLOCKED = QUERY_SELECT_ALL+"WHERE "+Fields.ACC_IS_BLOCKED+" = true;";
    public static final String QUERY_UPDATE = "UPDATE account SET " +Fields.ACC_NUMBER+"= ?, "
            +Fields.ACC_BALANCE+" = ?, "+Fields.ACC_IS_BLOCKED+" = ? WHERE "+Fields.ACC_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM account WHERE "+Fields.ACC_ID+" = ?;";



    public AccountDaoImpl(Connection connection) {
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
    public List<? super Account> parseResultSet(ResultSet rs) throws DaoException {
        List<Account> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                listResult.add(AccountDaoImpl.parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_PARSING, e);
        }
        return listResult;
    }

    public static Account parseResult(String alias,ResultSet rs) throws DaoException {
        alias = (alias.isEmpty())?alias:alias + ".";
        try {
            return new Account(rs.getInt(alias+Fields.ACC_ID),rs.getString(alias+Fields.ACC_NUMBER),
                    rs.getBigDecimal(alias+Fields.ACC_BALANCE),rs.getBoolean(alias+Fields.ACC_IS_BLOCKED));
        } catch (SQLException e) {
            throw new DaoException(LOG,ERR_PARSING, e);
        }
    }
    public static Account parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
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
    public Account findAccountByNumber(String number) throws DaoException {
        Account account = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_NUMBER);
            preparedStatement.setString(1,number);
            account = ((List<Account>)parseResultSet(preparedStatement.executeQuery())).get(0);

        } catch (SQLException e) {
            throw new DaoException(getLogger(), ERR_FIND_BY_NUMBER,e);
        }
        return account;
    }

    @Override
    public Account findByPKForUpdate(Number prKey) throws DaoException {
        List<Account> tList;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_PK_FOR_UPDATE);
            preparedStatement.setInt(1, (Integer) prKey);
            tList = (List<Account>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(),ERR_GET_BY_PK_QUERY,e);
        }
        if (tList == null || tList.size() == 0||tList.size()>1) {
            throw new DaoException(getLogger(),ERR_GET_BY_PK_QUERY);
        }
        return tList.get(0);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}

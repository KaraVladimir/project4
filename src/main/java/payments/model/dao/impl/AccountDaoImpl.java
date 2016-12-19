package payments.model.dao.impl;

import org.apache.log4j.Logger;
import payments.helper.Msgs;
import payments.model.dao.AccountDao;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import payments.model.entities.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static payments.model.dao.impl.Fields.*;

/**
 * Dao object for {@link Account}
 *
 * @author kara.vladimir2@gmail.com.
 */
public class AccountDaoImpl extends AbstractDaoImpl implements AccountDao {
    private static final Logger LOG = Logger.getLogger(AccountDaoImpl.class);

    private static final String QUERY_SAVE = "INSERT INTO account(" + ACC_NUMBER +
            "," + ACC_BALANCE + "," + ACC_IS_BLOCKED + ")" + " VALUES(?,?,?);";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM account ";
    private static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL + "WHERE " + ACC_ID + " = ?;";
    private static final String QUERY_FIND_BY_PK_FOR_UPDATE = QUERY_SELECT_ALL + "WHERE " + ACC_ID + " = ? FOR UPDATE;";
    private static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL + "WHERE " + ACC_NUMBER + " = ?;";
    private static final String QUERY_FIND_BLOCKED = QUERY_SELECT_ALL + "WHERE " + ACC_IS_BLOCKED + " = true;";
    private static final String QUERY_UPDATE = "UPDATE account SET " + ACC_NUMBER + "= ?, "
            + ACC_BALANCE + " = ?, " + ACC_IS_BLOCKED + " = ? WHERE " + ACC_ID + "= ?;";
    private static final String QUERY_DELETE = "DELETE * FROM account WHERE " + ACC_ID + " = ?;";


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
            preparedStatement.setBoolean(3, account.isBlocked());
            preparedStatement.setInt(4, account.getID());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_UPDATE, e);
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
            preparedStatement.setBoolean(3, account.isBlocked());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SAVE_QUERY, e);
        }
        return preparedStatement;
    }

    @Override
    protected List<Account> parseResultSet(ResultSet rs) throws DaoException {
        List<Account> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                listResult.add(AccountDaoImpl.parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_PARSING, e);
        }
        return listResult;
    }

    public static Account parseResult(String alias, ResultSet rs) throws DaoException {
        alias = (alias.isEmpty()) ? alias : alias + ".";
        try {
            return new Account(rs.getInt(alias + ACC_ID), rs.getString(alias + ACC_NUMBER),
                    rs.getBigDecimal(alias + ACC_BALANCE), rs.getBoolean(alias + ACC_IS_BLOCKED));
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ERR_PARSING, e);
        }
    }

    public static Account parseResult(ResultSet rs) throws DaoException {
        return parseResult("", rs);
    }

    @Override
    public List<Account> findBlockedAccounts() throws DaoException {
        List<Account> accounts;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BLOCKED)) {
            accounts = (List<Account>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BLOCKED_QUERY, e);
        }
        return accounts;
    }

    @Override
    public Account findAccountByNumber(String number) throws DaoException {
        Account account;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_NUMBER)) {
            preparedStatement.setString(1, number);
            List<Account> accounts = parseResultSet(preparedStatement.executeQuery());
            account = (accounts.size()>0)?accounts.get(0):null;
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_FIND_BY_NUMBER, e);
        }
        return account;
    }

    @Override
    public Account findByPKForUpdate(Number prKey) throws DaoException {
        List<Account> tList;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_BY_PK_FOR_UPDATE)) {
            preparedStatement.setInt(1, (Integer) prKey);
            tList = (List<Account>) parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_PK_QUERY, e);
        }
        if (tList == null || tList.size() == 0 || tList.size() > 1) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_PK_QUERY);
        }
        return tList.get(0);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}

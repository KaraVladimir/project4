package model.dao;

import model.dao.exception.DaoException;
import model.entities.Account;

import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface AccountDao extends GenericDao{
    List<Account> findBlockedAccounts() throws DaoException;

    Account findAccountByNumber(String number) throws DaoException;

    Account findByPKForUpdate(Number prKey) throws DaoException;
}

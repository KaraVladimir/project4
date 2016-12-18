package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.entities.Account;

import java.util.List;

/**
 * interface for dao object {@link Account}
 * @author kara.vladimir2@gmail.com.
 */
public interface AccountDao extends GenericDao{
    List<Account> findBlockedAccounts() throws DaoException;

    Account findAccountByNumber(String number) throws DaoException;

    Account findByPKForUpdate(Number prKey) throws DaoException;
}

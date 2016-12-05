package service;

import model.dao.AccountDao;
import model.dao.impl.AccountDaoImpl;
import model.dao.impl.DaoFactory;
import model.dao.impl.DaoManager;
import model.entities.Account;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class AccountService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserService.class);

    public Account findAccount(String number) {
        Account account = null;
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()){
            AccountDaoImpl accountDao = (AccountDaoImpl) daoManager.getDao(Account.class);
            account = (Account) accountDao.findByUniqueField(number);
        }
        return account;
    }
}

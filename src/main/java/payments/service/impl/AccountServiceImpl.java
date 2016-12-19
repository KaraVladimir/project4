package payments.service.impl;

import payments.exception.AppException;
import payments.model.dao.*;
import payments.model.dao.impl.DaoFactoryImpl;
import payments.model.entities.Account;
import payments.model.entities.Payment;
import payments.model.entities.TypeOfPayment;
import payments.service.AccountService;
import payments.service.exception.ServiceException;
import payments.helper.Msgs;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for commands bundles with accounts
 * @author kara.vladimir2@gmail.com.
 */
public class AccountServiceImpl implements payments.service.AccountService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserServiceImpl.class);

    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class Holder {
        private static final AccountService instance = new AccountServiceImpl();
    }

    public static AccountService getInstance() {
        return Holder.instance;
    }

    @Override
    public Account findAccountByNumber(String number) throws AppException {
        Account account;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            account = accountDao.findAccountByNumber(number);
            checkAccountIsNull(account);
        }
        return account;
    }

    @Override
    public Account unblockAccountByID(Integer id) throws AppException {
        Account account;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            daoManager.beginTransaction();
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            account = (Account) accountDao.read(id);
            checkAccountIsNull(account);
            checkAccountIsNotBlocked(account);
            account.setBlocked(false);
            accountDao.update(account);
            daoManager.commitTransaction();
        }
        return account;
    }


    @Override
    public Account block(Integer accId) throws AppException {
        Account account;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            daoManager.beginTransaction();
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            account = (Account) accountDao.read(accId);
            checkAccountIsNull(account);
            checkAccountIsBlocked(account);
            account.setBlocked(true);
            accountDao.update(account);
            daoManager.commitTransaction();
        }
        return account;
    }


    @Override
    public Payment pay(Integer accS_id, String number, BigDecimal amount) throws AppException {
        Payment payment;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            PaymentDao paymentDao = (PaymentDao) daoManager.getDao(Payment.class);

            daoManager.beginTransaction();
            Account accountSender = accountDao.findByPKForUpdate(accS_id);
            Account accountRecipient = accountDao.findAccountByNumber(number);


            AccountServiceImpl.this.checkAccountIsNull(accountSender);
            AccountServiceImpl.this.checkAccountIsNull(accountRecipient);

            accountRecipient = accountDao.findByPKForUpdate(accountRecipient.getID());

            AccountServiceImpl.this.checkBalance(amount, accountSender);
            AccountServiceImpl.this.checkAccountIsBlocked(accountSender);

            payment = new Payment(amount, TypeOfPayment.PAYMENT, accountSender, accountRecipient);
            payment = (Payment) paymentDao.save(payment);
            accountRecipient.addBalance(amount);
            accountDao.update(accountRecipient);
            accountSender.deductBalance(amount);
            accountDao.update(accountSender);
            daoManager.commitTransaction();
        }
        return payment;
    }

    @Override
    public Payment refill(Integer accS_id, BigDecimal amount) throws AppException {
        Payment payment;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            PaymentDao paymentDao = (PaymentDao) daoManager.getDao(Payment.class);
            daoManager.beginTransaction();
            Account account = accountDao.findByPKForUpdate(accS_id);
            payment = new Payment(amount, TypeOfPayment.REFILL, null, account);
            payment = (Payment) paymentDao.save(payment);
            account.addBalance(amount);
            accountDao.update(account);
            daoManager.commitTransaction();
        }
        return payment;
    }

    @Override
    public List<Account> findBlocked() throws AppException {
        List<Account> accounts;
        try (DaoManager daoManager = daoFactory.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            accounts = accountDao.findBlockedAccounts();
        }
        return accounts;
    }

    private void checkAccountIsNull(Account account1) throws ServiceException {
        if (account1 == null) {
            throw new ServiceException(LOG, Msgs.ACCOUNT_NULL);
        }
    }

    private void checkAccountIsNotBlocked(Account account1) throws ServiceException {
        if (!account1.isBlocked()) {
            throw new ServiceException(LOG, Msgs.ACCOUNT_NOT_BLOCKED);
        }
    }

    private void checkAccountIsBlocked(Account acc) throws ServiceException {
        if (acc.isBlocked()) {
            throw new ServiceException(LOG, Msgs.ACCOUNT_IS_BLOCKED);
        }
    }

    private void checkBalance(BigDecimal amount, Account accountSender) throws ServiceException {
        if (accountSender.getAccountBalance().compareTo(amount) < 0) {
            throw new ServiceException(LOG, Msgs.NOT_ENOUGH_MONEY);
        }
    }

}

package service.impl;

import exception.AppException;
import model.dao.AccountDao;
import model.dao.CreditCardDao;
import model.dao.DaoManager;
import model.dao.PaymentDao;
import model.dao.impl.DaoFactory;
import model.entities.Account;
import model.entities.CreditCard;
import model.entities.Payment;
import model.entities.TypeOfPayment;
import service.AccountService;
import service.exception.ServiceException;
import web.config.Msgs;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class AccountServiceImpl implements service.AccountService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserServiceImpl.class);


    private static class Holder {
        private static final AccountService instance = new AccountServiceImpl();
    }

    public static AccountService getInstance() {
        return Holder.instance;
    }

    @Override
    public Account findAccountByNumber(String number) throws AppException {
        Account account = null;
        try (DaoManager daoManager =  DaoFactory.INSTANCE.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            account = (Account) accountDao.findAccountByNumber(number);
            checkAccountIsNull(account);
        }
        return account;
    }

    @Override
    public Account unblockAccountByID(Integer id) throws AppException {
        Account account = null;
        try (DaoManager daoManager = DaoFactory.INSTANCE.getDaoManager()) {
            account = (Account) daoManager.transaction(daoManager1 -> {
                AccountDao accountDao = (AccountDao) daoManager1.getDao(Account.class);
                Account account1 = (Account) accountDao.read(id);
                checkAccountIsNull(account1);
                checkAccountIsNotBlocked(account1);
                account1.setBlocked(false);
                accountDao.update(account1);
                return account1;
            });

        }
        return account;
    }



    @Override
    public Account block(Integer accId) throws AppException {
        Account account = null;

        try (DaoManager daoManager =  DaoFactory.INSTANCE.getDaoManager()) {
            account = (Account) daoManager.transaction(daoManager1 -> {
                AccountDao accountDao = (AccountDao) daoManager1.getDao(Account.class);
                Account acc = (Account) accountDao.read(accId);
                checkAccountIsNull(acc);
                checkAccountIsBlocked(acc);
                acc.setBlocked(true);
                accountDao.update(acc);
                return acc;
            });
        }
        return account;
    }



    @Override
    public Payment pay(Integer accS_id, String number, BigDecimal amount) throws AppException {
        Payment payment = null;
        try (DaoManager daoManager = DaoFactory.INSTANCE.getDaoManager()) {
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            PaymentDao paymentDao = (PaymentDao) daoManager.getDao(Payment.class);
            CreditCardDao creditCardDao = (CreditCardDao) daoManager.getDao(CreditCard.class);

            payment = (Payment) daoManager.transaction(daoManager1 -> {
                Payment payment1 = null;
                Account accountSender = (Account) accountDao.findByPKForUpdate(accS_id);
                Account accountRecipient = accountDao.findAccountByNumber(number);
                accountRecipient = accountDao.findByPKForUpdate(accountRecipient.getID());

                checkAccountIsNull(accountSender);
                checkAccountIsNull(accountRecipient);

                checkBalance(amount, accountSender);
                checkAccountIsBlocked(accountSender);

                CreditCard creditCardSender = creditCardDao.findCreditCardByAccount(accountSender);
                payment1 = new Payment(amount, TypeOfPayment.PAYMENT,creditCardSender.getClient(),
                        accountSender, creditCardSender, accountRecipient);
                payment1 = (Payment) paymentDao.save(payment1);
                accountRecipient.addBalance(amount);
                accountDao.update(accountRecipient);
                accountSender.deductBalance(amount);
                accountDao.update(accountSender);
                return payment1;
            });
        }
        return payment;
    }


    @Override
    public Payment refill(Integer accS_id, BigDecimal amount) throws AppException {
        Payment payment = null;
        try (DaoManager daoManager = DaoFactory.INSTANCE.getDaoManager()){
            AccountDao accountDao = (AccountDao) daoManager.getDao(Account.class);
            PaymentDao paymentDao = (PaymentDao) daoManager.getDao(Payment.class);

            payment = (Payment) daoManager.transaction(daoManager1 -> {
                Payment payment1 = null;
                Account account = (Account) accountDao.findByPKForUpdate(accS_id);
                payment1 = new Payment(amount, TypeOfPayment.REFILL,null,
                        null, null, account);
                payment1 = (Payment) paymentDao.save(payment1);
                account.addBalance(amount);
                accountDao.update(account);
                return payment1;
            });

        }
        return payment;
    }

    @Override
    public List<Account> findBlocked() throws AppException {
        List<Account> accounts = null;
        try (DaoManager daoManager = DaoFactory.INSTANCE.getDaoManager()){
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

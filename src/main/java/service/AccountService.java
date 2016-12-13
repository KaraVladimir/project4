package service;

import exception.AppException;
import model.dao.exception.DaoException;
import model.dao.impl.*;
import model.entities.Account;
import model.entities.CreditCard;
import model.entities.Payment;
import model.entities.TypeOfPayment;
import service.exception.ServiceException;
import web.config.Msgs;

import java.math.BigDecimal;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum AccountService {
    INSTANCE;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserService.class);

    public Account findAccountByNumber(String number) throws AppException {
        Account account = null;
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()) {
            AccountDaoImpl accountDao = (AccountDaoImpl) daoManager.getDao(Account.class);
            account = (Account) accountDao.findByUniqueField(number);
            checkAccountIsNull(account);
        }
        return account;
    }

    public Account unblockAccountByNumber(final String number) throws AppException {
        Account account = null;
        if (number == null) return account;
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()) {
            account = (Account) daoManager.transaction(daoManager1 -> {
                AccountDaoImpl accountDao = (AccountDaoImpl) daoManager1.getDao(Account.class);
                Account account1 = (Account) accountDao.read(Integer.valueOf(number));
                checkAccountIsNull(account1);
                checkAccountIsNotBlocked(account1);
                account1.setBlocked(false);
                accountDao.update(account1);
                return account1;
            });

        }
        return account;
    }



    public Account block(Integer accId) throws AppException {
        Account account = null;

        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()) {
            account = (Account) daoManager.transaction(daoManager1 -> {
                AccountDaoImpl accountDao = (AccountDaoImpl) daoManager1.getDao(Account.class);
                Account acc = (Account) accountDao.read(accId);
                checkAccountIsNull(acc);
                checkAccountIsBlocked(acc);
                acc.setBlocked(true);
                accountDao.update(acc);
                return null;
            });
        }
        return account;
    }



    public void pay(Integer accS_id, Account accountRecipient, BigDecimal amount) throws AppException {
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()) {
            AccountDaoImpl accountDao = (AccountDaoImpl) daoManager.getDao(Account.class);
            PaymentDaoImpl paymentDao = (PaymentDaoImpl) daoManager.getDao(Payment.class);
            CreditCardDaoImpl creditCardDao = (CreditCardDaoImpl) daoManager.getDao(CreditCard.class);

            Payment payment = (Payment) daoManager.transaction(daoManager1 -> {
                Payment payment1 = null;
                Account accountSender = (Account) accountDao.read(accS_id);
                checkBalance(amount, accountSender);
                checkAccountIsBlocked(accountSender);
                CreditCard creditCardSender = (CreditCard) creditCardDao.
                        findCreditCardByAccount(accountSender);
                accountSender.deductBalance(amount);
                accountDao.update(accountSender);
                accountRecipient.addBalance(amount);
                accountDao.update(accountRecipient);
                payment1 = new Payment(amount, TypeOfPayment.PAYMENT,creditCardSender.getClient(),
                        accountSender, creditCardSender, accountRecipient);
                payment1 = (Payment) paymentDao.save(payment1);
                return payment1;
            });
        }
    }


    public void refill(Integer accS_id, BigDecimal amount) throws AppException {
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()){
            AccountDaoImpl accountDao = (AccountDaoImpl) daoManager.getDao(Account.class);
            PaymentDaoImpl paymentDao = (PaymentDaoImpl) daoManager.getDao(Payment.class);

            Payment payment = (Payment) daoManager.transaction(daoManager1 -> {
                Payment payment1 = null;
                Account account = (Account) accountDao.read(accS_id);
                payment1 = new Payment(amount, TypeOfPayment.REFILL,null,
                        null, null, account);
                account.addBalance(amount);
                accountDao.update(account);
                payment1 = (Payment) paymentDao.save(payment1);
                return payment1;
            });

        }
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

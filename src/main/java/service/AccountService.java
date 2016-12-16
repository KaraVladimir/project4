package service;

import exception.AppException;
import model.entities.Account;
import model.entities.Payment;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface AccountService {
    Account findAccountByNumber(String number) throws AppException;

    Account unblockAccountByID(Integer id) throws AppException;

    Account block(Integer accId) throws AppException;

    Payment pay(Integer accS_id, String number, BigDecimal amount) throws AppException;

    Payment refill(Integer accS_id, BigDecimal amount) throws AppException;

    List<Account> findBlocked() throws AppException;
}

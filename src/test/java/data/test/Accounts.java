package data.test;

import payments.model.entities.Account;

import java.math.BigDecimal;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum  Accounts {
    ACC1(1,"1234",new BigDecimal("1234.50"),false),
    ACC2(2,"2234",new BigDecimal("700"),false),
    ACC3(3,"7894",new BigDecimal("500"),true),
    ACC4(4,"75324",new BigDecimal("900"),false);

    public Account account;

    Accounts(Integer id, String number, BigDecimal balance, boolean isBlocked) {
        account = new Account(id, number, balance, isBlocked);
    }
}

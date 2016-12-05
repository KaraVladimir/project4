package model.entities;

import model.dao.Identified;

import java.math.BigDecimal;

/**
 * Represents bank account
 * Created by Kara_Vladimir.
 */
public class Account implements Identified<Integer>{
    private Integer id;
    private String accountNumber;
    private BigDecimal accountBalance;
    private boolean isBlocked;

    public Account(String accountNumber, BigDecimal accountBalance, boolean isBlocked) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.isBlocked = isBlocked;
    }

    public Account(Integer id, String accountNumber, BigDecimal accountBalance, boolean isBlocked) {
        this(accountNumber, accountBalance, isBlocked);
        this.id = id;
    }


    public Integer getID() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}

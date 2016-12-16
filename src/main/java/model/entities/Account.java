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

    @Override
    public void setID(Integer integer) {
        this.id = integer;
    }
    @Override
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

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void addBalance(BigDecimal amount) {
        accountBalance = accountBalance.add(amount);
    }
    public void deductBalance(BigDecimal amount) {
        accountBalance = accountBalance.subtract(amount);
    }
}

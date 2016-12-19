package payments.model.entities;

import payments.model.dao.Identified;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents payment
 * Created by Kara_Vladimir.
 */
public class Payment implements Identified<Integer> {
    private Integer id;
    private Timestamp timestamp;
    private BigDecimal amount;
    private TypeOfPayment typeOfPayment;
    private Account senderAccount;
    private Account recipientAccount;

    public Payment(BigDecimal amount, TypeOfPayment typeOfPayment, Account senderAccount, Account recipientAccount) {
        this.amount = amount;
        this.typeOfPayment = typeOfPayment;
        this.senderAccount = senderAccount;
        this.recipientAccount = recipientAccount;
    }

    public Payment(Timestamp timestamp, BigDecimal amount, TypeOfPayment typeOfPayment) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.typeOfPayment = typeOfPayment;
    }

    public Payment(Integer id, Timestamp timestamp, BigDecimal amount, TypeOfPayment typeOfPayment) {
        this.id = id;
        this.timestamp = timestamp;
        this.amount = amount;
        this.typeOfPayment = typeOfPayment;
    }

    @Override
    public void setID(Integer integer) {
        this.id = integer;
    }

    @Override
    public Integer getID() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TypeOfPayment getTypeOfPayment() {
        return typeOfPayment;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }
}

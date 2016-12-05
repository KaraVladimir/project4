package model.entities;

import model.dao.Identified;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents payment
 * Created by Kara_Vladimir.
 */
public class Payment implements Identified<Integer>{
    private Integer id;
    private Timestamp timestamp;
    private BigDecimal amount;
    private TypeOfPayment typeOfPayment;
    private Client senderClient;
    private Account senderAccount;
    private CreditCard senderCard;
    private Account recipientAccount;

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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TypeOfPayment getTypeOfPayment() {
        return typeOfPayment;
    }

    public Client getSenderClient() {
        return senderClient;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public CreditCard getSenderCard() {
        return senderCard;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public Integer getID() {
        return id;
    }
}

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
    private Client senderClient;
    private Account senderAccount;
    private CreditCard senderCard;
    private Account recipientAccount;

    public Payment(BigDecimal amount, TypeOfPayment typeOfPayment,
                   Client senderClient,Account senderAccount, CreditCard senderCard, Account recipientAccount) {
        this.amount = amount;
        this.typeOfPayment = typeOfPayment;
        this.senderClient = senderClient;
        this.senderAccount = senderAccount;
        this.senderCard = senderCard;
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

    public void setSenderClient(Client senderClient) {
        this.senderClient = senderClient;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public void setSenderCard(CreditCard senderCard) {
        this.senderCard = senderCard;
    }

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }
}

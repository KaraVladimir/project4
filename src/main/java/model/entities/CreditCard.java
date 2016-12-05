package model.entities;

import model.dao.Identified;

import java.io.Serializable;

/**
 * Represents credit card
 * Created by Kara_Vladimir.
 */
public class CreditCard implements Identified<Integer> {
    private Integer id;
    private String cardNumber;
    private Client client;
    private Account account;

    public CreditCard(Integer id, String cardNumber) {
        this.id = id;
        this.cardNumber = cardNumber;
    }

    public CreditCard(String cardNumber, Client client, Account account) {
        this.cardNumber = cardNumber;
        this.client = client;
        this.account = account;
    }

    public CreditCard(Integer id, String cardNumber, Client client, Account account) {
        this(cardNumber, client, account);
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Client getClient() {
        return client;
    }

    public Account getAccount() {
        return account;
    }
}

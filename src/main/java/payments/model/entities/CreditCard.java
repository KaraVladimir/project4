package payments.model.entities;

import payments.model.dao.Identified;

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

    @Override
    public void setID(Integer integer) {
        this.id = integer;
    }

    @Override
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

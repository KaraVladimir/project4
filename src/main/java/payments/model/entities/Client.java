package payments.model.entities;

import payments.model.dao.Identified;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents client.
 *
 * @author kara.vladimir2@gmail.com.
 */
public class Client implements Identified<Integer> {
    private Integer id;
    private String familyName;
    private String name;
    private String email;
    private List<CreditCard> creditCards = new ArrayList<>();

    public Client(String familyName, String name, String email) {
        this.familyName = familyName;
        this.name = name;
        this.email = email;
    }

    public Client(Integer id, String familyName, String name, String email) {
        this(familyName, name, email);
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

    public String getFamilyName() {
        return familyName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public boolean addCardToList(CreditCard card) {
        return this.creditCards.add(card);
    }

    public boolean removeCardFromList(CreditCard card) {
        return this.creditCards.remove(card);
    }
}

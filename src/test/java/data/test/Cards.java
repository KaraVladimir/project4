package data.test;

import payments.model.entities.CreditCard;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum Cards {
    CARD1(1, "1234");

    public CreditCard card;

    Cards(Integer id, String number) {
        card = new CreditCard(id, number);
    }
}

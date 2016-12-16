package model.dao;

import model.dao.exception.DaoException;
import model.entities.Account;
import model.entities.CreditCard;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface CreditCardDao extends GenericDao{
    CreditCard findCreditCardByAccount(Account account) throws DaoException;
}

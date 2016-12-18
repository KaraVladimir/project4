package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.entities.Account;
import payments.model.entities.CreditCard;

/**
 * interface for dao object {@link CreditCard}
 * @author kara.vladimir2@gmail.com.
 */
public interface CreditCardDao extends GenericDao{
    CreditCard findCreditCardByAccount(Account account) throws DaoException;
}

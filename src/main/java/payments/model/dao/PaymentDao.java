package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.entities.Payment;

import java.util.List;

/**
 * interface for dao object {@link Payment}
 *
 * @author kara.vladimir2@gmail.com.
 */
public interface PaymentDao extends GenericDao {
    List<Payment> findPaymentsByClient(Number usrId) throws DaoException;
}

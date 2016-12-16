package model.dao;

import model.dao.exception.DaoException;
import model.entities.Payment;

import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface PaymentDao extends GenericDao{
    List<Payment> findPaymentsByClient(Number usrId) throws DaoException;
}

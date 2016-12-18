package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.dao.impl.DaoManagerImpl;
import payments.service.exception.ServiceException;

/**
 * Hides transaction details
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoCommand {
    Object execute(DaoManager daoManager) throws DaoException, ServiceException;
}

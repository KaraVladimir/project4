package model.dao;

import model.dao.exception.DaoException;
import model.dao.impl.DaoManagerImpl;
import service.exception.ServiceException;

/**
 * Hides transaction details
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoCommand {
    Object execute(DaoManagerImpl daoManager) throws DaoException, ServiceException;
}

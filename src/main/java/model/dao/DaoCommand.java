package model.dao;

import model.dao.exception.DaoException;
import model.dao.impl.DaoManager;
import service.exception.ServiceException;

/**
 * Hides transaction details
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoCommand {
    Object execute(DaoManager daoManager) throws DaoException, ServiceException;
}

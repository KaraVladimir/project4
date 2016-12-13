package model.dao;

import model.dao.exception.DaoException;

/**
 * Initiates datasource and generates connection
 * @author kara.vladimir2@gmail.com.
 */
public interface IDaoFactory {
    IDaoManager getDaoManager() throws DaoException;
}

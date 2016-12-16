package model.dao;

import model.dao.exception.DaoException;
import model.entities.Client;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface ClientDao extends GenericDao{
    Client findClientByEmail(String email) throws DaoException;
}

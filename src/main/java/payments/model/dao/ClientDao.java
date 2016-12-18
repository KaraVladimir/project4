package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.entities.Client;

/**
 * interface for dao object {@link Client}
 * @author kara.vladimir2@gmail.com.
 */
public interface ClientDao extends GenericDao{
    Client findClientByEmail(String email) throws DaoException;
}

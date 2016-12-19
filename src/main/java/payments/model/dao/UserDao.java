package payments.model.dao;

import payments.model.dao.exception.DaoException;
import payments.model.entities.User;

/**
 * interface for dao object {@link User}
 *
 * @author kara.vladimir2@gmail.com.
 */
public interface UserDao extends GenericDao {
    User findUserByLogin(String login) throws DaoException;
}

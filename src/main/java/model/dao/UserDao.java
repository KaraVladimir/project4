package model.dao;

import model.dao.exception.DaoException;
import model.entities.User;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface UserDao extends GenericDao{
    User findUserByLogin(String login) throws DaoException;
}

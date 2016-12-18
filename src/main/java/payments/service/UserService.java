package payments.service;

import payments.exception.AppException;
import payments.model.dao.exception.DaoException;
import payments.model.entities.Account;
import payments.model.entities.User;

import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface UserService {
    User login(String login, String pass) throws AppException;

    User findById(Number id) throws DaoException, AppException;

    List<Account> findAllUserAccounts(Integer userId) throws AppException;

    List<Account> findAvailableUserAccounts(Integer userId) throws AppException;
}

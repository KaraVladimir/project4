package service;

import exception.AppException;
import model.entities.Account;
import model.entities.User;

import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface UserService {
    User login(String login, String pass) throws AppException;

    List<Account> findUserAccounts(Integer userId) throws AppException;
}

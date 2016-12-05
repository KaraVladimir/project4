package model.dao;

import model.entities.Account;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface AccountDao {
    boolean isNumberExist(String numberAcc);

    Account findByNumber(String numberAcc);
}

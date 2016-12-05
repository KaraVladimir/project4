package model.dao;

import model.dao.impl.DaoManager;

/**
 * Hides transaction details
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoCommand {
    public Object execute(DaoManager daoManager);
}

package model.dao.impl;

import model.dao.DaoCommand;
import model.dao.GenericDao;
import model.dao.IDaoManager;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.dao.impl.AccountDaoImpl;
import model.entities.Account;
import model.entities.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class generates DAO objects and manages connection
 *
 * @author kara.vladimir2@gmail.com.
 */

public class DaoManager implements IDaoManager {
    private static final Logger LOG = Logger.getLogger(DaoManager.class);
    public static final String NO_DAO_FOR_THIS_ENTITY = "doesn't exist dao for this entity";
    public static final String ROLLBACK_FAILED = "rollback failed";
    public static final String COMMIT_FAILED = "commit failed";
    public static final String SET_AUTO_COMMIT_FAILED = "setAutoCommit failed";
    public static final String CLOSE_CONNECTION_FAILED = "close connection failed";
    protected Connection connection = null;
    /**
     * mapping entity class to dao class
     */
    private static Map<Class<? extends Identified>, Class<? extends GenericDao>>
            classHashMap = new HashMap<>();

    public DaoManager(Connection connection) {
        this.connection = connection;
        initClassMap();
    }

    private static void initClassMap() {
        classHashMap.put(Account.class, AccountDaoImpl.class);
        classHashMap.put(User.class, UserDaoImpl.class);
    }

    /**
     * get dao object
     * @param dtoClazz
     * @return
     */
    public <T extends Identified<PK>, PK extends Number> GenericDao<T, PK> getDao(Class<? extends Identified> dtoClazz) {
        LOG.trace(dtoClazz);
        Class daoClazz = classHashMap.get(dtoClazz);
        try {
            return (GenericDao) daoClazz.getDeclaredConstructor(Connection.class).
                    newInstance(this.connection);
        } catch (Exception e) {
            throw new DaoException(NO_DAO_FOR_THIS_ENTITY, e);
        }
    }

    /**
     * represents universal transaction for DaoCommand
     *
     * @param command
     * @return
     */
    public Object transaction(DaoCommand command) {
        try {
            this.connection.setAutoCommit(false);
            Object returnValue = command.execute(this);
            this.connection.commit();
            return returnValue;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException(ROLLBACK_FAILED, e1);
            }
            throw new DaoException(COMMIT_FAILED, e);
        } finally {
            try {
                this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DaoException(SET_AUTO_COMMIT_FAILED, e);
            }
        }
    }

    public void close() {
        // TODO: leaking (field DaoManager in AbstractDaoImpl)
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(CLOSE_CONNECTION_FAILED, e);
        }
    }
}

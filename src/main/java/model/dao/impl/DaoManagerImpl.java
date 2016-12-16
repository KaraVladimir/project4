package model.dao.impl;

import exception.AppException;
import model.dao.DaoCommand;
import model.dao.GenericDao;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.*;
import org.apache.log4j.Logger;
import service.exception.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class generates DAO objects and manages connection
 *
 * @author kara.vladimir2@gmail.com.
 */

public class DaoManagerImpl implements model.dao.DaoManager {
    private static final Logger LOG = Logger.getLogger(DaoManagerImpl.class);

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

    public DaoManagerImpl(Connection connection) {
        this.connection = connection;
        initClassMap();
    }

    private static void initClassMap() {
        classHashMap.put(Account.class, AccountDaoImpl.class);
        classHashMap.put(Client.class, ClientDaoImpl.class);
        classHashMap.put(CreditCard.class, CreditCardDaoImpl.class);
        classHashMap.put(Payment.class, PaymentDaoImpl.class);
        classHashMap.put(User.class, UserDaoImpl.class);
    }

    /**
     * get dao object
     * @param dtoClazz
     * @return
     */
    public <T extends Identified<PK>, PK extends Number> GenericDao<T, PK> getDao(Class<? extends Identified> dtoClazz) throws DaoException {
        Class daoClazz = classHashMap.get(dtoClazz);
        try {
            return (GenericDao) daoClazz.getDeclaredConstructor(Connection.class).
                    newInstance(this.connection);
        } catch (Exception e) {
            throw new DaoException(LOG,NO_DAO_FOR_THIS_ENTITY, e);
        }
    }

    /**
     * represents universal transaction for DaoCommand
     *
     * @param command
     * @return
     */
    public Object transaction(DaoCommand command) throws AppException {
        try {
            this.connection.setAutoCommit(false);
            Object returnValue = command.execute(this);
            this.connection.commit();
            return returnValue;
        } catch (ServiceException e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException(LOG, ROLLBACK_FAILED, e1);
            }
            throw new ServiceException(LOG, e.getMessage());
        } catch (Exception e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException(LOG, ROLLBACK_FAILED, e1);
            }
            throw new DaoException(LOG, COMMIT_FAILED, e);
        } finally {
            try {
                this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DaoException(LOG, SET_AUTO_COMMIT_FAILED, e);
            }
        }
    }

    public void close() throws DaoException {
        try {
            connection.close();
        } catch (Exception e) {
            throw new DaoException(LOG,CLOSE_CONNECTION_FAILED, e);
        }
    }
}

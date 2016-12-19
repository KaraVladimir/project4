package payments.model.dao.impl;

import payments.helper.Msgs;
import payments.exception.AppException;
import payments.model.dao.DaoCommand;
import payments.model.dao.GenericDao;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import payments.model.dao.DaoManager;
import payments.model.entities.*;
import payments.service.exception.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class generates DAO objects and manages connection
 *
 * @author kara.vladimir2@gmail.com.
 */

public class DaoManagerImpl implements DaoManager {
    private static final Logger LOG = Logger.getLogger(DaoManagerImpl.class);

    private boolean flagTransactionProcess = false;

    private Connection connection = null;
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

    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * get dao object
     *
     * @param dtoClazz
     * @return
     */
    public <T extends Identified<PK>, PK extends Number> GenericDao getDao(Class<? extends Identified> dtoClazz) throws DaoException {
        Class daoClazz = classHashMap.get(dtoClazz);
        try {
            return (GenericDao) daoClazz.getDeclaredConstructor(Connection.class).
                    newInstance(getConnection());
        } catch (Exception e) {
            throw new DaoException(LOG, Msgs.NO_DAO_FOR_THIS_ENTITY, e);
        }
    }

    /**
     * represents universal transaction for DaoCommand
     * deprecate because too complicate for test
     *
     * @param command
     * @return
     */
    @Deprecated
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
                throw new DaoException(LOG, Msgs.ROLLBACK_FAILED, e1);
            }
            throw new ServiceException(LOG, e.getMessage());
        } catch (Exception e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException(LOG, Msgs.ROLLBACK_FAILED, e1);
            }
            throw new DaoException(LOG, Msgs.COMMIT_FAILED, e);
        } finally {
            try {
                this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DaoException(LOG, Msgs.SET_AUTO_COMMIT_FAILED, e);
            }
        }
    }

    @Override
    public void beginTransaction() throws DaoException {
        try {
            connection.setAutoCommit(false);
            flagTransactionProcess = false;
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.SET_AUTO_COMMIT_FAILED, e);
        }
    }

    @Override
    public void commitTransaction() throws DaoException {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            flagTransactionProcess = false;
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.COMMIT_FAILED, e);
        }
    }

    @Override
    public void rollbackTransaction() throws DaoException {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ROLLBACK_FAILED, e);
        }
    }

    public void close() throws DaoException {
        try {
            if (flagTransactionProcess) connection.rollback();
            connection.close();
        } catch (Exception e) {
            throw new DaoException(LOG, Msgs.CLOSE_CONNECTION_FAILED, e);
        }
    }
}

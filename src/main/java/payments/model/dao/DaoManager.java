package payments.model.dao;

import payments.exception.AppException;
import payments.model.dao.exception.DaoException;

import java.sql.Connection;

/**
 * * interface for dao manager
 *
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoManager extends AutoCloseable {

    <T extends Identified<PK>, PK extends Number> GenericDao getDao(Class<? extends Identified> dtoClazz) throws DaoException;

    Object transaction(DaoCommand command) throws AppException;

    Connection getConnection();

    void beginTransaction() throws DaoException;

    void commitTransaction() throws DaoException;

    void rollbackTransaction() throws DaoException;

    @Override
    void close() throws DaoException;
}

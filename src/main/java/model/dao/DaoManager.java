package model.dao;

import exception.AppException;
import model.dao.exception.DaoException;

import java.io.Serializable;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface DaoManager extends AutoCloseable{

    <T extends Identified<PK>,PK extends Number> GenericDao<T,PK> getDao(Class<? extends Identified> dtoClazz) throws DaoException;

    Object transaction(DaoCommand command) throws AppException;

    @Override
    void close() throws DaoException;
}

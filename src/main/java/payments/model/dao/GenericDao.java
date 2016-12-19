package payments.model.dao;

import payments.model.dao.exception.DaoException;

import java.util.List;

/**
 * Contains common functionality for all dao objects
 *
 * @author kara.vladimir2@gmail.com.
 */
public interface GenericDao<T extends Identified<PK>, PK extends Number> {

    T save(T t) throws DaoException;

    T read(PK prKey) throws DaoException;

    void update(T t) throws DaoException;

    void delete(T t) throws DaoException;

    List<T> readAll() throws DaoException;
}

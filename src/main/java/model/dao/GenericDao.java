package model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * Contains common functionality for all dao objects
 * @author kara.vladimir2@gmail.com.
 */
public interface GenericDao<T extends Identified<PK>,PK extends Number> {

    T save(T t);

    T read(PK prKey);

    void update(T t);

    void delete(T t);

    List<T> readAll();
}

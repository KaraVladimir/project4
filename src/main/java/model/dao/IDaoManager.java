package model.dao;

import java.io.Serializable;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface IDaoManager extends AutoCloseable{

    <T extends Identified<PK>,PK extends Number> GenericDao<T,PK> getDao(Class<? extends Identified> dtoClazz);

    public Object transaction(DaoCommand command);
}

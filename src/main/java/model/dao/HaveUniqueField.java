package model.dao;

import model.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface is implemented objects who have got one unique field
 * @author kara.vladimir2@gmail.com.
 */
public interface HaveUniqueField {
    static final String ERR_FIND_BY_UNIQUE_FIELD = "find by unique field error";

    default boolean isUniqueFieldExist(String uniqueField) {
        try {
            return findByUniqueFieldQuery(uniqueField).first();
        } catch (SQLException e) {
            throw new DaoException(ERR_FIND_BY_UNIQUE_FIELD, e);
        }
    }

    default ResultSet findByUniqueFieldQuery(String uniqueField) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getQueryFindByUniqueField());
            preparedStatement.setString(1,uniqueField);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new DaoException(ERR_FIND_BY_UNIQUE_FIELD, e);
        }
    }

    default public Identified<Integer> findByUniqueField(String uniqueField) {
        Identified id = null;
        List<Identified> ids = parseResultSetGen(findByUniqueFieldQuery(uniqueField));
        if (ids.size() > 0) {
            id = ids.get(0);
        }
        return id;
    }

    List<Identified> parseResultSetGen(ResultSet resultSet);

    String getQueryFindByUniqueField();

    Connection getConnection();
}

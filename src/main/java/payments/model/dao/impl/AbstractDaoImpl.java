package payments.model.dao.impl;

import payments.helper.Msgs;
import payments.model.dao.GenericDao;
import payments.model.dao.Identified;
import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;

/**
 * Standard CRUD functionality
 *
 * @author kara.vladimir2@gmail.com.
 */
public abstract class AbstractDaoImpl<T extends Identified<PK>, PK extends Number> implements GenericDao {
    private static final Logger LOG = Logger.getLogger(AbstractDaoImpl.class);

    Connection connection;

    AbstractDaoImpl(Connection connection) {
        this.connection = connection;
    }

    protected abstract String getSaveQuery();

    protected abstract String getByPKQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract String getSelectAllQuery();

    protected abstract PreparedStatement prepareStatementForUpdate
            (PreparedStatement preparedStatement, Identified identified) throws DaoException;

    protected abstract PreparedStatement prepareStatementForSave
            (PreparedStatement preparedStatement, Identified identified) throws DaoException;


    protected abstract List<T> parseResultSet(ResultSet rs) throws DaoException;

    public Identified<PK> save(Identified identified) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSaveQuery(), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForSave(preparedStatement, identified);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                identified.setID(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SAVE_QUERY, e);
        }
        return identified;
    }

    public Identified read(Number prKey) throws DaoException {
        List<T> tList;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getByPKQuery())) {
            preparedStatement.setInt(1, (Integer) prKey);
            tList = parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_PK_QUERY, e);
        }
        if (tList == null || tList.size() == 0 || tList.size() > 1) {
            throw new DaoException(getLogger(), Msgs.ERR_GET_BY_PK_QUERY);
        }
        return tList.get(0);
    }

    public void update(Identified identified) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(preparedStatement, identified);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_UPDATE_QUERY, e);
        }
    }

    public void delete(Identified identified) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getDeleteQuery())) {
            if (identified.getID() == null) {
                throw new DaoException(getLogger(), Msgs.ERR_DELETE_QUERY);
            }
            preparedStatement.setInt(1, (Integer) identified.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_DELETE_QUERY, e);
        }
    }

    public List<T> readAll() throws DaoException {
        List<T> tList;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSelectAllQuery())) {
            tList = parseResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(getLogger(), Msgs.ERR_SELECT_ALL_QUERY, e);
        }
        return tList;
    }

    protected abstract Logger getLogger();
}

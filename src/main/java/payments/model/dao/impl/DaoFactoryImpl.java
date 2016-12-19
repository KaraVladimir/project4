package payments.model.dao.impl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import payments.helper.Msgs;
import payments.model.dao.DaoFactory;
import payments.model.dao.DaoManager;
import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Initiates Datasource and create {@link DaoManager}
 *
 * @author kara.vladimir2@gmail.com.
 */
public class DaoFactoryImpl implements payments.model.dao.DaoFactory {
    private static final Logger LOG = Logger.getLogger(DaoFactoryImpl.class);

    private final DataSource dataSource = initDataSource();

    private DaoFactoryImpl() {
    }

    private static class InstanceHolder {
        private static final DaoFactory instance = new DaoFactoryImpl();
    }

    public static DaoFactory getInstance() {
        return InstanceHolder.instance;
    }

    public DaoManager getDaoManager() throws DaoException {
        try {
            DaoManager daoManager = new DaoManagerImpl(dataSource.getConnection());
            return daoManager;
        } catch (SQLException e) {
            throw new DaoException(LOG, Msgs.ERR_CREATE_DAO_MANAGER, e);
        }
    }

    private static DataSource initDataSource() {
        ResourceBundle props = ResourceBundle.getBundle("db");
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(props.getString("db.url"));
        mysqlDataSource.setUser(props.getString("db.user"));
        mysqlDataSource.setPassword(props.getString("db.pwd"));
        return mysqlDataSource;
    }

}

package model.dao.impl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import model.dao.IDaoManager;
import model.dao.exception.DaoException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Singleton enum.INSTANCE initiates Datasource and create DaoManager
 * @author kara.vladimir2@gmail.com.
 */
public enum  DaoFactory implements model.dao.IDaoFactory {

    INSTANCE;

    public static final String ERR_CREATE_DAO_MANAGER = "create daoManager failed";

    private DataSource dataSource;

    DaoFactory() {
        ResourceBundle props = ResourceBundle.getBundle("db");
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(props.getString("db.url"));
        mysqlDataSource.setUser(props.getString("db.user"));
        mysqlDataSource.setPassword(props.getString("db.pwd"));
        this.dataSource = mysqlDataSource;
    }

    public IDaoManager getDaoManager() {
        try {
            return new DaoManager(dataSource.getConnection());
        } catch (SQLException e) {
            throw new DaoException(ERR_CREATE_DAO_MANAGER, e);
        }
    }
}

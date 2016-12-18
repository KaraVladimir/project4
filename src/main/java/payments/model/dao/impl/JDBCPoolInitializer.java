package payments.model.dao.impl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.util.ResourceBundle;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class JDBCPoolInitializer {

    private static class InstanceHolder {
        private static final DataSource instance = initDataSource();
    }

    public static DataSource getInstance() {
        return JDBCPoolInitializer.InstanceHolder.instance;
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

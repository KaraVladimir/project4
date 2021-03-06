package payments.model.dao;

import payments.model.dao.exception.DaoException;
import org.junit.Assert;
import org.junit.Test;
import payments.model.dao.impl.DaoFactoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class TestDaoFactory {
    @Test
    public void testConnection() {
        Connection connection = null;
        ResourceBundle props = ResourceBundle.getBundle("db");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection
                    (props.getString("db.url"), props.getString("db.user"), props.getString("db.pwd"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(connection != null);
    }

    @Test
    public void testDaoManager() {
        DaoManager daoManager = null;
        try {
            daoManager = DaoFactoryImpl.getInstance().getDaoManager();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(daoManager != null);
    }
}

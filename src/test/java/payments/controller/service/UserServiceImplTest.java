package payments.controller.service;

import data.test.Users;
import org.junit.Assert;
import org.mockito.InjectMocks;
import payments.controller.security.Coder;
import payments.exception.AppException;
import payments.model.dao.DaoFactory;
import payments.model.dao.DaoManager;
import payments.model.dao.UserDao;
import payments.model.dao.exception.DaoException;
import payments.model.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import payments.service.UserService;
import payments.service.impl.UserServiceImpl;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UserServiceImplTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private Connection connection;
    @Mock
    private DaoManager daoManager;
    @Mock
    private UserDao mockedUserDao;


    @InjectMocks
    private UserService service = UserServiceImpl.getInstance();

    @Before
    public void init() throws DaoException, SQLException {
        MockitoAnnotations.initMocks(this);
        when(daoFactory.getDaoManager()).thenReturn(daoManager);
        when(daoManager.getConnection()).thenReturn(connection);
        when(daoManager.getDao(User.class)).thenReturn(mockedUserDao);
        when(mockedUserDao.findUserByLogin(anyString())).thenReturn(Users.USER1.user);
    }

    @Test
    public void testLogin() throws AppException, SQLException {
        service.login("user1", Coder.INSTANCE.getHash("q"));
        verify(mockedUserDao,times(1)).findUserByLogin("user1");
    }

//    @Test
    public void testFindUserAccounts() throws AppException {
        service.findAvailableUserAccounts(1);
    }
}


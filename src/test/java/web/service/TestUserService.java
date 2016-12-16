package web.service;

import data.test.Users;
import exception.AppException;
import model.dao.DaoFactory;
import model.dao.DaoManager;
import model.dao.UserDao;
import model.dao.exception.DaoException;
import model.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;

import java.sql.Connection;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class TestUserService {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DaoManager daoManager;

    @Mock
    private UserDao mockedUserDao;
    @Mock
    private Connection connection;
    @Mock
    private DataSource source;

    private UserService service;

    @Before
    public void init() throws DaoException {
        MockitoAnnotations.initMocks(this);
        when(daoFactory.getDaoManager()).thenReturn(daoManager);
        when(daoManager.getDao(User.class)).thenReturn(mockedUserDao);
        when(mockedUserDao.findUserByLogin(anyString())).thenReturn(Users.USER1.user);
        service = UserServiceImpl.getInstance();
    }

    @Test
    public void testLogin() throws AppException {
        service.login("user1", "q");
        verify(mockedUserDao, times(1)).findUserByLogin("user1");
    }

    @Test
    public void testFindUserAccounts() throws AppException {
        service.findUserAccounts(1);
    }
}


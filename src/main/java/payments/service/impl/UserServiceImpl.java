package payments.service.impl;

import payments.exception.AppException;
import payments.model.dao.ClientDao;
import payments.model.dao.DaoFactory;
import payments.model.dao.DaoManager;
import payments.model.dao.UserDao;
import payments.model.dao.impl.DaoFactoryImpl;
import payments.model.entities.Account;
import payments.model.entities.Client;
import payments.model.entities.CreditCard;
import payments.model.entities.User;
import payments.service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UserServiceImpl implements payments.service.UserService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserServiceImpl.class);

    public static final String CLIENT_NOT_EXIST = "client doesn't exist";
    private static final String USER_NOT_EXIST = "user doesn't exist";
    public static final String INCORRECT_LOGIN = "Login or password is incorrect";

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();


    private static class Holder {
        private static final UserServiceImpl instance = new UserServiceImpl();
    }

    public static UserServiceImpl getInstance() {
        return Holder.instance;
    }


    @Override
    public User login(String login, String pass) throws AppException {
        try (DaoManager manager = daoFactory.getDaoManager()) {
            UserDao userDao = (UserDao) manager.getDao(User.class);
            User user = userDao.findUserByLogin(login);
            checkCorrectLoginAndPass(pass, user);
            return user;
        }
    }

    @Override
    public User findById(Number id) throws AppException {
        try (DaoManager manager = daoFactory.getDaoManager()){
            UserDao userDao = (UserDao) manager.getDao(User.class);
            User user = (User) userDao.read(id);
            checkUserExist(user);
            return user;
        }
    }

    public List<Account> findAllUserAccounts(Integer userId) throws AppException {
        List<Account> accounts = null;
        DaoFactory daoFactory = DaoFactoryImpl.getInstance();
        try (DaoManager daoManager = daoFactory.getDaoManager()){
            accounts = (List<Account>) daoManager.transaction(daoManager1 -> {
                List<Account> accounts1 = new ArrayList<>();
                UserDao userDao = (UserDao) daoManager1.getDao(User.class);
                ClientDao clientDao = (ClientDao) daoManager1.getDao(Client.class);
                User user = (User) userDao.read(userId);
                checkUserExist(user);
                checkClientExist(user.getClient());
                Client client = (Client) clientDao.read(user.getClient().getID());
                checkClientExist(client);
                List<CreditCard> cardList = client.getCreditCards();
                accounts1  = cardList.stream().map((cc) ->cc.getAccount()).collect(Collectors.toList());
                return accounts1;
            });
        }
        return accounts;
    }

    @Override
    public List<Account> findAvailableUserAccounts(Integer userId) throws AppException {
        List<Account> accounts = null;
        accounts = findAllUserAccounts(userId).stream()
                .filter(account -> !account.isBlocked()).collect(Collectors.toList());
        return accounts;
    }





    private void checkClientExist(Client client) throws ServiceException {
        if (client == null) {
            throw new ServiceException(LOG, CLIENT_NOT_EXIST);
        }
    }

    private void checkUserExist(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException(LOG, USER_NOT_EXIST);
        }
    }

    private void checkCorrectLoginAndPass(String pass, User user) throws ServiceException {
        if (user == null || !user.getPassword().equals(pass)) {
            throw new ServiceException(LOG, INCORRECT_LOGIN);
        }
    }

}

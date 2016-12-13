package service;

import exception.AppException;
import model.dao.impl.*;
import model.entities.*;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum  UserService {
    INSTANCE;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserService.class);

    public static final String CLIENT_NOT_EXIST = "client doesn't exist";
    private static final String USER_NOT_EXIST = "user doesn't exist";
    public static final String INCORRECT_LOGIN = "Login or password is incorrect";


    public User login(String login,String pass) throws AppException {
        try (DaoManager manager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()){
            UserDaoImpl userDao = (UserDaoImpl) manager.getDao(User.class);
            User user = (User) userDao.findByUniqueField(login);
            checkCorrectLoginAndPass(pass, user);
            return user;
        }
    }

    public List<Account> findUserAccounts(Integer userId) throws AppException {
        List<Account> accounts = new ArrayList<>();
        try (DaoManager daoManager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()){
            final String[] message = new String[1];
            daoManager.transaction(daoManager1 -> {
                UserDaoImpl userDao = (UserDaoImpl) daoManager1.getDao(User.class);
                ClientDaoImpl clientDao = (ClientDaoImpl) daoManager1.getDao(Client.class);
                User user = (User) userDao.read(userId);
                checkUserExist(user);
                Client client = (Client) clientDao.read(user.getClient().getID());
                checkClientExist(client);
                List<CreditCard> cardList = client.getCreditCards();
                cardList.forEach(v->{
                    accounts.add(v.getAccount());
                });
                return DaoManager.Result.SUCCESS;
            });
        }
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

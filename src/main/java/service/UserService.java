package service;

import model.dao.impl.DaoFactory;
import model.dao.impl.DaoManager;
import model.dao.impl.UserDaoImpl;
import model.entities.User;

import java.util.logging.Logger;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UserService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserService.class);
    //todo casts
    public User login(String login,String pass) {
        LOG.trace("UserService start");
        User userForReturn = null;
        try (DaoManager manager = (DaoManager) DaoFactory.INSTANCE.getDaoManager()){
            UserDaoImpl userDao = (UserDaoImpl) manager.getDao(User.class);
            User user = (User) userDao.findByUniqueField(login);
            if ((user != null) && (user.getPassword().equals(pass))) {
                userForReturn = user;
            }
            return userForReturn;
        }
    }
}

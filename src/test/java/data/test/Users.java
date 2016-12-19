package data.test;

import payments.model.entities.User;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum Users {
    ADMIN(1, "admin", "7694f4a66316e53c8cdd9d9954bd611d", true),
    USER1(2, "user1", "7694f4a66316e53c8cdd9d9954bd611d", false),
    USER2(3, "user2", "7694f4a66316e53c8cdd9d9954bd611d", false);

    public User user;

    Users(Integer id, String login, String pass, boolean isAdmin) {
        user = new User(id, login, pass, isAdmin);
    }
}

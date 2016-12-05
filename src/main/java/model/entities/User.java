package model.entities;

import model.dao.Identified;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class User implements Identified<Integer>{
    private Integer id;
    private String login;
    private String password;
    private boolean isAdmin;
    private Client client;

    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(Integer id, String login, String password, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public Integer getID() {
        return id;
    }
}

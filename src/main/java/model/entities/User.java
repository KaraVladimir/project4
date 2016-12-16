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
//    private Number clientId;

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

    @Override
    public void setID(Integer integer) {
        this.id = integer;
    }
    @Override
    public Integer getID() {
        return id;
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

    public void setClient(Client client) {
        this.client = client;
    }
}

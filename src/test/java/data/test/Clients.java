package data.test;

import model.entities.Client;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum Clients {
    CL1(1,"Ivanova","Elena","asd@g.com"),
    CL2(2,"Petrenko","Petr","qwe@g.com");

    public Client client;

    Clients(Integer id,String fname,String name,String email) {
        new Client(id, fname, name, email);
    }
}

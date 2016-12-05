package model.dao;

/**
 * Initiates datasource and generates connection
 * @author kara.vladimir2@gmail.com.
 */
public interface IDaoFactory {
    IDaoManager getDaoManager();
}

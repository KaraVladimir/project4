package payments.model.dao;

/**
 * represents Entity with Serializable primary key.
 *
 * @author kara.vladimir2@gmail.com.
 */
public interface Identified<PK extends Number> {
    PK getID();

    void setID(PK pk);
}

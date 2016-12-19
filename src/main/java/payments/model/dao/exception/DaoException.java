package payments.model.dao.exception;

import payments.exception.AppException;
import org.apache.log4j.Logger;


/**
 * Dao exception
 * @author kara.vladimir2@gmail.com.
 */

public class DaoException extends AppException {

    public DaoException(Logger logger, String message) {
        super(logger, message);
    }

    public DaoException(Logger logger, String message, Throwable cause) {
        super(logger, message, cause);
    }

    public DaoException(Logger logger, Throwable cause) {
        super(logger, cause);
    }
}

package model.dao.exception;

import exception.AppException;

/**
 * @author kara.vladimir2@gmail.com.
 */

public class DaoException extends AppException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}

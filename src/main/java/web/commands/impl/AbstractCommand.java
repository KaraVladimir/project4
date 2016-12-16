package web.commands.impl;

import exception.AppException;
import model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import service.exception.ServiceException;
import web.commands.Command;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

/**
 * @author kara.vladimir2@gmail.com.
 */
public abstract class AbstractCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AbstractCommand.class);

    public static final String BAD_PARSING_ID = "Parsing id is failed";
    public static final String BAD_PARSING_BIGDECIMAL = "Parsing amount is failed";
    public static final String INCORRECT_ACCOUNT_ID = "Incorrect account ID";
    public static final String INCORRECT_ACCOUNT_NUMBER = "Incorrect account number";
    public static final String INCORRECT_USER_ID = "Incorrect user ID";
    public static final String ERR_LOGIN = "Wrong login or password";
    public static final String EMPTY_LOGIN = "Empty login";
    public static final String EMPTY_PASS = "Empty password";
    public static final String INCORRECT_AMOUNT = "Incorrect amount";
    public static final String ACCOUNT_NOT_EXIST = "Account doesn't exist";
    public static final String ERR_DB = "DB ERROR!";
    public static final String UNKNOWN_ERR = "UNKNOWN ERROR!";



    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path = null;
        try {
            path = proceedExecute(request, response);
        } catch (ServiceException e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, e.getMessage());
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);
            (e.getLogger()).info(e.getMessage());
        } catch (AppException e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, ERR_DB);
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);
            e.printStackTrace();
            (e.getLogger()).error(e.getMessage());
        } catch (Exception e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, UNKNOWN_ERR);
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);
            LOG.error(e.getMessage());
            LOG.error(e.getStackTrace());
        }

        return path;
    }

    public abstract String proceedExecute
            (HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    protected void checkNullOrEmptyString(Logger logger, String str, String textErr) throws ServiceException {
        if (str == null || str.isEmpty()) {
            throw new ServiceException(logger, textErr);
        }
    }

    protected void checkNullObject(Logger logger, Object o, String textErr) throws ServiceException {
        if (o == null) {
            throw new ServiceException(logger, textErr);
        }
    }

    protected Integer checkAndParsingStringToInteger(Logger logger, String str) throws ServiceException {
        Integer integ;
        try {
            integ = Integer.parseInt(str);
        }catch (NumberFormatException e) {
            throw new ServiceException(logger, BAD_PARSING_ID);
        }
        return integ;
    }

    protected Integer getIdFromString(Logger logger,String param,String errMsg) throws ServiceException {
        Integer id;
        checkNullOrEmptyString(logger,param, errMsg);
        id = checkAndParsingStringToInteger(logger, param);
        return id;
    }

    protected BigDecimal getBigDecimalFromString(Logger logger,String param,String errMsg) throws ServiceException {
        BigDecimal bigDecimal;
        checkNullOrEmptyString(logger,param, errMsg);
        try {
            bigDecimal = new BigDecimal(param);
        } catch (NumberFormatException e) {
            throw new ServiceException(logger, BAD_PARSING_BIGDECIMAL);
        }
        return bigDecimal;
    }
}

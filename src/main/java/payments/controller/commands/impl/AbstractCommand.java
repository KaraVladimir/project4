package payments.controller.commands.impl;

import payments.helper.Msgs;
import payments.exception.AppException;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import payments.service.exception.ServiceException;
import payments.controller.commands.Command;
import payments.helper.Attrs;
import payments.helper.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static payments.helper.Msgs.*;

/**
 * Represents common functionality for all commands
 * @author kara.vladimir2@gmail.com.
 */
public abstract class AbstractCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AbstractCommand.class);


    /**
     * For intercepting exception
     * @param request
     * @param response
     * @return path to next page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path;
        try {
            path = proceedExecute(request, response);
        } catch (ServiceException e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, e.getMessage());
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);

            (e.getLogger()).info(forLog(e.getMessage()));
        } catch (AppException e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, ERR_DB);
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);
            e.printStackTrace();

            (e.getLogger()).error(forLog(e.getMessage()));

        } catch (Exception e) {
            path = Pages.mapTransition.get(request.getRequestURI());
            request.setAttribute(Attrs.MSG, UNKNOWN_ERR);
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOG.error(e.getMessage());
            LOG.error(errors.toString());
        }

        return path;
    }


    protected abstract String proceedExecute
            (HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    void checkNullOrEmptyString(Logger logger, String str, String textErr) throws ServiceException {
        if (str == null || str.isEmpty()) {
            throw new ServiceException(logger, textErr);
        }
    }

    void checkNullObject(Logger logger, Object o, String textErr) throws ServiceException {
        if (o == null) {
            throw new ServiceException(logger, textErr);
        }
    }

    private Integer checkAndParsingStringToInteger(Logger logger, String str) throws ServiceException {
        Integer integer;
        try {
            integer = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ServiceException(logger, BAD_PARSING_ID);
        }
        return integer;
    }

    Integer getIdFromString(Logger logger, String param) throws ServiceException {
        Integer id;
        checkNullOrEmptyString(logger, param, INCORRECT_ACCOUNT_ID);
        id = checkAndParsingStringToInteger(logger, param);
        return id;
    }

    BigDecimal getBigDecimalFromString(Logger logger, String param) throws ServiceException {
        BigDecimal bigDecimal;
        checkNullOrEmptyString(logger, param, INCORRECT_AMOUNT);
        try {
            bigDecimal = new BigDecimal(param);
        } catch (NumberFormatException e) {
            throw new ServiceException(logger, BAD_PARSING_BIGDECIMAL);
        }
        return bigDecimal;
    }
}

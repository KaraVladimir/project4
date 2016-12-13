package web.commands.impl;

import exception.AppException;
import model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class LogoutCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(BlockCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
        if (userId != null) {
            request.getSession().invalidate();
        }
        return Pages.PAGE_LOGIN;
    }
}

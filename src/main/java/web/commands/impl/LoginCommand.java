package web.commands.impl;

import model.dao.exception.DaoException;
import model.entities.User;
import org.apache.log4j.Logger;
import service.UserService;
import web.config.Attrs;
import web.config.Pages;
import web.security.Coder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class LoginCommand extends AbstractCommand{
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);
    public static final String ERR_EMPTY = "Login/password cannot be empty";
    public static final String ERR_LOGIN = "Wrong login or password";

    public String execute(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws DaoException {
        HttpSession session = request.getSession();

        String login = request.getParameter("login");
        String password = Coder.INSTANCE.getHash(request.getParameter("password"));
        LOG.trace(password);
        if (login == null || password == null || login.isEmpty()|| password.isEmpty()) {
            throw new DaoException(ERR_EMPTY);
        }
        User user = new UserService().login(login, password);
        if (user != null) {
            request.getSession().setAttribute(Attrs.USER_ID, user.getId());
            request.getSession().setAttribute(Attrs.CLIENT, user.getClient());
            request.getSession().setAttribute(Attrs.IS_ADMIN, user.isAdmin());
            if (user.isAdmin()) {
                return Pages.PAGE_ADMIN_START;
            } else {
                return Pages.PAGE_USER_START;
            }
        } else {
            request.setAttribute(Attrs.MSG, ERR_LOGIN);
        }
        LOG.trace(login);
        //todo return to login page
        return Pages.PAGE_LOGIN;
    }
}

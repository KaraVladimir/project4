package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import model.entities.User;
import org.apache.log4j.Logger;
import service.impl.AccountServiceImpl;
import service.impl.UserServiceImpl;
import web.config.Attrs;
import web.config.Pages;
import web.security.Coder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class LoginCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);



    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        String login = request.getParameter(Attrs.LOGIN);
        String pwd = request.getParameter(Attrs.PASSWORD);

        if ((request.getSession().getAttribute(Attrs.USER_ID) != null)) {
            if ((boolean) request.getSession().getAttribute(Attrs.IS_ADMIN)) {
                List<Account> accounts = AccountServiceImpl.getInstance().findBlocked();
                request.setAttribute(Attrs.BLOCKED_ACCOUNTS,accounts);
                return Pages.PAGE_ADMIN_START;
            } else {
                return Pages.PAGE_USER_START;
            }
        }

        checkNullOrEmptyString(LOG,login, EMPTY_LOGIN);
        checkNullOrEmptyString(LOG,pwd, EMPTY_PASS);
        String password = Coder.INSTANCE.getHash(pwd);

        User user = UserServiceImpl.getInstance().login(login, password);
        checkNullObject(LOG,user,ERR_LOGIN);

        request.getSession().setAttribute(Attrs.USER_ID, user.getId());
        request.getSession().setAttribute(Attrs.CLIENT, user.getClient());
        request.getSession().setAttribute(Attrs.IS_ADMIN, user.isAdmin());
        if (user.isAdmin()) {
            List<Account> accounts = AccountServiceImpl.getInstance().findBlocked();
            request.setAttribute(Attrs.BLOCKED_ACCOUNTS,accounts);
            return Pages.PAGE_ADMIN_START;
        } else {
            return Pages.PAGE_USER_START;
        }

    }
}

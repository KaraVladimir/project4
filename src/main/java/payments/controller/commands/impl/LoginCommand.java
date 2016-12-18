package payments.controller.commands.impl;

import payments.exception.AppException;
import payments.model.entities.Account;
import payments.model.entities.User;
import org.apache.log4j.Logger;
import payments.service.impl.AccountServiceImpl;
import payments.service.impl.UserServiceImpl;
import payments.config.Attrs;
import payments.config.Pages;
import payments.controller.security.Coder;

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
        Object userId = request.getSession().getAttribute(Attrs.USER_ID);
        Object isAdmin = request.getSession().getAttribute(Attrs.IS_ADMIN);

        if ((userId != null)) {
            if ((boolean)isAdmin) {
                return returnForAdmin(request);
            } else {
                return returnForUser(request, UserServiceImpl.getInstance().findById((Integer) userId));
            }
        }

        checkNullOrEmptyString(LOG,login, EMPTY_LOGIN);
        checkNullOrEmptyString(LOG,pwd, EMPTY_PASS);
        String password = Coder.INSTANCE.getHash(pwd);

        User user = UserServiceImpl.getInstance().login(login, password);
        checkNullObject(LOG,user,ERR_LOGIN);

        request.getSession().setAttribute(Attrs.USER_ID, user.getId());
        request.getSession().setAttribute(Attrs.USER_LOGIN, user.getLogin());
        request.getSession().setAttribute(Attrs.CLIENT, user.getClient());
        request.getSession().setAttribute(Attrs.IS_ADMIN, user.isAdmin());
        if (user.isAdmin()) {
            return returnForAdmin(request);
        } else {
            return returnForUser(request, user);
        }

    }

    private String returnForUser(HttpServletRequest request, User user) throws AppException {
        List<Account> accounts = UserServiceImpl.getInstance().findAllUserAccounts(user.getID());
        request.setAttribute(Attrs.ALL_ACCOUNTS,accounts);
        return Pages.PAGE_USER_START;
    }

    private String returnForAdmin(HttpServletRequest request) throws AppException {
        List<Account> accounts = AccountServiceImpl.getInstance().findBlocked();
        request.setAttribute(Attrs.BLOCKED_ACCOUNTS,accounts);
        return Pages.PAGE_ADMIN_START;
    }
}

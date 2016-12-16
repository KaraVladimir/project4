package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.impl.AccountServiceImpl;
import web.config.Attrs;
import web.config.Msgs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UnblockCommand extends AbstractCommand{
    private static final Logger LOG = Logger.getLogger(UnblockCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        List<Account> accounts = AccountServiceImpl.getInstance().findBlocked();
        request.setAttribute(Attrs.BLOCKED_ACCOUNTS,accounts);
        if (request.getParameter(Attrs.EXECUTE)==null||request.getParameter(Attrs.EXECUTE).equals("n")) {
            return Pages.PAGE_ADMIN_UNBLOCK;
        }
        Integer id = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        Account account = AccountServiceImpl.getInstance().unblockAccountByID(id);
        if (account != null) {
            request.setAttribute(Attrs.MSG, Msgs.SUCCESS);
        }
        request.setAttribute(Attrs.ACCOUNT,account);
        accounts = AccountServiceImpl.getInstance().findBlocked();
        request.setAttribute(Attrs.BLOCKED_ACCOUNTS,accounts);

        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}

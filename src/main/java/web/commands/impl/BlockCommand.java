package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.AccountService;
import service.UserService;
import sun.nio.cs.ext.MS874;
import web.config.Attrs;
import web.config.Msgs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class BlockCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(BlockCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        Integer usId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
        List<Account> accounts = UserService.INSTANCE.findUserAccounts(usId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        if (request.getParameter(Attrs.EXECUTE)==null||request.getParameter(Attrs.EXECUTE).equals("n")) {
            return Pages.PAGE_USER_BLOCK;
        }

        Integer accId = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        Account account = AccountService.INSTANCE.block(accId);
        if (account != null) {
            request.setAttribute(Attrs.MSG, Msgs.SUCCESS);
        }
        accounts = UserService.INSTANCE.findUserAccounts(usId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        return Pages.PATH_BLOCK;
    }



}

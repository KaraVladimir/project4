package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.AccountService;
import service.UserService;
import web.config.Attrs;
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
        Integer accId = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        Integer usId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);

        Account account = AccountService.INSTANCE.block(accId);
        List<Account> accounts = UserService.INSTANCE.findUserAccounts(usId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        return Pages.PATH_BLOCK;
    }



}

package web.commands.impl;

import exception.AppException;
import model.dao.exception.DaoException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.AccountService;
import service.UserService;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class RefillCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(PayCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
        Integer accS_ID = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        BigDecimal amount = getBigDecimalFromString(LOG, request.getParameter(Attrs.ACC_AMOUNT), INCORRECT_AMOUNT);

        AccountService.INSTANCE.refill(accS_ID,amount);
        List<Account> accounts = UserService.INSTANCE.findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        return Pages.PAGE_USER_REFILL;
    }
}

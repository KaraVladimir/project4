package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import model.entities.Payment;
import org.apache.log4j.Logger;
import service.impl.AccountServiceImpl;
import service.impl.UserServiceImpl;
import web.config.Attrs;
import web.config.Msgs;
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
        List<Account> accounts = UserServiceImpl.getInstance().findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        if (request.getParameter(Attrs.EXECUTE)==null||request.getParameter(Attrs.EXECUTE).equals("n")) {
            return Pages.PAGE_USER_REFILL;
        }

        Integer accS_ID = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        BigDecimal amount = getBigDecimalFromString(LOG, request.getParameter(Attrs.ACC_AMOUNT), INCORRECT_AMOUNT);

        Payment payment = AccountServiceImpl.getInstance().refill(accS_ID,amount);
        if (payment != null) {
            request.setAttribute(Attrs.MSG, Msgs.SUCCESS);
        }
        accounts = UserServiceImpl.getInstance().findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);

        return Pages.PAGE_USER_REFILL;
    }
}

package payments.helper;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * references to the error messages and method allows for logging messages
 * @author kara.vladimir2@gmail.com.
 */
public final class Msgs {
    public static final String ACCOUNT_NULL = "account.null";
    public static final String ACCOUNT_NOT_BLOCKED = "account.not.blocked";
    public static final String ACCOUNT_IS_BLOCKED = "account.is.blocked";
    public static final String SUCCESS = "success.operation";
    public static final String ACCOUNT_FORMAT_ERROR = "account.format.error";
    public static final String AMOUNT_FORMAT_ERROR = "amount.format.error";
    public static final String NOT_ENOUGH_MONEY = "account.no.money";

    public static final String CODER_ERROR = "coder.error";
    public static final String BAD_PARSING_ID = "parsing.error";
    public static final String BAD_PARSING_BIGDECIMAL = "parsing.bigdecimal.error";
    public static final String INCORRECT_ACCOUNT_ID = "account.id.incorrect";
    public static final String INCORRECT_ACCOUNT_NUMBER = "account.number.incorrect";
    public static final String INCORRECT_USER_ID = "user.id.incorrect";
    public static final String ERR_LOGIN = "login.or.password.incorrect";
    public static final String EMPTY_LOGIN = "login.empty";
    public static final String EMPTY_PASS = "password.empty";
    public static final String INCORRECT_AMOUNT = "amount.incorrect";
    public static final String ACCOUNT_NOT_EXIST = "account.null";
    public static final String ERR_DB = "db.error";
    public static final String UNKNOWN_ERR = "unknown.error";
    public static final String ERR_SAVE_QUERY = "save.failed";
    public static final String ERR_GET_BY_PK_QUERY = "get.by.pk.failed";
    public static final String ERR_UPDATE_QUERY = "update.failed";
    public static final String ERR_DELETE_QUERY = "delete.failed";
    public static final String ERR_SELECT_ALL_QUERY = "select.all.failed";
    public static final String ERR_PARSING = "parsing.failed";
    public static final String ERR_UPDATE = "update.failed";
    public static final String ERR_GET_BY_ACCOUNT_QUERY = "find.credit.card.by.account.failed";
    public static final String ERR_GET_BLOCKED_QUERY = "find.blocked.accounts.failed";
    public static final String ERR_GET_PAYMENTS_BY_USER = "get.payments.by.user.failed";
    public static final String ERR_FIND_BY_NUMBER = "find.by.account.number.failed";
    public static final String ERR_FIND_BY_EMAIL = "find.client.by.email.failed";
    public static final String ERR_FIND_BY_LOGIN = "find.user.by.login.failed";
    public static final String ERR_CREATE_DAO_MANAGER = "create.daomanager.failed";
    public static final String NO_DAO_FOR_THIS_ENTITY = "doesn.t.exist.dao.for.this.entity";
    public static final String ROLLBACK_FAILED = "rollback.failed";
    public static final String COMMIT_FAILED = "commit.failed";
    public static final String SET_AUTO_COMMIT_FAILED = "setautocommit.failed";
    public static final String CLOSE_CONNECTION_FAILED = "close.connection.failed";
    public static final String CLIENT_NOT_EXIST = "client.doesn.t.exist";
    public static final String USER_NOT_EXIST = "user.doesn.t.exist";
    public static final String INCORRECT_LOGIN = "login.or.password.is.incorrect";
    public static final String UNAUTHORIZED_ACCESS = "attempted.unauthorized.access";

    public static String forLog(String err) {
        ResourceBundle rb = ResourceBundle.getBundle("msg", Locale.ENGLISH);
        if (rb.containsKey(err)) return rb.getString(err);
        else return err;
    }
}

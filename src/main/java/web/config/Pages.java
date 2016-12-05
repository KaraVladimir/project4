package web.config;

/**
 * @author kara.vladimir2@gmail.com.
 */
public final class Pages {
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String LOGIN_PATH = "/";
    public static final String PAGE_ERR = "/WEB-INF/pages/error_page.jsp";
    public static final String PAGE_RESULT = "/WEB-INF/pages/operation_result.jsp";

    public static final String PAGE_ADMIN_UNBLOCK = "/WEB-INF/view/pages/admin/unblock.jsp";
    public static final String PAGE_ADMIN_HOME = "/WEB-INF/view/pages/admin/home.jsp";

    public static final String PAGE_USER_PAY = "/WEB-INF/view/pages/user/pay.jsp";
    public static final String PAGE_USER_HOME = "/WEB-INF/view/pages/user/home.jsp";
    public static final String PAGE_USER_REFILL = "/WEB-INF/pages/user/error_page.jsp";
    public static final String PAGE_USER_BLOCK = "/WEB-INF/pages/user/error_page.jsp";


    public static final String PAGE_ADMIN_START = PAGE_ADMIN_HOME;
    public static final String PAGE_USER_START = PAGE_USER_HOME;
}

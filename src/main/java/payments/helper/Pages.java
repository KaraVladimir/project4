package payments.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class. Pages and transition map
 * @author kara.vladimir2@gmail.com.
 */
public final class Pages {
    public static final String PAGE_LOGIN = "/WEB-INF/view/login.jsp";

    public static final String PAGE_ADMIN_UNBLOCK = "/WEB-INF/view/pages/admin/unblock.jsp";
//    public static final String PAGE_ADMIN_HOME = "/WEB-INF/view/pages/admin/home.jsp";

    public static final String PAGE_USER_PAY = "/WEB-INF/view/pages/user/pay.jsp";
    public static final String PAGE_USER_REFILL = "/WEB-INF/view/pages/user/refill.jsp";
    public static final String PAGE_USER_BLOCK = "/WEB-INF/view/pages/user/block.jsp";
    public static final String PAGE_USER_HOME = "/WEB-INF/view/pages/user/home.jsp";

    public static final String PAGE_ADMIN_START = PAGE_ADMIN_UNBLOCK;
    public static final String PAGE_USER_START = PAGE_USER_HOME;

    public static final String PATH_ADM = "/page/admin";
    public static final String PATH_USR = "/page/user";
    public static final String PATH_HOME = "/page/home";
    public static final String PATH_LOGOUT = "/page/logout";
    public static final String PATH_UNBLOCK = "/page/admin/unblock";
    public static final String PATH_FIND_ACCOUNT = "/page/findAccount";
    public static final String PATH_REFILL = "/page/user/refill";
    public static final String PATH_BLOCK = "/page/user/block";
    public static final String PATH_PAY = "/page/user/pay";


    public static Map<String, String> mapTransition = initMap();

    /**
     * Inits map for transition after error
     * @return
     */
    private static Map<String, String> initMap() {
        Map<String, String> map = new HashMap<>();
        map.put(PATH_UNBLOCK, PAGE_ADMIN_UNBLOCK);
        map.put(PATH_PAY, PAGE_USER_PAY);
        map.put(PATH_REFILL, PAGE_USER_REFILL);
        map.put(PATH_BLOCK, PAGE_USER_BLOCK);
        map.put(PATH_HOME, PAGE_LOGIN);

        return map;
    }
}

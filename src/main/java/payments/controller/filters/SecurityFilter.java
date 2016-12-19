package payments.controller.filters;

import org.apache.log4j.Logger;
import payments.helper.Attrs;
import payments.helper.Msgs;
import payments.helper.Pages;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static payments.helper.Msgs.*;

/**
 * Prevents unauthorized access
 * @author kara.vladimir2@gmail.com.
 */
public class SecurityFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object userId = request.getSession().getAttribute(Attrs.USER_ID);
        if (userId == null) {
            if (!request.getRequestURI().equals(Pages.PATH_HOME)) {
                LOG.info(forLog(UNAUTHORIZED_ACCESS));
                request.getRequestDispatcher(Pages.PAGE_LOGIN).forward(request, response);
            }
        } else {
            boolean isAdm = (boolean) request.getSession().getAttribute(Attrs.IS_ADMIN);
            String uri = request.getRequestURI();
            if (isAdm && uri.startsWith(Pages.PATH_USR)) {
                LOG.info(forLog(UNAUTHORIZED_ACCESS));
                request.getRequestDispatcher(Pages.PATH_HOME).forward(request, response);
            }
            if (!isAdm && uri.startsWith(Pages.PATH_ADM)) {
                LOG.info(forLog(UNAUTHORIZED_ACCESS));
                request.getRequestDispatcher(Pages.PATH_HOME).forward(request, response);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

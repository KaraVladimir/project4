package web.filters;

import org.apache.log4j.Logger;
import service.exception.ServiceException;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class SecurityFilter implements Filter{
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
                request.getRequestDispatcher(Pages.PAGE_LOGIN).forward(request, response);
            }
        } else {
            boolean isAdm = (boolean) request.getSession().getAttribute(Attrs.IS_ADMIN);
            String uri = request.getRequestURI();
            if (isAdm&&uri.startsWith(Pages.PATH_USR)) {
                request.getRequestDispatcher(Pages.PAGE_ADMIN_START).forward(request, response);
            }
            if (!isAdm&&uri.startsWith(Pages.PATH_ADM)) {
                request.getRequestDispatcher(Pages.PAGE_USER_START).forward(request, response);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {

    }
}

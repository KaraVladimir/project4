package web.filters;

import org.apache.log4j.Logger;
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
        LOG.trace("intercept");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getSession().getAttribute(Attrs.USER_ID) == null&&!request.getRequestURI().equals(Pages.PATH_HOME)) {
            response.sendRedirect(Pages.PAGE_LOGIN);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

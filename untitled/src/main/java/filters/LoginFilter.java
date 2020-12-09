package filters;

import constants.Links;
import utils.hibernate.HibernateSessionFactory;
import utils.hibernate.Hibernator;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.ApiLinks.API;
import static constants.Keys.TOKEN;
import static constants.Links.LOGIN;

@WebFilter(value = {API + "/*", "*" + Links.SUFFIX})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        final Hibernator instance = Hibernator.getInstance();
        instance.clear();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean useHeader = false;
        String token = request.getHeader(TOKEN);
        if (token == null){
            final Object attribute = request.getSession().getAttribute(TOKEN);
            if (attribute != null){
                token = attribute.toString();
            }
        } else {
            useHeader = true;
        }
        if (token != null){
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(request.getContextPath() + LOGIN);
        }
    }

    @Override
    public void destroy() {
        HibernateSessionFactory.shutdown();
    }
}

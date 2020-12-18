package filters;

import utils.hibernate.HibernateSessionFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static constants.Keys.CONTEXT;
import static constants.Keys.LOCALE;

@WebFilter(value = {"*"})
public class ContextFilter implements Filter {

    private static final String NOW = "now";
    private long now;

    @Override
    public void init(FilterConfig filterConfig) {
        now = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        req.setAttribute(CONTEXT, req.getContextPath());
        req.setAttribute(LOCALE, "uk");
        req.setAttribute(NOW, now);
        filterChain.doFilter(req, servletResponse);
    }

    @Override
    public void destroy() {
        HibernateSessionFactory.shutdown();
    }
}

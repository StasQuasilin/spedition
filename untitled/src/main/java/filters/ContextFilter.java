package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static constants.Keys.CONTEXT;
import static constants.Keys.LOCALE;

@WebFilter(value = {"*"})
public class ContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        req.setAttribute(CONTEXT, req.getContextPath());
        filterChain.doFilter(req, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

package filters;

import constants.Links;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static constants.Keys.LOCALE;

@WebFilter("*" + Links.SUFFIX)
public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    public static final String LANGUAGE = "uk";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        req.setAttribute(LOCALE, LANGUAGE);
        filterChain.doFilter(req, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

package controllers.login;

import constants.Links;
import controllers.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.ROLE;
import static constants.Keys.TOKEN;

@WebServlet(Links.LOGOUT)
public class LogoutControl extends Controller {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute(TOKEN, null);
        req.getSession().setAttribute(ROLE, null);
        resp.sendRedirect(req.getContextPath() + Links.LOGIN);
    }
}

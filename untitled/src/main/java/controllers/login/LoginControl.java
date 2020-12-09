package controllers.login;

import constants.ApiLinks;
import constants.Links;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.LOGIN;

@WebServlet(Links.LOGIN)
public class LoginControl extends HttpServlet {
    private static final String PAGE = "/pages/login/login.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(LOGIN, ApiLinks.LOGIN);
        req.getRequestDispatcher(PAGE).forward(req, resp);
    }
}

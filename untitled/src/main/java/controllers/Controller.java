package controllers;

import constants.Links;
import entity.Role;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.*;

public abstract class Controller extends AbstractController {
    private static final String CONTENT_SHELL = "/pages/contentShell.jsp";
    public void show(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        req.setAttribute(USER_LIST, Links.USERS);
        req.setAttribute(LOGOUT, Links.LOGOUT);
        req.getRequestDispatcher(CONTENT_SHELL).forward(req, response);
    }


}

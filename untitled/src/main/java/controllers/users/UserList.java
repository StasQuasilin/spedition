package controllers.users;

import constants.Links;
import controllers.Modal;
import entity.Role;
import entity.User;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.*;

@WebServlet(value = Links.USERS)
public class UserList extends Modal {

    private static final String _CONTENT = "/pages/users/userList.jsp";
    private static final String _TITLE = "title.users";
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(TITLE, _TITLE);
        req.setAttribute(CONTENT, _CONTENT);
        req.setAttribute(REGISTRATION, Links.REGISTRATION);
        req.setAttribute(DELETE, Links.USER_DELETE);
        final User user = getUser(req);
        req.setAttribute(USERS, userDAO.getUsersBySupervisor(user));

        show(req, resp);
    }
}

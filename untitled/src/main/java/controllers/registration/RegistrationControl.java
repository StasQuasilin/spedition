package controllers.registration;

import constants.ApiLinks;
import constants.Links;
import controllers.Modal;
import entity.Role;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.*;

@WebServlet(Links.REGISTRATION)
public class RegistrationControl extends Modal {

    private static final String _CONTENT = "/pages/registration/registration.jsp";
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(TITLE, "title.registration");
        req.setAttribute(CONTENT, _CONTENT);
        req.setAttribute(ROLES, Role.values());
        req.setAttribute(SUPERVISORS, userDAO.getUsersByRole(Role.supervisor));
        req.setAttribute(REGISTRATION, ApiLinks.REGISTRATION);
        show(req, resp);
    }
}

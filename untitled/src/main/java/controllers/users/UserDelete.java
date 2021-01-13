package controllers.users;

import constants.ApiLinks;
import constants.Links;
import controllers.Modal;
import entity.User;
import org.json.simple.JSONObject;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.*;

@WebServlet(value = Links.USER_DELETE)
public class UserDelete extends Modal {

    private static final String _TITLE = "title.user.delete";
    private static final String _CONTENT = "/pages/users/userDelete.jsp";
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        if(body != null){
            final Object id = body.get(ID);
            final User user = userDAO.getUserById(id);
            req.setAttribute(USER, user);
            req.setAttribute(TITLE, _TITLE);
            req.setAttribute(CONTENT, _CONTENT);
            req.setAttribute(DELETE, ApiLinks.USER_DELETE);
            show(req, resp);
        }
    }
}

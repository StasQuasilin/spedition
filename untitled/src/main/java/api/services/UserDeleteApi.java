package api.services;

import api.ServletAPI;
import constants.ApiLinks;
import entity.Answer;
import entity.ErrorAnswer;
import entity.SuccessAnswer;
import org.json.simple.JSONObject;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.ID;

@WebServlet(ApiLinks.USER_DELETE)
public class UserDeleteApi extends ServletAPI {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Object id = body.get(ID);
            userDAO.removeUser(id);
            answer = new SuccessAnswer();
        } else {
            answer = new ErrorAnswer("Empty body");
        }
        write(resp, answer);
    }
}

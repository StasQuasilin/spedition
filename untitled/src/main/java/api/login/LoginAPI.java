package api.login;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Links;
import entity.*;
import org.json.simple.JSONObject;
import utils.hibernate.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static constants.Keys.*;

@WebServlet(ApiLinks.LOGIN)
public class LoginAPI extends ServletAPI {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            System.out.println(body);
            String phone = String.valueOf(body.get(PHONE));
            final UserAccess access = userDAO.getUserAccessByPhone(phone);

            if (access != null){
                String password = String.valueOf(body.get(PASSWORD));
                if (access.getPassword().equals(password)){
                    answer = new SuccessAnswer();
                    if(body.containsKey(REDIRECT)){
                        final HttpSession session = req.getSession();
                        session.setAttribute(USER, access.getUser());
                        session.setAttribute(TOKEN, access.getToken());
                        session.setAttribute(ROLE, access.getUser().getRole());
                        answer.addParam(REDIRECT, req.getContextPath() + Links.REPORTS);
                    } else {
                        answer.addParam(TOKEN, access.getToken());
                        answer.addParam(USER, access.getUser().getPerson().getValue());
                    }
                } else {
                    answer = new ErrorAnswer(WRONG_PASSWORD);
                    answer.addParam(REASON, WRONG_PASSWORD);
                }
            } else {
                answer = new ErrorAnswer(NOT_FOUND);
                answer.addParam(REASON, NOT_FOUND);
            }
        } else {
            answer = new ErrorAnswer(EMPTY_BODY);
            answer.addParam(REASON, EMPTY_BODY);
        }
        write(resp, answer.toJson());
    }
}

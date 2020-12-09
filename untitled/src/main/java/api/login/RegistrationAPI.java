package api.login;

import api.ServletAPI;
import constants.ApiLinks;
import entity.*;
import org.json.simple.JSONObject;
import org.w3c.dom.UserDataHandler;
import utils.hibernate.dao.RegistrationDAO;
import utils.hibernate.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

import static constants.Keys.*;

@WebServlet(ApiLinks.REGISTRATION)
public class RegistrationAPI extends ServletAPI {

    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        if (body != null){
            System.out.println(body);
            String number = String.valueOf(body.get(PHONE));
            User user = registrationDAO.getUserByPhone(number);
            if (user == null){
                Person person = new Person();
                person.setForename(String.valueOf(body.get(FORENAME)));
                person.setSurname(String.valueOf(body.get(SURNAME)));
                person.setPatronymic(String.valueOf(body.get(PATRONYMIC)));

                final Role role = Role.valueOf(String.valueOf(body.get(ROLE)));
                User supervisor = null;
                if (role == Role.supervisor){
                    final Object token = req.getSession().getAttribute(TOKEN);
                    supervisor = userDAO.getUserByToken(token);
                } else if (role == Role.user){
                    final Object o = body.get(SUPERVISOR);
                    supervisor = userDAO.getUserById(o);
                }
                user = new User();
                user.setPerson(person);
                user.setRole(role);
                user.setSupervisor(supervisor);

                Phone phone = new Phone();
                phone.setPerson(person);
                phone.setNumber(number);

                UserAccess access = new UserAccess();
                access.setUser(user);
                access.setPassword(Base64.getEncoder().encodeToString(String.valueOf(body.get(PASSWORD)).getBytes()));
                access.setToken(java.util.UUID.randomUUID().toString());

                registrationDAO.registration(person, user, phone, access);
                write(resp, SUCCESS_ANSWER);

            } else {
                ErrorAnswer errorAnswer = new ErrorAnswer("Phone already used");
                write(resp, errorAnswer.toJson());
            }
        }
    }
}

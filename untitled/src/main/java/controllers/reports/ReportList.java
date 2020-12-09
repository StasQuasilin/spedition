package controllers.reports;

import api.socket.SubscribeType;
import constants.Keys;
import constants.Links;
import controllers.Controller;
import entity.Role;
import entity.User;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.*;

@WebServlet(Links.REPORTS)
public class ReportList extends Controller {

    private static final String _CONTENT = "/pages/reports/reportList.jsp";
    private static final String _CONTENT2 = "/pages/reports/reportList2.jsp";
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(TITLE, "title.report.show");
        req.setAttribute(SHOW, Links.SHOW_REPORT);
        req.setAttribute(Keys.MONTH_REPORT, Links.MONTH_REPORT);
        req.setAttribute(SUBSCRIBE, SubscribeType.reports);
        final Role role = getRole(req);
        if (role == Role.user){
            req.setAttribute(CONTENT, _CONTENT);
        } else {
            final User user = getUser(req);
            req.setAttribute(USERS, userDAO.getUsersBySupervisor(user));
            req.setAttribute(CONTENT, _CONTENT2);
        }
        show(req, resp);
    }
}

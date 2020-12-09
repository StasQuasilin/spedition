package api.reports;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Report;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.answers.Answer;
import utils.answers.SuccessAnswer;
import utils.hibernate.dao.ReportDAO;
import utils.hibernate.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(ApiLinks.ACTIVE_REPORTS)
public class GetActiveReports extends ServletAPI {

    private final ReportDAO reportDAO = new ReportDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        if (body != null){
            JSONArray array = new JSONArray();
            final List<Report> reports = reportDAO.getNotClosetReports(userDAO.getUserById(body.get(Keys.USER)));
            for (Report report : reports){
                array.add(report.toJson());
            }
            Answer answer = new SuccessAnswer();
            answer.addParam(Keys.REPORTS, reports);
        }
    }
}

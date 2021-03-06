package api.reports;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.reports.Report;
import entity.reports.ReportHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.answers.Answer;
import utils.answers.SuccessAnswer;
import utils.hibernate.dao.ReportDAO;
import utils.hibernate.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Keys.USER;

@WebServlet(ApiLinks.REPORTS)
public class GetReportsAPI extends ServletAPI {

    private final ReportDAO reportDAO = new ReportDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        if(body != null){
            System.out.println(body);
            JSONArray array = new JSONArray();
            final List<ReportHeader> reports = reportDAO.getReportsHeaders(userDAO.getUserById(body.get(USER)));
            for  (ReportHeader r : reports){
                array.add(r.toJson());
            }
            Answer answer = new SuccessAnswer();
            answer.addParam(Keys.REPORTS, answer);
            write(resp, answer);
        }
    }
}

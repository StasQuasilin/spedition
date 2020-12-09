package api.reports;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.ErrorAnswer;
import entity.Report;
import entity.SuccessAnswer;
import org.json.simple.JSONObject;
import utils.hibernate.dao.ReportDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.REPORT_REMOVE)
public class RemoveReportApi extends ServletAPI {

    private ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Report report = reportDAO.getReportByUUID(body.get(Keys.REPORT));
            if(report != null){
                answer = new SuccessAnswer();
                reportDAO.remove(report);
            } else {
                answer = new ErrorAnswer("Report not found");
            }

        } else {
            answer = new ErrorAnswer("Req body is empty");
        }
        write(resp, answer);
    }
}

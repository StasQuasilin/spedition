package api.reports;

import api.ServletAPI;
import api.socket.SubscribeType;
import api.socket.UpdateUtil;
import constants.ApiLinks;
import constants.Keys;
import entity.*;
import entity.reports.Report;
import org.json.simple.JSONObject;
import utils.hibernate.dao.ReportDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.REPORT_REMOVE)
public class RemoveReportApi extends ServletAPI {

    private ReportDAO reportDAO = new ReportDAO();
    private UpdateUtil updateUtil =new UpdateUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Report report = reportDAO.getReportByUUID(body.get(Keys.REPORT));
            if(report != null){
                answer = new SuccessAnswer();
                for (ReportField field : reportDAO.getFields(report)){
                    reportDAO.remove(field);
                }

                reportDAO.remove(report);
                final int id = report.getId();
                final User owner = report.getOwner();
                updateUtil.remove(SubscribeType.reports, id, owner);
                final User supervisor = owner.getSupervisor();
                if (supervisor != null){
                    updateUtil.remove(SubscribeType.reports, id, supervisor);
                }
            } else {
                answer = new ErrorAnswer("Report not found");
            }
        } else {
            answer = new ErrorAnswer("Req body is empty");
        }
        write(resp, answer);
    }
}

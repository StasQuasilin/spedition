package api.reports;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.Report;
import entity.SuccessAnswer;
import entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.hibernate.dao.ReportDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@WebServlet(ApiLinks.GET_REPORTS)
public class GetReportsByMonth extends ServletAPI {

    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        if(body != null){
            int month = Integer.parseInt(String.valueOf(body.get(Keys.MONTH)));
            int year = Integer.parseInt(String.valueOf(body.get(Keys.YEAR)));
            final LocalDate from = LocalDate.of(year, month, 1);
            final LocalDate to = from.plusMonths(1).minusDays(1);
            final HashMap<User, LinkedList<Report>> repo = new HashMap<>();
            for (Report r : reportDAO.getReports(Date.valueOf(from), Date.valueOf(to))){
                final User owner = r.getOwner();
                if(!repo.containsKey(owner)){
                    repo.put(owner, new LinkedList<>());
                }
                repo.get(owner).add(r);
            }
            final JSONObject json = new JSONObject();
            for (Map.Entry<User, LinkedList<Report>> entry : repo.entrySet()){
                final User key = entry.getKey();
                final LinkedList<Report> value = entry.getValue();
                Collections.sort(value);
                JSONArray array = new JSONArray();
                for (Report r : value){
                    array.add(r.toSimpleJson());
                }
                json.put(key.getPerson().getValue(), array);
            }
            Answer answer = new SuccessAnswer();
            answer.addParam(Keys.REPORTS, json);
            write(resp, answer);
        }
    }
}

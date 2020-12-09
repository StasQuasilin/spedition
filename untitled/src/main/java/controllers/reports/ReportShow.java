package controllers.reports;

import constants.Keys;
import constants.Links;
import controllers.Modal;
import entity.Report;
import entity.ReportField;
import entity.ReportNote;
import org.json.simple.JSONObject;
import utils.hibernate.dao.ReportDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static constants.Keys.*;

@WebServlet(Links.SHOW_REPORT)
public class ReportShow extends Modal {

    private static final String PAGE = "/pages/reports/reportShow.jsp";
    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final JSONObject body = parseBody(req);
        if (body != null){
            final Report report = reportDAO.getReport(body.get(ID));
            if(report != null){
                req.setAttribute(REPORT, report);
                final List<ReportField> fields = reportDAO.getFields(report);
                Collections.sort(fields);
                req.setAttribute(FIELDS, fields);
                final List<ReportNote> notes = reportDAO.getNotes(report);
                Collections.sort(notes);
                req.setAttribute(NOTES, notes);
                req.setAttribute(CONTENT, PAGE);
            }
        }
        req.setAttribute(Keys.REPORT_REMOVE, Links.REPORT_REMOVE);
        show(req, resp);
    }
}

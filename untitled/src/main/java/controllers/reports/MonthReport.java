package controllers.reports;

import constants.ApiLinks;
import constants.Keys;
import constants.Links;
import controllers.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.CONTENT;
import static constants.Keys.TITLE;

@WebServlet(Links.MONTH_REPORT)
public class MonthReport extends Controller {
    private static final String _CONTENT = "/pages/reports/monthReport.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(TITLE, "title.month.report");
        req.setAttribute(CONTENT, _CONTENT);
        req.setAttribute(Keys.GET_REPORTS, ApiLinks.GET_REPORTS);
        show(req, resp);
    }
}

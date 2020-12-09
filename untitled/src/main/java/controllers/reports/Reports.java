package controllers.reports;

import constants.Links;
import controllers.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static constants.Keys.CONTENT;

@WebServlet(Links.REPORTS2)
public class Reports extends Controller {
    private static final String PAGE = "/pages/reportList2.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(CONTENT, PAGE);
        show(req, resp);
    }
}

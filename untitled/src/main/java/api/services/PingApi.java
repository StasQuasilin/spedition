package api.services;

import api.ServletAPI;
import constants.ApiLinks;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.PING)
public class PingApi extends ServletAPI {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        write(resp, SUCCESS_ANSWER);
        System.out.println("Ping from " + req.getRemoteAddr());
    }
}

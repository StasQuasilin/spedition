package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Driver;
import org.json.simple.JSONArray;
import utils.answers.Answer;
import utils.answers.SuccessAnswer;
import utils.hibernate.dao.DriverDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.REFERENCES)
public class GetReferencesAPI extends ServletAPI {

    private static final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Answer answer = new SuccessAnswer();
        final JSONArray array = new JSONArray();
        for (Driver driver : driverDAO.getDrivers()){
            array.add(driver.toJson());
        }
        answer.addParam(Keys.DRIVERS, array);
        write(resp, answer);
    }
}

package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.Driver;
import entity.ErrorAnswer;
import entity.SuccessAnswer;
import org.json.simple.JSONObject;
import utils.hibernate.dao.DriverDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(ApiLinks.GET_DRIVER)
public class GetDriversAPI extends ServletAPI {

    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Object id = body.get(Keys.ID);

            final Driver driver = driverDAO.getDriver(id);
            if (driver != null){
                String uuid = driver.getUuid();
                if (uuid == null || uuid.length() < 36){
                    uuid = UUID.randomUUID().toString();
                    driver.setUuid(uuid);
                    driverDAO.save(driver);
                }
                answer = new SuccessAnswer();
                answer.addParam(Keys.DRIVER, driver.toJson());
            } else {
                answer = new ErrorAnswer("Driver not found");
            }
        } else {
            answer = new ErrorAnswer("Empty request body");
        }
        write(resp, answer);
    }
}

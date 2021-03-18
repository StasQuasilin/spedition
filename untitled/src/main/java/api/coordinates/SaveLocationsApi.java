package api.coordinates;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.ErrorAnswer;
import entity.SuccessAnswer;
import entity.coordinates.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.hibernate.Hibernator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import static constants.Keys.REPORT;
import static constants.Keys.TIME;

@WebServlet(ApiLinks.SAVE_LOCATIONS)
public class SaveLocationsApi extends ServletAPI {

    private final Hibernator hibernator = Hibernator.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            int report = Integer.parseInt(String.valueOf(body.get(REPORT)));
            for (Object o : (JSONArray)body.get(Keys.LOCATIONS)){
                Location location = new Location();
                location.setReport(report);
                JSONObject object = (JSONObject) o;
                location.setTimestamp(Timestamp.valueOf(String.valueOf(object.get(TIME))));
                location.setLatitude(Long.parseLong(String.valueOf(object.get(Keys.LATITUDE))));
                location.setLongitude(Long.parseLong(String.valueOf(object.get(Keys.LONGITUDE))));
                hibernator.save(location);
            }
            answer = new SuccessAnswer();
        } else {
            answer = new ErrorAnswer("No req body");
        }
        write(resp, answer);
    }
}

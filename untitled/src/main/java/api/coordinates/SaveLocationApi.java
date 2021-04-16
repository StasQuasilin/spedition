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
import utils.JsonParser;
import utils.hibernate.Hibernator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import static constants.Keys.TIME;

@WebServlet(ApiLinks.SAVE_LOCATION)
public class SaveLocationApi extends ServletAPI {

    final Hibernator hibernator = Hibernator.getInstance();
    private final JsonParser parser = new JsonParser();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            System.out.println(body);
            if (body.containsKey(Keys.DATA)){
                for (Object o : (JSONArray)body.get(Keys.DATA)){
                    saveLocation(parser.parse(String.valueOf(o)));
                }
            }
            answer = new SuccessAnswer();
        } else {
            answer = new ErrorAnswer("No req body");
        }
        write(resp, answer);
    }

    private void saveLocation(JSONObject body) {
        Location location = new Location();
        location.setReport(String.valueOf(body.get(Keys.REPORT)));
        location.setTimestamp(new Timestamp(Long.parseLong(String.valueOf(body.get(TIME)))));
        location.setLatitude(Float.parseFloat(String.valueOf(body.get(Keys.LATITUDE))));
        location.setLongitude(Float.parseFloat(String.valueOf(body.get(Keys.LONGITUDE))));
        location.setSpeed(Float.parseFloat(String.valueOf(body.get(Keys.SPEED))));
        hibernator.save(location);
    }
}

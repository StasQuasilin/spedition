package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.hibernate.dao.CounterpartyDAO;
import utils.hibernate.dao.DriverDAO;
import utils.hibernate.dao.ProductDAO;
import utils.hibernate.dao.ReferencesDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

@WebServlet(ApiLinks.SYNC_REFERENCES)
public class SyncReferencesAPI extends ServletAPI {

    private final ProductDAO productDAO = new ProductDAO();
    private final DriverDAO driverDAO = new DriverDAO();
    private final CounterpartyDAO counterpartyDAO = new CounterpartyDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            System.out.println(body);

            answer = new SuccessAnswer();
            answer.addParam(Keys.PRODUCTS, buildArray(body,Keys.PRODUCTS, productDAO));
            answer.addParam(Keys.DRIVERS, buildArray(body,Keys.DRIVERS, driverDAO));
            answer.addParam(Keys.COUNTERPARTY, buildArray(body, Keys.COUNTERPARTY, counterpartyDAO));
            write(resp, answer);
        }
    }

    JSONArray buildArray(JSONObject json, String keyWord, ReferencesDAO dao){
        final Object c = json.get(keyWord);
        int[] list;
        if(c == null){
            list = dao.getAllId();
        } else {
            long time = Long.parseLong(String.valueOf(c));
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            timestamp.setTime(time);
            list = dao.getIdAfter(timestamp);
        }
        JSONArray array = new JSONArray();

        Collections.addAll(array, list);
        return array;
    }
}


package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.*;
import entity.references.ReferenceAction;
import entity.references.ReferenceItem;
import entity.references.ReferenceType;
import entity.references.ReferencesItemAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.hibernate.dao.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(ApiLinks.SYNC_REFERENCES)
public class SyncReferencesAPI extends ServletAPI {

    private final ProductDAO productDAO = new ProductDAO();
    private final DriverDAO driverDAO = new DriverDAO();
    private final CounterpartyDAO counterpartyDAO = new CounterpartyDAO();
    private final ReferencesItemDAO referencesItemDAO = new ReferencesItemDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            System.out.println(body);

            final String version = (String)body.get(Keys.VERSION);
            final String token = req.getHeader(Keys.TOKEN);
            userDAO.setVersionCode(token, version);

            answer = new SuccessAnswer();
            answer.addParam(Keys.PRODUCTS, buildArray(body, Keys.PRODUCTS, productDAO));
            answer.addParam(Keys.DRIVERS, buildArray(body, Keys.DRIVERS, driverDAO));
            answer.addParam(Keys.COUNTERPARTY, buildArray(body, Keys.COUNTERPARTY, counterpartyDAO));
            answer.addParam(Keys.P, buildObject(body, Keys.PRODUCTS, ReferenceType.product));
            answer.addParam(Keys.D, buildObject(body, Keys.DRIVERS, ReferenceType.driver));
            answer.addParam(Keys.C, buildObject(body, Keys.COUNTERPARTY, ReferenceType.counterparty));
            write(resp, answer);
        }
    }

    private JSONObject buildObject(JSONObject body, String key, ReferenceType type) {
        JSONArray update = new JSONArray();
        JSONArray remove = new JSONArray();
        Timestamp time = null;
        final Object o = body.get(key);
        if (o != null){
            final long l = Long.parseLong(String.valueOf(o));
            time = Timestamp.valueOf(LocalDateTime.now());
            time.setTime(l);
        }
        for (ReferencesItemAction action : referencesItemDAO.getActions(time, type)){
            if (action.getAction() == ReferenceAction.update){
                update.add(action.getItemId());
            } else {
                remove.add(action.getItemId());
            }
        }
        JSONObject object = new JSONObject();
        object.put(Keys.UPDATE, update);
        object.put(Keys.REMOVE, remove);
        return object;
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
        for (int i : list){
            array.add(i);
        }
        return array;
    }
}


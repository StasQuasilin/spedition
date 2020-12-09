package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.Counterparty;
import entity.ErrorAnswer;
import entity.SuccessAnswer;
import org.json.simple.JSONObject;
import utils.hibernate.dao.CounterpartyDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.GET_COUNTERPARTY)
public class GetCounterpartyAPI extends ServletAPI {

    private final CounterpartyDAO counterpartyDAO = new CounterpartyDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Object id = body.get(Keys.ID);
            final Counterparty counterparty = counterpartyDAO.getCounterparty(id);
            if(counterparty != null){
                answer = new SuccessAnswer();
                answer.addParam(Keys.COUNTERPARTY, counterparty);
            } else {
                answer = new ErrorAnswer("Counterparty " + id + " not found");
            }
        } else {
            answer = new ErrorAnswer("Empty req body");
        }
        write(resp, answer);
    }
}

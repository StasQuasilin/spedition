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

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

@WebServlet(ApiLinks.SYNC_REFERENCES)
public class SyncReferences extends ServletAPI {

    private final ProductDAO productDAO = new ProductDAO();
    private final DriverDAO driverDAO = new DriverDAO();
    private final CounterpartyDAO counterpartyDAO = new CounterpartyDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            System.out.println(body);

            final Object o = body.get(Keys.PRODUCTS);

            LinkedList<Product> products = new LinkedList<>();
            if (o == null){
                products.addAll(productDAO.getProducts());
            }

            final Object d = body.get(Keys.DRIVERS);
            final LinkedList<Driver> drivers = new LinkedList<>();
            if (d == null){
                drivers.addAll(driverDAO.getDrivers());
            }

            final Object c = body.get(Keys.COUNTERPARTY);
            final LinkedList<Counterparty> counterparties = new LinkedList<>();
            if(d == null){
                counterparties.addAll(counterpartyDAO.getAll());
            }

            answer = new SuccessAnswer();
            final JSONArray productArray = new JSONArray();
            for (Product product : products){
                productArray.add(product.getId());
            }
            final JSONArray driverArray = new JSONArray();
            for (Driver driver : drivers){
                driverArray.add(driver.getId());
            }
            final JSONArray counterpartyArray = new JSONArray();
            for (Counterparty counterparty : counterparties){
                counterpartyArray.add(counterparty.getId());
            }
            answer.addParam(Keys.PRODUCTS, productArray);
            answer.addParam(Keys.DRIVERS, driverArray);
            answer.addParam(Keys.COUNTERPARTY, counterparties);
            write(resp, answer);

        }
    }
}


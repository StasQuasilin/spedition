package api.references;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.Answer;
import entity.ErrorAnswer;
import entity.Product;
import entity.SuccessAnswer;
import org.json.simple.JSONObject;
import utils.hibernate.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ApiLinks.GET_PRODUCT)
public class GetProductApi extends ServletAPI {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final JSONObject body = parseBody(req);
        Answer answer;
        if (body != null){
            final Product product = productDAO.getProduct(body.get(Keys.ID));
            if (product != null){
                answer = new SuccessAnswer();
                answer.addParam(Keys.PRODUCT, product.toJson());
            } else {
                answer = new ErrorAnswer("Product not found");
            }
        } else {
            answer = new ErrorAnswer("Request body is empty");
        }
        write(resp, answer);
    }
}

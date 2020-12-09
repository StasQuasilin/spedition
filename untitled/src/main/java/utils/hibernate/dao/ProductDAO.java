package utils.hibernate.dao;

import entity.Product;
import utils.hibernate.Hibernator;

import java.util.Collection;
import java.util.List;

import static constants.Keys.ID;

public class ProductDAO {
    private static final Hibernator hibernator = Hibernator.getInstance();

    public Product getProduct(Object id) {
        return hibernator.get(Product.class, ID, id);
    }

    public List<Product> getProducts() {
        return hibernator.query(Product.class, null);
    }
}

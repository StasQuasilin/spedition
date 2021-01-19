package utils.hibernate.dao;

import entity.Product;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static constants.Keys.ID;

public class ProductDAO implements ReferencesDAO {
    private static final Hibernator hibernator = Hibernator.getInstance();

    public Product getProduct(Object id) {
        return hibernator.get(Product.class, ID, id);
    }

    public List<Product> getProducts() {
        return hibernator.query(Product.class, null);
    }

    @Override
    public int[] getAllId() {
        return build(getProducts());
    }

    private int[] build(List<Product> products) {
        int[] ints = new int[products.size()];
        for (int i = 0; i < products.size(); i++){
            ints[i] = products.get(i).getId();
        }
        return ints;
    }

    @Override
    public int[] getIdAfter(Timestamp t) {
        return build(getProductsAfter(t));
    }

    private List<Product> getProductsAfter(Timestamp t) {
        return getProducts();
    }
}

package ua.svasilina.spedition.entity.reports;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.LinkedList;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.utils.ProductsUtil;
import ua.svasilina.spedition.utils.db.DriverUtil;
import ua.svasilina.spedition.utils.db.JsonObject;

public class SimpleReport extends IReport implements Comparable<SimpleReport>{

    private final LinkedList<Product> products = new LinkedList<>();
    private final LinkedList<Driver> drivers = new LinkedList<>();


    public SimpleReport(JsonObject parse, Context context) {
        super(parse);
        ProductsUtil productsUtil = new ProductsUtil(context);
        for (int i : parse.getIntArray(Keys.PRODUCTS)){
            products.add(productsUtil.getProduct(i));
        }
        DriverUtil driverUtil = new DriverUtil(context);
        for (Object o : parse.getJsonArray(Keys.DRIVERS)){
            final JsonObject object = new JsonObject((JSONObject) o);

            drivers.add(driverUtil.getDriver(object.getString(Keys.DRIVER)));
        }
    }

    public SimpleReport() {

    }

    @Override
    public LinkedList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public LinkedList<Driver> getDrivers() {
        return drivers;
    }

    @Override
    public int compareTo(@NotNull SimpleReport o) {
        if (o.doneDate != null && doneDate != null){
            return o.doneDate.compareTo(doneDate);
        } else if(o.doneDate != null){
            return -1;
        } else if (doneDate != null){
            return 1;
        } else if (o.leaveTime != null && leaveTime != null){
            return o.leaveTime.compareTo(leaveTime);
        } else if (o.leaveTime == null){
            return 1;
        } else {
            return -1;
        }
    }
}

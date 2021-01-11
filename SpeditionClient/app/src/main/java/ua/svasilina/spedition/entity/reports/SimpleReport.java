package ua.svasilina.spedition.entity.reports;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Product;

public class SimpleReport extends IReport implements Comparable<SimpleReport>{

    private LinkedList<Product> products;
    private LinkedList<Driver> drivers;

    public SimpleReport() {
        products = new LinkedList<>();
        drivers = new LinkedList<>();
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
        } else if(o.doneDate == null){
            return 1;
        } else if (doneDate == null){
            return -1;
        } else if (o.leaveTime != null && leaveTime == null){
            return o.leaveTime.compareTo(leaveTime);
        } else if (o.leaveTime == null){
            return 1;
        } else {
            return -1;
        }
    }
}

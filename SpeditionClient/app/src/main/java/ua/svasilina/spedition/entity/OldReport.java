package ua.svasilina.spedition.entity;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ua.svasilina.spedition.utils.changes.IChanged;

import static ua.svasilina.spedition.constants.Keys.DONE;
import static ua.svasilina.spedition.constants.Keys.DRIVER;
import static ua.svasilina.spedition.constants.Keys.EXPENSES;
import static ua.svasilina.spedition.constants.Keys.FARES;
import static ua.svasilina.spedition.constants.Keys.FIELDS;
import static ua.svasilina.spedition.constants.Keys.FONE;
import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.LEAVE;
import static ua.svasilina.spedition.constants.Keys.NOTES;
import static ua.svasilina.spedition.constants.Keys.PER_DIEM;
import static ua.svasilina.spedition.constants.Keys.PRODUCT;
import static ua.svasilina.spedition.constants.Keys.ROUTE;
import static ua.svasilina.spedition.constants.Keys.WEIGHT;

public class OldReport implements Serializable, Comparable<OldReport>, IChanged, JsonAble {

    private String uuid;
    public Calendar leaveTime;
    private Calendar doneDate;
    private Driver driver;
    public Route route;
    private Product product;
    private Weight weight;
    private int perDiem;
    final public ArrayList<ReportField> fields = new ArrayList<>();
    final public ArrayList<Expense> expenses = new ArrayList<>();
    final public ArrayList<Expense> fares = new ArrayList<>();
    final public ArrayList<ReportNote> notes = new ArrayList<>();
    private boolean fone;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Calendar getLeaveTime() {
        return leaveTime;
    }
    public void setLeaveTime(Calendar leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Calendar getDoneDate() {
        return doneDate;
    }
    public void setDoneDate(Calendar doneDate) {
        this.doneDate = doneDate;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Route getRoute() {
        return route;
    }
    public void setRoute(Route route) {
        this.route = route;
    }

    public Weight getWeight() {
        return weight;
    }
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public boolean isDone() {
        return doneDate != null;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }
    public ArrayList<ReportNote> getNotes() {
        return notes;
    }
    public ArrayList<Expense> getFares() {
        return fares;
    }

    public int getPerDiem() {
        return perDiem;
    }
    public void setPerDiem(int perDiem) {
        this.perDiem = perDiem;
    }

    public boolean isFone() {
        return fone;
    }
    public void setFone(boolean fone) {
        this.fone = fone;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, uuid);
        if (leaveTime != null) {
            json.put(LEAVE, leaveTime.getTimeInMillis());
        }
        if(doneDate != null){
            json.put(DONE, doneDate.getTimeInMillis());
        }
        if (driver != null && driver.anyChanges()){
            json.put(DRIVER, driver.toJson());
        }
        if (route != null){
            json.put(ROUTE, route.toJson());
        }
        if (product != null){
            json.put(PRODUCT, product.getId());
        }
        if (weight != null){
            json.put(WEIGHT, weight.toJson());
        }
        json.put(FIELDS, fields());
        json.put(FARES, fare());
        json.put(EXPENSES, expenses());
        json.put(PER_DIEM, perDiem);
        json.put(FONE, fone);
        json.put(NOTES, notes());

        return json;
    }

    private JSONArray fare() {
        JSONArray array = new JSONArray();
        for (Expense fare : fares){
            array.add(fare.toJson());
        }
        return array;
    }

    private JSONArray notes() {
        JSONArray array = new JSONArray();
        for (ReportNote note : notes){
            array.add(note.toJson());
        }
        return array;
    }

    private JSONArray expenses() {
        JSONArray array = new JSONArray();
        for (Expense expense : expenses){
            array.add(expense.toJson());
        }
        return array;
    }

    private JSONArray fields() {
        JSONArray array = new JSONArray();
        for (ReportField field : fields){
            array.add(field.toJson());
        }
        return array;
    }

    public List<ReportField> getFields() {
        return fields;
    }

    @Override
    public int compareTo(@NotNull OldReport o) {
        if (leaveTime == null){
            return -1;
        } else if (o.leaveTime == null){
            return 1;
        } else {
            return o.leaveTime.compareTo(leaveTime);
        }
    }

    public void addField(ReportField field){
        fields.add(field);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addFare(Expense fare) {
        fares.add(fare);
    }

    @Override
    public HashMap<String, Object> getValues(String key) {
        HashMap<String, Object> values = new HashMap<>();
        if (leaveTime != null){
            values.put(LEAVE, leaveTime.getTimeInMillis());
        } else {
            values.put(LEAVE, null);
        }
        if (doneDate != null){
            values.put(DONE, doneDate.getTimeInMillis());
        } else {
            values.put(DONE, null);
        }


        return values;
    }
}

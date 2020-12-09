package ua.svasilina.spedition.entity.reports;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Expense;
import ua.svasilina.spedition.entity.JsonAble;
import ua.svasilina.spedition.entity.OldReport;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.ReportNote;
import ua.svasilina.spedition.utils.changes.IChanged;

import static ua.svasilina.spedition.constants.Keys.COMA;
import static ua.svasilina.spedition.constants.Keys.DONE;
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
import static ua.svasilina.spedition.constants.Keys.UUID;

public class Report extends IReport implements JsonAble, Serializable, Comparable<Report>, IChanged {

    private int serverId = -1;
    private final LinkedList<ReportDetail> details;
    private Product product;
    private boolean separatedProducts;
    private int perDiem;
    final public ArrayList<ReportField> fields = new ArrayList<>();
    final public ArrayList<Expense> expenses = new ArrayList<>();
    final public ArrayList<Expense> fares = new ArrayList<>();
    final public ArrayList<ReportNote> notes = new ArrayList<>();
    private boolean fone;

    public Report() {
        details = new LinkedList<>();
    }

    public Report(OldReport report) {
        this();
        setUuid(report.getUuid());
        leaveTime = report.leaveTime;
        doneDate = report.getDoneDate();
        for (String route : report.getRoute().getPoints()){
            addRoute(route);
        }
        final Driver driver = report.getDriver();
        if (driver != null) {
            ReportDetail detail = new ReportDetail();
            detail.setDriver(driver);
            detail.setOwnWeight(report.getWeight());
        }

        product = report.getProduct();
        perDiem = report.getPerDiem();
        fields.addAll( report.fields);
        expenses.addAll(report.expenses);
        fares.addAll(report.fares);
        notes.addAll(report.notes);
        fone = report.isFone();
    }

    public int getServerId() {
        return serverId;
    }
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public LinkedList<ReportDetail> getDetails() {
        return details;
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

    private final LinkedList<Product> products = new LinkedList<>();
    @Override
    public LinkedList<Product> getProducts() {
        products.clear();
        products.add(product);
        return products;
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
        JSONObject json  = new JSONObject();
        json.put(ID, serverId);
        json.put(UUID, getUuid());
        if (leaveTime != null) {
            json.put(LEAVE, leaveTime.getTimeInMillis());
        }
        if(doneDate != null){
            json.put(DONE, doneDate.getTimeInMillis());
        }
        if (route != null){
            json.put(ROUTE, route);
        }
        json.put(Keys.DETAILS, details());
        if (product != null){
            json.put(PRODUCT, product.getId());
        }
        json.put(FIELDS, fields());
        json.put(FARES, fare());
        json.put(EXPENSES, expenses());
        json.put(PER_DIEM, perDiem);
        json.put(FONE, fone);
        json.put(NOTES, notes());

        return json;
    }

    private JSONArray details() {
        JSONArray array = new JSONArray();
        for (ReportDetail detail : details){
            array.add(detail.toJson());
        }
        return array;
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
    public int compareTo(@NotNull Report o) {
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

    public String getRouteString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String s : route){
            builder.append(s);
            if (i < route.size() - 1){
                builder.append(COMA);
            }
            i++;
        }
        return builder.toString();
    }

    public void addDetail(ReportDetail reportDetail) {
        details.add(reportDetail);
    }
}

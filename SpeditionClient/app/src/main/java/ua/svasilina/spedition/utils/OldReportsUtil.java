package ua.svasilina.spedition.utils;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Expense;
import ua.svasilina.spedition.entity.OldReport;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.ReportNote;
import ua.svasilina.spedition.entity.Route;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.utils.changes.ChangeUtil;
import ua.svasilina.spedition.utils.db.ReportUtil;
import ua.svasilina.spedition.utils.sync.SyncListUtil;
import ua.svasilina.spedition.utils.sync.SyncUtil;

import static ua.svasilina.spedition.constants.Keys.AMOUNT;
import static ua.svasilina.spedition.constants.Keys.ARRIVE;
import static ua.svasilina.spedition.constants.Keys.COUNTERPARTY;
import static ua.svasilina.spedition.constants.Keys.DESCRIPTION;
import static ua.svasilina.spedition.constants.Keys.DONE;
import static ua.svasilina.spedition.constants.Keys.DRIVER;
import static ua.svasilina.spedition.constants.Keys.EXPENSES;
import static ua.svasilina.spedition.constants.Keys.FARES;
import static ua.svasilina.spedition.constants.Keys.FIELDS;
import static ua.svasilina.spedition.constants.Keys.FONE;
import static ua.svasilina.spedition.constants.Keys.GROSS;
import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.LEAVE;
import static ua.svasilina.spedition.constants.Keys.MONEY;
import static ua.svasilina.spedition.constants.Keys.NOTE;
import static ua.svasilina.spedition.constants.Keys.NOTES;
import static ua.svasilina.spedition.constants.Keys.PER_DIEM;
import static ua.svasilina.spedition.constants.Keys.PHONES;
import static ua.svasilina.spedition.constants.Keys.PRODUCT;
import static ua.svasilina.spedition.constants.Keys.ROUTE;
import static ua.svasilina.spedition.constants.Keys.TARE;
import static ua.svasilina.spedition.constants.Keys.TIME;
import static ua.svasilina.spedition.constants.Keys.WEIGHT;

public class OldReportsUtil {

    private static final String TAG = "ReportsUtil";
    private final JSONParser parser = new JSONParser();
    private StorageUtil storageUtil;
    private ProductsUtil productsUtil;
    private SyncListUtil syncListUtil;
    private static final int STORAGE_SIZE = 20;

    private static final String reportsDir = "report_";
    private final FileFilter fileFilter;
    private final Context context;
    private final SyncUtil syncUtil;
    private final ChangeUtil changeUtil;

    public OldReportsUtil(Context context) {
        storageUtil = new StorageUtil(context);
        fileFilter = new FileFilter(reportsDir);
        this.context = context;
        syncUtil = new SyncUtil(context, new ReportUtil(context));
        changeUtil = new ChangeUtil(context);
        syncListUtil = new SyncListUtil(context);
        productsUtil = new ProductsUtil(context);
    }

    public Context getContext() {
        return context;
    }

    public void saveReport(final OldReport report){
        String uuid = report.getUuid();
        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            report.setUuid(uuid);
        }

        final String fileName = reportsDir + uuid;
        final JSONObject jsonObject = report.toJson();
        final String data = jsonObject.toJSONString();

        storageUtil.saveData(fileName, data);
//        syncUtil.saveReport(report, true);
    }

    private OldReport parseReport(String data, ReportDetail detailed){

        OldReport report = null;
        if (data != null && !data.isEmpty()) {
            try {
                final JSONObject parse = (JSONObject) parser.parse(data);
                report = new OldReport();
                report.setUuid(String.valueOf(parse.get(ID)));

                if (parse.containsKey(DRIVER)) {
                    final JSONObject driverJson = (JSONObject) parse.get(DRIVER);
                    final Driver driver = Driver.fromJson(driverJson);
                    report.setDriver(driver);
                    if (driverJson != null) {
                        if (detailed == ReportDetail.full && driverJson.containsKey(PHONES)) {
                            final Object o = driverJson.get(PHONES);
                            if (o != null) {
                                for (Object p : (JSONArray) o) {
                                    driver.addPhone(String.valueOf(p));
                                }
                            }
                        }
                    }
                }
                if (parse.containsKey(LEAVE)) {
                    final Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(Long.parseLong(String.valueOf(parse.get(LEAVE))));
                    report.setLeaveTime(instance);
                }
                if (parse.containsKey(DONE)) {
                    final Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(Long.parseLong(String.valueOf(parse.get(DONE))));
                    report.setDoneDate(instance);
                }
                if (parse.containsKey(ROUTE)) {
                    Route route = new Route();
                    JSONObject routeJson = (JSONObject) parse.get(ROUTE);
                    if (routeJson != null) {
                        final JSONArray routeArray = (JSONArray) routeJson.get(ROUTE);
                        if (routeArray != null) {
                            for (Object o : routeArray) {
                                String point = String.valueOf(o);
                                route.addPoint(point);
                            }
                        }
                    }
                    report.setRoute(route);
                }
                if (parse.containsKey(PRODUCT)) {
                    int productId = Integer.parseInt(String.valueOf(parse.get(PRODUCT)));
                    report.setProduct(productsUtil.getProduct(productId));
                }
                if (detailed == ReportDetail.full) {

                    if (parse.containsKey(WEIGHT)) {
                        final JSONObject weightJson = (JSONObject) parse.get(WEIGHT);
                        if (weightJson != null) {
                            report.setWeight(parseWeight(weightJson));
                        }
                    }

                    if (parse.containsKey(EXPENSES)) {
                        final Object o = parse.get(EXPENSES);
                        if (o != null)
                            parseExpenses(report.getExpenses(), (JSONArray) o);
                    }

                    if (parse.containsKey(PER_DIEM)) {
                        int perDiem = Integer.parseInt(String.valueOf(parse.get(PER_DIEM)));
                        report.setPerDiem(perDiem);
                    }

                    if (parse.containsKey(FONE)) {
                        boolean fone = Boolean.parseBoolean(String.valueOf(parse.get(FONE)));
                        report.setFone(fone);
                    }

                    if (parse.containsKey(FIELDS)) {
                        final Object o = parse.get(FIELDS);
                        if (o != null)
                            parseFields(report, (JSONArray) o);
                    }

                    if (parse.containsKey(NOTES)) {
                        final Object o = parse.get(NOTES);
                        if (o != null) {
                            parseNotes(report, (JSONArray) o);
                        }
                    }

                    if (parse.containsKey(FARES)) {
                        final Object o = parse.get(FARES);
                        if (o != null) {
                            parseExpenses(report.getFares(), (JSONArray) o);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return report;
    }

    private void parseNotes(OldReport report, JSONArray array) {
        for (Object o : array){
            JSONObject json = (JSONObject) o;
            ReportNote note = new ReportNote();
            note.setUuid(String.valueOf(json.get(ID)));
            final Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(Long.parseLong(String.valueOf(json.get(TIME))));
            note.setTime(instance);
            note.setNote(String.valueOf(json.get(NOTE)));
            report.getNotes().add(note);
        }
    }

    private void parseFields(OldReport report, JSONArray fieldsArray) {
        for (Object o :  fieldsArray) {
            JSONObject field = (JSONObject) o;
            ReportField reportField = new ReportField();

            reportField.setUuid(String.valueOf(field.get(ID)));

            if (field.containsKey(ARRIVE)) {
                final long arrive = Long.parseLong(String.valueOf(field.get(ARRIVE)));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(arrive);
                reportField.setArriveTime(calendar);
            }
            if (field.containsKey(LEAVE)){
                final long leave = Long.parseLong(String.valueOf(field.get(LEAVE)));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(leave);
                reportField.setLeaveTime(calendar);
            }

            if (field.containsKey(PRODUCT)){
                reportField.setProduct(productsUtil.getProduct(Integer.parseInt(String.valueOf(field.get(PRODUCT)))));
            }

            if (field.containsKey(COUNTERPARTY)) {
                String counterparty = String.valueOf(field.get(COUNTERPARTY));
//                reportField.setCounterparty(counterparty);
            }

            if (field.containsKey(MONEY)) {
                int money = Integer.parseInt(String.valueOf(field.get(MONEY)));
                reportField.setMoney(money);
            }

            if (field.containsKey(WEIGHT)) {

                final JSONObject weightJson = (JSONObject) field.get(WEIGHT);
                if (weightJson != null) {
                    reportField.setWeight(parseWeight(weightJson));
                }
            }
            report.addField(reportField);
        }
    }

    private Weight parseWeight(JSONObject weightJson) {
        Weight weight = new Weight();
        weight.setGross((int) Float.parseFloat(String.valueOf(weightJson.get(GROSS))));
        weight.setTare((int) Float.parseFloat(String.valueOf(weightJson.get(TARE))));
        return weight;
    }

    private void parseExpenses(ArrayList<Expense> list, JSONArray expensesArray) {

        for (Object o : expensesArray){
            JSONObject json = (JSONObject) o;
            Expense expense = new Expense();
            expense.setUuid(String.valueOf(json.get(ID)));
            expense.setDescription(String.valueOf(json.get(DESCRIPTION)));
            expense.setAmount(Integer.parseInt(String.valueOf(json.get(AMOUNT))));
            list.add(expense);
        }
    }

    public List<OldReport> readStorage(){
        return readStorage(ReportDetail.info);
    }

    public List<OldReport> readStorage(ReportDetail detail){
        List<OldReport> reports = new ArrayList<>();
        final File[] files = storageUtil.getFiles(fileFilter);
        if (files != null) {
            for (File file : files) {
                final String s = storageUtil.readFile(file.getName());
                OldReport report = parseReport(s, detail);
                if (report != null){
                    reports.add(report);
                }
            }
        }
        Collections.sort(reports);
        while (reports.size() > STORAGE_SIZE){
            final OldReport report = reports.get(reports.size() - 1);
            if (report.isDone()){
                reports.remove(report);
                storageUtil.remove(reportsDir + report.getUuid());
            }
        }

        return reports;
    }

    public OldReport openReport(String uuid) {
        final String s = storageUtil.readFile(reportsDir + uuid);
        if (s != null) {
            return parseReport(s, ReportDetail.full);
        }
        return null;
    }

    public void sync() {
//        Log.i(TAG, "Sync Storage");
//        Toast.makeText(context, R.string.sync, Toast.LENGTH_LONG).show();
        syncUtil.sync();
    }

    public void removeReport(String uuid) {
        storageUtil.remove(reportsDir + uuid);
    }
}

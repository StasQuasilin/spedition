package api.reports;

import api.ServletAPI;
import constants.ApiLinks;
import constants.Keys;
import entity.*;
import entity.reports.CounterpartyWeight;
import entity.reports.ReportDetails;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.hibernate.dao.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static constants.Keys.*;

@WebServlet(ApiLinks.REPORT_SAVE)
public class SaveReportAPI extends ServletAPI {

    private final UserDAO userDAO = new UserDAO();
    private final ReportDAO reportDAO = new ReportDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final DriverDAO driverDAO = new DriverDAO();
    private final CounterpartyDAO counterpartyDAO = new CounterpartyDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject body = parseBody(req);
        Answer answer;
        if (body != null) {
            System.out.println("!" + body);
            final String header = req.getHeader(TOKEN);
            final User user = userDAO.getUserByToken(header);
            final Object uuid = body.get(UUID);
            Report report = reportDAO.getReportByUUID(uuid);

            if (report == null){
                report = new Report();
                report.setOwner(user);
                report.setUuid(uuid.toString());
            }

            if (body.containsKey(LEAVE)) {
                Timestamp leave = new Timestamp(Long.parseLong(String.valueOf(body.get(LEAVE))));
                report.setLeaveTime(leave);
            }

            if (body.containsKey(DONE)){
                Timestamp done = new Timestamp(Long.parseLong(String.valueOf(body.get(DONE))));
                report.setDone(done);
            }

            final Product product = productDAO.getProduct(body.get(PRODUCT));
            report.setProduct(product);

            if (body.containsKey(ROUTE)) {
                JSONArray routeArray = null;
                try {
                    JSONObject route = (JSONObject) body.get(ROUTE);
                    if (route.containsKey(ROUTE)) {
                        routeArray = (JSONArray) route.get(ROUTE);
                    }
                } catch (Exception ignore){
                    routeArray = (JSONArray) body.get(ROUTE);
                }
                StringBuilder stringBuilder = new StringBuilder();
                if (routeArray != null) {
                    int i = 0;
                    for (Object o : routeArray) {
                        stringBuilder.append(o);
                        if (i < routeArray.size() - 1) {
                            stringBuilder.append(COMA);
                        }
                        i++;
                    }
                    report.setRoute(stringBuilder.toString());
                } else {
                    report.setRoute(String.valueOf(body.get(ROUTE)));
                }
            }
            
            int perDiem = Integer.parseInt(String.valueOf(body.get(PER_DIEM)));
            report.setPerDiem(perDiem);

            reportDAO.save(report);
            if (body.containsKey(Keys.DETAILS)){
                saveDetails(report, (JSONArray)body.get(DETAILS));
            } else {
                final Set<ReportDetails> details = report.getDetails();
                ReportDetails reportDetails = null;
                if (details != null && details.size() > 0){
                    for (ReportDetails d : details){
                        reportDetails = d;
                        break;
                    }
                } else {
                    reportDetails = new ReportDetails();
                    reportDetails.setReport(report);
                }

                if(body.containsKey(DRIVER)){
                    reportDetails.setDriver(extractDriver((JSONObject) body.get(DRIVER)));
                } else {
                    reportDetails.setDriver(null);
                }
                if(body.containsKey(WEIGHT)){
                    final Object o = body.get(WEIGHT);
                    if (o != null){
                        Weight weight = reportDetails.getWeight();
                        if (weight == null){
                            weight = new Weight();
                            reportDetails.setWeight(weight);
                        }
                        parseWeight(weight, (JSONObject)o);
                    }
                } else {
                    reportDetails.setWeight(null);
                }
                reportDAO.save(reportDetails);
            }
            saveFields(report, (JSONArray) body.get(FIELDS));
            saveExpenses(report, report.getFares(), (JSONArray) body.get(FARES), ExpenseType.fare);
            saveExpenses(report, report.getExpenses(), (JSONArray) body.get(EXPENSES), ExpenseType.expense);
            saveNotes(report, (JSONArray) body.get(NOTES));

            answer = new SuccessAnswer();
            answer.addParam(ID, report.getId());
            write(resp, answer.toJson());
            reportDAO.afterSave(report);
        }
    }

    private Driver extractDriver(JSONObject driverJson) {
        Driver driver = driverDAO.getDriverByUUID(driverJson.get(ID));
        int hash = -1;
        if (driver == null){
            driver = new Driver();
            driver.setPerson(new Person());
        } else {
            hash = driver.hashCode();
        }
        driver.setUuid(String.valueOf(driverJson.get(ID)));
        final Person person = driver.getPerson();

        person.setSurname(String.valueOf(driverJson.get(SURNAME)));
        person.setForename(String.valueOf(driverJson.get(FORENAME)));

        if (hash != driver.hashCode()){
            driverDAO.save(driver);
        }

        final HashMap<String, Phone> phones = new HashMap<>();
        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                phones.put(phone.getNumber(), phone);
            }
        }
        final Object o = driverJson.get(PHONES);
        if (o != null){
            for (Object p : (JSONArray)o){
                String number = String.valueOf(p);
                Phone phone = phones.remove(number);
                if (phone == null){
                    phone = new Phone();
                    phone.setPerson(person);
                    phone.setNumber(number);
                    userDAO.save(phone);
                }
            }
        }
        for (Map.Entry<String, Phone> entry : phones.entrySet()){
            userDAO.remove(entry.getValue());
        }
        return driver;
    }

    private void saveDetails(Report report, JSONArray jsonArray) {
        HashMap<String, ReportDetails> details = new HashMap<>();
        for (ReportDetails d : report.getDetails()){
            details.put(d.getUuid(), d);
        }

        for (Object o : jsonArray){
            System.out.println("!! " + o);
            JSONObject json = (JSONObject) o;
            final String uuid = String.valueOf(json.get(UUID));
            ReportDetails rd = details.remove(uuid);
            if (rd == null){
                rd = new ReportDetails();
                rd.setReport(report);
                rd.setUuid(uuid);
            }
            if (json.containsKey(DRIVER)){
                rd.setDriver(extractDriver((JSONObject) json.get(DRIVER)));
            } else {
                rd.setDriver(null);
            }
            if (json.containsKey(WEIGHT)){
                Weight weight = rd.getWeight();
                if (weight == null){
                    weight = new Weight();
                    rd.setWeight(weight);
                }
                parseWeight(weight, (JSONObject) json.get(WEIGHT));
            }
            reportDAO.save(rd);
            final String rdKey = rd.getUuid();
            final HashMap<String, CounterpartyWeight> counterpartyExistWeight = new HashMap<>();
            for (CounterpartyWeight cw : reportDAO.getCounterpartyWeight(rdKey)){
                counterpartyExistWeight.put(cw.getField(), cw);
            }
            if (json.containsKey(COUNTERPARTY_WEIGHT)){
                for (Object w : (JSONArray)json.get(Keys.COUNTERPARTY_WEIGHT)){
                    JSONObject jsonObject = (JSONObject) w;
                    final String fieldKey = String.valueOf(jsonObject.get(FIELD));
                    CounterpartyWeight counterpartyWeight = counterpartyExistWeight.remove(fieldKey);
                    if (counterpartyWeight == null){
                        counterpartyWeight = new CounterpartyWeight();
                        counterpartyWeight.setDetails(rdKey);
                        counterpartyWeight.setField(fieldKey);
                        counterpartyWeight.setWeight(new Weight());
                    }
                    final Weight weight = counterpartyWeight.getWeight();
                    parseWeight(weight, (JSONObject) jsonObject.get(WEIGHT));
                    reportDAO.save(counterpartyWeight);
                }
            }

            for (CounterpartyWeight cw : counterpartyExistWeight.values()){
                reportDAO.remove(cw.getWeight());
                reportDAO.remove(cw);
            }
        }
        for (ReportDetails d : details.values()){
            reportDAO.remove(d);
        }
    }

    private void parseWeight(Weight weight, JSONObject o) {
        float gross = Float.parseFloat(String.valueOf(o.get(GROSS)));
        float tare = Float.parseFloat(String.valueOf(o.get(TARE)));
        weight.setGross(gross);
        weight.setTare(tare);
        reportDAO.save(weight);
    }

    private void saveNotes(Report report, JSONArray array) {
        final HashMap<String, ReportNote> noteHashMap = new HashMap<>();
        for (ReportNote note : reportDAO.getNotes(report)){
            noteHashMap.put(note.getUuid(), note);
        }

        for (Object o : array){
            JSONObject json = (JSONObject) o;
            String uuid = String.valueOf(json.get(ID));
            ReportNote reportNote = noteHashMap.remove(uuid);
            if (reportNote == null){
                reportNote = new ReportNote();
                reportNote.setReport(report);
                reportNote.setUuid(uuid);
            }

            Timestamp time = new Timestamp(Long.parseLong(String.valueOf(json.get(TIME))));
            reportNote.setTime(time);

            String note = String.valueOf(json.get(NOTE));
            reportNote.setNote(note);

            reportDAO.save(reportNote);
        }
        for (Map.Entry<String, ReportNote> entry : noteHashMap.entrySet()){
            reportDAO.remove(entry.getValue());
        }
    }

    private void saveExpenses(Report report, Set<Expense> expenses, JSONArray expensesList, ExpenseType type) {
        HashMap<String, Expense> expenseHashMap = new HashMap<>();
        for (Expense expense : expenses){
            expenseHashMap.put(expense.getUuid(), expense);
        }
        for (Object o : expensesList){
            JSONObject json = (JSONObject) o;
            String uuid = String.valueOf(json.get(ID));
            Expense expense = expenseHashMap.remove(uuid);
            if (expense == null){
                expense = new Expense();
                expense.setReport(report);
                expense.setUuid(uuid);
                expense.setType(type);
            }

            String description = String.valueOf(json.get(DESCRIPTION));
            expense.setDescription(description);

            int amount = Integer.parseInt(String.valueOf(json.get(AMOUNT)));
            expense.setAmount(amount);

            reportDAO.save(expense);
        }
    }

    private void saveFields(Report report, JSONArray fields) {
        HashMap<String, ReportField> fieldHashMap = new HashMap<>();
        for (ReportField rf : reportDAO.getFields(report)){
            fieldHashMap.put(rf.getUuid(), rf);
        }
        for (Object o : fields){
            JSONObject field = (JSONObject) o;
            String id = String.valueOf(field.get(ID));
            ReportField reportField = fieldHashMap.remove(id);
            if (reportField == null){
                reportField = new ReportField();
                reportField.setReport(report);
                reportField.setUuid(id);
            }

            if (field.containsKey(ARRIVE)){
                Timestamp arrive = new Timestamp(Long.parseLong(String.valueOf(field.get(ARRIVE))));
                reportField.setArriveTime(arrive);
            }

            if (field.containsKey(LEAVE)){
                Timestamp leave = new Timestamp(Long.parseLong(String.valueOf(field.get(LEAVE))));
                reportField.setLeaveTime(leave);
            }

            if (field.containsKey(COUNTERPARTY)){
                reportField.setCounterparty(parseCounterparty((JSONObject)field.get(COUNTERPARTY)));
            } else {
                reportField.setCounterparty(null);
            }

            int money = Integer.parseInt(String.valueOf(field.get(MONEY)));
            reportField.setMoney(money);

            if (field.containsKey(WEIGHT)){
                final Object w = field.get(WEIGHT);
                if (w != null) {
                    Weight weight = reportField.getWeight();
                    if (weight == null) {
                        weight = new Weight();
                        reportField.setWeight(weight);
                    }
                    parseWeight(weight, (JSONObject) w);
                }
            }

            reportDAO.save(reportField);
        }
        for (Map.Entry<String, ReportField> entry : fieldHashMap.entrySet()){
            reportDAO.remove(entry.getValue());
        }
    }

    private Counterparty parseCounterparty(JSONObject jsonObject) {
        final String uuid = (String) jsonObject.get(UUID);
        Counterparty counterparty = counterpartyDAO.getCounterpartyByUUID(uuid);
        if (counterparty == null){
            counterparty = new Counterparty();
            counterparty.setUuid(uuid);
            counterparty.setName(String.valueOf(jsonObject.get(NAME)));
            counterpartyDAO.save(counterparty);
        }
        return counterparty;
    }
}


package entity;

import entity.reports.ReportDetails;
import org.hibernate.annotations.Where;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static constants.Keys.*;

@Entity
@Table(name = "reports")
public class Report extends JsonAble implements Comparable<Report> {
    private int id;
    private int clientId;
    private String uuid;
    private String route;
    private Set<ReportDetails> details = new HashSet<>();
    private Timestamp leaveTime;
    private Product product;
    private Timestamp done;
    private User owner;

    private Driver driver;
    private Weight weight;

    private Set<Expense> fares = new HashSet<>();
    private Set<Expense> expenses = new HashSet<>();
    private int perDiem;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "_client_id")
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "leave_time")
    public Timestamp getLeaveTime() {
        return leaveTime;
    }
    public void setLeaveTime(Timestamp leaveTime) {
        this.leaveTime = leaveTime;
    }

    @OneToOne
    @JoinColumn(name = "product")
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    @OneToOne
    @JoinColumn(name = "weight")
    public Weight getWeight() {
        return weight;
    }
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "route")
    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "report", cascade = CascadeType.ALL)
    public Set<ReportDetails> getDetails() {
        return details;
    }
    public void setDetails(Set<ReportDetails> details) {
        this.details = details;
    }

    @Basic
    @Column(name = "done")
    public Timestamp getDone() {
        return done;
    }
    public void setDone(Timestamp done) {
        this.done = done;
    }

    @OneToOne
    @JoinColumn(name = "owner")
    public User getOwner() {
        return owner;
    }
    public void setOwner(User attendant) {
        this.owner = attendant;
    }

    @OneToOne
    @JoinColumn(name = "driver")
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "report", cascade = CascadeType.ALL)
    @Where(clause = "type = 0")
    public Set<Expense> getFares() {
        return fares;
    }
    public void setFares(Set<Expense> fares) {
        this.fares = fares;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "report", cascade = CascadeType.ALL)
    @Where(clause = "type = 1")
    public Set<Expense> getExpenses() {
        return expenses;
    }
    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    @Basic
    @Column(name = "per_diem")
    public int getPerDiem() {
        return perDiem;
    }
    public void setPerDiem(int perDiem) {
        this.perDiem = perDiem;
    }

    @Override
    public JSONObject toSimpleJson() {
        JSONObject json = getJsonObject();
        json.put(ID, id);
        if (driver != null){
            json.put(DRIVER, driver.toJson());
        }
        if (leaveTime != null) {
            json.put(LEAVE, leaveTime.toString());
        }
        if (route != null){
            json.put(ROUTE, route);
        }
        json.put(DETAILS, details());
        if (product != null){
            json.put(PRODUCT, product.toJson());
        }
        if (done != null){
            json.put(DONE, done.toString());
        }
        json.put(OWNER, owner.toJson());
        return json;
    }

    private JSONArray details() {
        JSONArray array = new JSONArray();
        for (ReportDetails details : details){
            array.add(details.toJson());
        }
        return array;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject json = toSimpleJson();
        if (weight != null){
            json.put(WEIGHT, weight.toJson());
        }

        json.put(FARES, fares());
        json.put(EXPENSES, expenses());
        json.put(PER_DIEM, perDiem);
        return json;
    }

    private JSONArray fares() {
        JSONArray array = new JSONArray();
        return array;
    }

    private JSONArray expenses() {
        JSONArray array = new JSONArray();
        for (Expense expense : expenses){
            array.add(expense.toJson());
        }
        return array;
    }

    static final int divider = 1000 * 60 * 60 * 24;
    @Transient
    public int length(){
        if (leaveTime != null && done != null){
            final long diff = done.getTime() - leaveTime.getTime();
            return (int) Math.ceil(1d * diff / divider);
        }
        return 0;
    }
    @Transient
    public String buildRoute(){
        return route.replaceAll(COMA, SPACE + RIGHT_ARROW + SPACE);
    }

    @Override
    public int compareTo(Report report) {
        return leaveTime.compareTo(report.getLeaveTime());
    }
}

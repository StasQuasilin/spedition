package entity.reports;

import constants.Keys;
import entity.*;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "report_details")
public class ReportDetails extends JsonAble {
    private int id;
    private String uuid;
    private Report report;
    private Driver driver;
    private Weight weight;
    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "_uuid")
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @ManyToOne
    @JoinColumn(name = "_report")
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }

    @OneToOne
    @JoinColumn(name = "_driver")
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @OneToOne
    @JoinColumn(name = "_weight")
    public Weight getWeight() {
        return weight;
    }
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @OneToOne
    @JoinColumn(name = "_product")
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = getJsonObject();
        if(driver != null){
            jsonObject.put(Keys.DRIVER, driver.toJson());
        }
        if(weight != null){
            jsonObject.put(Keys.WEIGHT, weight.toJson());
        }
        return jsonObject;
    }
}

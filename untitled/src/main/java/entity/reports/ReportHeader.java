package entity.reports;

import entity.JsonAble;
import entity.Product;
import entity.User;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

import static constants.Keys.*;
import static constants.Keys.OWNER;

@Entity
@Table(name = "reports")
public class ReportHeader extends JsonAble {
    private int id;
    private String route;
    private Product product;
    private Timestamp leaveTime;
    private Timestamp done;
    private User owner;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "route")
    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }

    @OneToOne
    @JoinColumn(name = "product")
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    @Basic
    @Column(name = "leave_time")
    public Timestamp getLeaveTime() {
        return leaveTime;
    }
    public void setLeaveTime(Timestamp leaveTime) {
        this.leaveTime = leaveTime;
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

    @Override
    public JSONObject toJson() {
        JSONObject json = getJsonObject();
        json.put(ID, id);
        if (route != null){
            json.put(ROUTE, route);
        }
        if (product != null){
            json.put(PRODUCT, product.toJson());
        }
        if (leaveTime != null) {
            json.put(LEAVE, leaveTime.toString());
        }
        if (done != null){
            json.put(DONE, done.toString());
        }
        json.put(OWNER, owner.toJson());
        return json;
    }
}

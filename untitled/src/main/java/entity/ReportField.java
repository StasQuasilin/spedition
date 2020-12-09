package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report_fields")
public class ReportField implements Comparable<ReportField> {
    private int id;
    private Report report;
    private String uuid;
    private Timestamp arriveTime;
    private Timestamp leaveTime;
    private String oldCounterparty;
    private Counterparty counterparty;
    private int money;
    private Product product;
    private Weight weight;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "report")
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
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
    @Column(name = "arrive_time")
    public Timestamp getArriveTime() {
        return arriveTime;
    }
    public void setArriveTime(Timestamp arriveTime) {
        this.arriveTime = arriveTime;
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
    @Column(name = "counterparty")
    public String getOldCounterparty() {
        return oldCounterparty;
    }
    public void setOldCounterparty(String counterparty) {
        this.oldCounterparty = counterparty;
    }

    @OneToOne
    @JoinColumn(name = "_counterparty")
    public Counterparty getCounterparty() {
        return counterparty;
    }
    public void setCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
    }

    @Basic
    @Column(name = "money")
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
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

    @Override
    public int compareTo(ReportField reportField) {
        if (arriveTime == null){
            return 1;
        } else if (reportField.getArriveTime() == null){
            return -1;
        } else {
            return arriveTime.compareTo(reportField.getArriveTime());
        }
    }
}

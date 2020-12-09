package entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

import static constants.Keys.AMOUNT;
import static constants.Keys.DESCRIPTION;

@Entity
@Table(name = "expenses")
public class Expense extends JsonAble{
    private int id;
    private String uuid;
    private Report report;
    private String description;
    private int amount;
    private ExpenseType type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "amount")
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    public ExpenseType getType() {
        return type;
    }
    public void setType(ExpenseType type) {
        this.type = type;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = getJsonObject();
        jsonObject.put(DESCRIPTION, description);
        jsonObject.put(AMOUNT, amount);
        return jsonObject;
    }
}


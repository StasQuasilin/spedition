package entity.reports;

import entity.ReportField;
import entity.Weight;

import javax.persistence.*;

@Entity
@Table(name = "counterparty_weight")
public class CounterpartyWeight {
    private int id;
    private String details;
    private String field;
    private Weight weight;

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
    @Column(name = "_details")
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    @Basic
    @Column(name = "_field")
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    @OneToOne
    @JoinColumn(name = "_weight")
    public Weight getWeight() {
        return weight;
    }
    public void setWeight(Weight weight) {
        this.weight = weight;
    }
}

package entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

import static constants.Keys.GROSS;
import static constants.Keys.TARE;

@Entity
@Table(name = "weights")
public class Weight extends JsonAble{
    private int id;
    private float gross;
    private float tare;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "gross")
    public float getGross() {
        return gross;
    }
    public void setGross(float gross) {
        this.gross = gross;
    }

    @Basic
    @Column(name = "tare")
    public float getTare() {
        return tare;
    }
    public void setTare(float tare) {
        this.tare = tare;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = getJsonObject();
        jsonObject.put(GROSS, gross);
        jsonObject.put(TARE, tare);
        return jsonObject;
    }
    @Transient
    public float net(){
        if(gross > 0 && tare > 0){
            return gross - tare;
        }
        return 0;
    }
}

package entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

import static constants.Keys.ID;
import static constants.Keys.NAME;

@Entity
@Table(name = "products")
public class Product extends JsonAble{
    private int id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = getJsonObject();
        json.put(ID, id);
        json.put(NAME, name);
        return json;
    }
}

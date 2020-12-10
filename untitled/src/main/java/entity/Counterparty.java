package entity;

import constants.Keys;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "counterparty")
public class Counterparty extends JsonAble{
    private int id;
    private String uuid;
    private String name;
    private Timestamp lastChange;

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

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "last_change")
    public Timestamp getLastChange() {
        return lastChange;
    }
    public void setLastChange(Timestamp lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.ID, id);
        jsonObject.put(Keys.UUID, uuid);
        jsonObject.put(Keys.NAME, name);
        return jsonObject;
    }
}

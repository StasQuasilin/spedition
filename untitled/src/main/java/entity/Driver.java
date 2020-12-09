package entity;

import org.json.simple.JSONObject;

import javax.persistence.*;

import java.sql.Timestamp;

import static constants.Keys.ID;
import static constants.Keys.UUID;

@Entity
@Table(name = "drivers")
public class Driver extends JsonAble{
    private int id;
    private String uuid;
    private Person person;
    private Timestamp modificationTime;

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

    @OneToOne
    @JoinColumn(name = "person")
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    @Basic
    @Column(name = "_lm")
    public Timestamp getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Timestamp modificationTime) {
        this.modificationTime = modificationTime;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = person.toJson();
        jsonObject.put(ID, id);
        jsonObject.put(UUID, uuid);
        return jsonObject;
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }
}

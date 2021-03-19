package entity.coordinates;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "locations")
public class Location {
    private int id;
    private String report;
    private Timestamp timestamp;
    private float latitude;
    private float longitude;
    private float speed;

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
    @Column(name = "_report")
    public String getReport() {
        return report;
    }
    public void setReport(String report) {
        this.report = report;
    }

    @Basic
    @Column(name = "_timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "_latitude")
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "_longitude")
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "_speed")
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

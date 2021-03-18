package entity.coordinates;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "locations")
public class Location {
    private int id;
    private int report;
    private Timestamp timestamp;
    private long latitude;
    private long longitude;

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
    public int getReport() {
        return report;
    }
    public void setReport(int report) {
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
    public long getLatitude() {
        return latitude;
    }
    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "_longitude")
    public long getLongitude() {
        return longitude;
    }
    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}

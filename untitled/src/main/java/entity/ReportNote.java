package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report_notes")
public class ReportNote implements Comparable<ReportNote>{
    private int id;
    private String uuid;
    private Report report;
    private Timestamp time;
    private String note;

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
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int compareTo(ReportNote reportNote) {
        return time.compareTo(reportNote.time);
    }
}

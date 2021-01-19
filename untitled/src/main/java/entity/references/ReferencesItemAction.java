package entity.references;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reference_item_action")
public class ReferencesItemAction {
    private int id;
    private Timestamp timestamp;
    private ReferenceAction action;
    private ReferenceType type;
    private int itemId;

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
    @Column(name = "_timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "_action")
    public ReferenceAction getAction() {
        return action;
    }
    public void setAction(ReferenceAction action) {
        this.action = action;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "_type")
    public ReferenceType getType() {
        return type;
    }
    public void setType(ReferenceType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "_item_id")
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}

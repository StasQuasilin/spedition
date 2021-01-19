package utils.hibernate.dao;

import entity.references.ReferenceAction;
import entity.references.ReferenceType;
import entity.references.ReferencesItemAction;
import utils.hibernate.DateContainers.GE;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class ReferencesItemDAO {
    private final Hibernator hibernator = Hibernator.getInstance();

    public List<ReferencesItemAction> getActions(Timestamp time, ReferenceType type) {
        final HashMap<String, Object> param = new HashMap<>();
        if (time != null){
            param.put("timestamp", new GE(time));
        }
        param.put("type", type);
        return hibernator.query(ReferencesItemAction.class, param);
    }

    public void updateAction(ReferenceType type, ReferenceAction action, int itemId){
        ReferencesItemAction itemAction = hibernator.get(ReferencesItemAction.class, "itemId", itemId);
        if(itemAction == null){
            itemAction = new ReferencesItemAction();
            itemAction.setItemId(itemId);
        }
        itemAction.setType(type);
        itemAction.setAction(action);
        itemAction.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        hibernator.save(itemAction);


    }
}

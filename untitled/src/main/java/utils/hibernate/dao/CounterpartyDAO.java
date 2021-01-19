package utils.hibernate.dao;

import constants.Keys;
import entity.Counterparty;
import entity.references.ReferenceAction;
import entity.references.ReferenceItem;
import entity.references.ReferenceType;
import utils.hibernate.DateContainers.GE;
import utils.hibernate.DateContainers.LE;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class CounterpartyDAO implements ReferencesDAO {

    private final Hibernator hibernator = Hibernator.getInstance();
    private final ReferencesItemDAO itemDAO = new ReferencesItemDAO();

    public List<Counterparty> getList() {
        return hibernator.query(Counterparty.class, null);
    }

    public Counterparty getCounterparty(Object id) {
        return hibernator.get(Counterparty.class, Keys.ID, id);
    }

    public Counterparty getCounterpartyByUUID(String uuid) {
        return hibernator.get(Counterparty.class, Keys.UUID, uuid);
    }

    public void save(Counterparty counterparty) {
        counterparty.setLastChange(Timestamp.valueOf(LocalDateTime.now()));
        hibernator.save(counterparty);
        itemDAO.updateAction(ReferenceType.counterparty, ReferenceAction.update, counterparty.getId());
    }

    @Override
    public int[] getAllId() {
        return build(getList());
    }

    private int[] build(List<Counterparty> list) {
        int[] ints = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            ints[i] = list.get(i).getId();
        }
        return ints;
    }

    @Override
    public int[] getIdAfter(Timestamp t) {
        return build(getCounterpartyAfter(t));
    }

    public List<Counterparty> getCounterpartyAfter(Timestamp timestamp) {
        return hibernator.query(Counterparty.class, "lastChange", new GE(timestamp));
    }
}

package utils.hibernate.dao;

import constants.Keys;
import entity.Counterparty;
import entity.Driver;
import entity.references.ReferenceAction;
import entity.references.ReferenceItem;
import entity.references.ReferenceType;
import utils.hibernate.DateContainers.GE;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class DriverDAO implements ReferencesDAO {

    private final Hibernator hibernator = Hibernator.getInstance();
    private final ReferencesItemDAO itemDAO = new ReferencesItemDAO();

    public Driver getDriverByUUID(Object uuid){
        return hibernator.get(Driver.class, Keys.UUID, uuid);
    }

    public void save(Driver driver) {
        hibernator.save(driver.getPerson());
        driver.setModificationTime(Timestamp.valueOf(LocalDateTime.now()));
        hibernator.save(driver);
        itemDAO.updateAction(ReferenceType.driver, ReferenceAction.update, driver.getId());
    }
    public List<Driver> getAllList() {
        return hibernator.query(Driver.class, null);
    }

    public Driver getDriver(Object id) {
        return hibernator.get(Driver.class, Keys.ID, id);
    }

    public List<Driver> getDriverAfter(Timestamp t) {
        return hibernator.query(Driver.class, "modificationTime", new GE(t));
    }

    @Override
    public int[] getAllId() {
        return build(getAllList());
    }

    private int[] build(List<Driver> list) {
        int[] ints = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            ints[i] = list.get(i).getId();
        }
        return ints;
    }

    @Override
    public int[] getIdAfter(Timestamp t) {
        return build(getDriverAfter(t));
    }
}

package utils.hibernate.dao;

import constants.Keys;
import entity.Driver;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class DriverDAO {
    private final Hibernator hibernator = Hibernator.getInstance();

    public Driver getDriverByUUID(Object uuid){
        return hibernator.get(Driver.class, Keys.ID, uuid);
    }

    public void save(Driver driver) {
        hibernator.save(driver.getPerson());
        driver.setModificationTime(Timestamp.valueOf(LocalDateTime.now()));
        hibernator.save(driver);
    }

    public List<Driver> getDrivers() {
        return hibernator.query(Driver.class, null);
    }

    public Driver getDriver(Object id) {
        return hibernator.get(Driver.class, Keys.ID, id);
    }
}

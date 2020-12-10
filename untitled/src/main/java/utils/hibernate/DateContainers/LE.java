package utils.hibernate.DateContainers;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by szpt_user045 on 05.11.2018.
 */
public class LE extends IDateContainer {
    public LE(Date date) {
        super(date);
    }

    public LE(Timestamp timestamp) {
        super(timestamp);
    }
}

package utils.hibernate.DateContainers;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Quasilin on 02.11.2018.
 */
public class GE extends IDateContainer {
    public GE(Date date) {
        super(date);
    }

    public GE(Timestamp timestamp) {
        super(timestamp);
    }
}

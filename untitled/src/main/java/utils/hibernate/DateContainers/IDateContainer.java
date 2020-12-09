package utils.hibernate.DateContainers;

import java.sql.Date;

/**
 * Created by Quasilin on 02.11.2018.
 */
public abstract class IDateContainer  {
    private Date date;

    public IDateContainer(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

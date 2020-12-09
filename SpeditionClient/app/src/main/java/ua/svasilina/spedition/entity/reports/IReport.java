package ua.svasilina.spedition.entity.reports;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Product;

public abstract class IReport {
    long id;
    private String uuid;
    public Calendar leaveTime;
    Calendar doneDate;
    public LinkedList<String> route;

    public IReport() {
        route = new LinkedList<>();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Calendar getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Calendar leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Calendar getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Calendar doneDate) {
        this.doneDate = doneDate;
    }

    public LinkedList<String> getRoute() {
        return route;
    }

    public void setLeaveTime(long time) {
        if(leaveTime == null)
            leaveTime = Calendar.getInstance();
        leaveTime.setTimeInMillis(time);
    }

    public void setDoneTime(long time) {
        if (doneDate == null)
            doneDate = Calendar.getInstance();

        doneDate.setTimeInMillis(time);
    }

    public void addRoute(String point){
        route.add(point);
    }

    public void setRoute(String route) {
        if (route != null)
            this.route.addAll(Arrays.asList(route.split(Keys.COMA)));
    }


    public boolean isDone() {
        return doneDate != null;
    }

    public abstract LinkedList<Product> getProducts();

}

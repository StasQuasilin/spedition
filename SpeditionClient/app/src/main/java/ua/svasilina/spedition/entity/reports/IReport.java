package ua.svasilina.spedition.entity.reports;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.utils.db.JsonObject;

public abstract class IReport {
    long id;
    private String uuid;
    public Calendar leaveTime;
    Calendar doneDate;
    public final LinkedList<String> route = new LinkedList<>();
    public IReport(){

    }
    public IReport(JsonObject parse) {
        id = parse.getLongOrDefault(Keys.ID, -1);
        uuid = parse.getString(Keys.UUID);
        final long leave = parse.getLongOrDefault(Keys.LEAVE, -1);
        if(leave != -1){
            setLeaveTime(leave);
        }
        final long done = parse.getLongOrDefault(Keys.DONE, -1);
        if (done != -1){
            setDoneTime(done);
        }
        final String route = parse.getStringOrNull(Keys.ROUTE);
        if (route != null){
            setRoute(route);
        }
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

    public boolean isActive(){
        return leaveTime != null && doneDate == null;
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

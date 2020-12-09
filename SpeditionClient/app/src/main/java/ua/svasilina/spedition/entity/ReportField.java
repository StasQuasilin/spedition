package ua.svasilina.spedition.entity;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.Calendar;

import static ua.svasilina.spedition.constants.Keys.ARRIVE;
import static ua.svasilina.spedition.constants.Keys.COUNTERPARTY;
import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.LEAVE;
import static ua.svasilina.spedition.constants.Keys.MONEY;
import static ua.svasilina.spedition.constants.Keys.PRODUCT;
import static ua.svasilina.spedition.constants.Keys.WEIGHT;

public class ReportField implements Comparable<ReportField>, JsonAble {
    private String uuid;
    private Counterparty counterparty;
    private Calendar arriveTime;
    private Calendar leaveTime;
    private Product product;
    private int money;
    private Weight weight;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }
    public void setCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
    }

    public Calendar getArriveTime() {
        return arriveTime;
    }
    public void setArriveTime(Calendar arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Calendar getLeaveTime() {
        return leaveTime;
    }
    public void setLeaveTime(Calendar leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public Weight getWeight() {
        return weight;
    }
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, uuid);
        if (counterparty != null) {
            jsonObject.put(COUNTERPARTY, counterparty.toJson());
        }
        if (arriveTime != null) {
            jsonObject.put(ARRIVE, arriveTime.getTimeInMillis());
        }
        if (leaveTime != null){
            jsonObject.put(LEAVE, leaveTime.getTimeInMillis());
        }
        if (product != null){
            jsonObject.put(PRODUCT, product.getId());
        }
        jsonObject.put(MONEY, money);
        if (weight != null){
            jsonObject.put(WEIGHT, weight.toJson());
        }

        return jsonObject;
    }


    @Override
    public int compareTo(@NotNull ReportField o) {
        if (arriveTime == null){
            return 1;
        } else if (o.arriveTime == null){
            return -1;
        }
        return arriveTime.compareTo(o.arriveTime);
    }
}

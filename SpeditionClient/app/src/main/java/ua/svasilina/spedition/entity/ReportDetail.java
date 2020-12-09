package ua.svasilina.spedition.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ua.svasilina.spedition.constants.Keys;

public class ReportDetail implements JsonAble{
    private long id = -1;
    private int serverId;
    private String uuid;
    private Driver driver;
    private Product product;
    private Weight ownWeight;
    private HashMap<String, Weight> counterpartyWeight = new HashMap<>();

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public Weight getOwnWeight() {
        return ownWeight;
    }
    public void setOwnWeight(Weight ownWeight) {
        this.ownWeight = ownWeight;
    }

    public HashMap<String, Weight> getCounterpartyWeight() {
        return counterpartyWeight;
    }
    public void setCounterpartyWeight(HashMap<String, Weight> counterpartyWeight) {
        this.counterpartyWeight = counterpartyWeight;
    }
    public Weight getCounterpartyWeight(String uuid){
        return counterpartyWeight.get(uuid);
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        assert obj != null;
        return obj.getClass().equals(getClass()) && obj.hashCode() == hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return (driver != null ? driver.toString() : Keys.EMPTY) +
                (driver != null && ownWeight != null ? Keys.COLON+ Keys.SPACE : Keys.EMPTY) +
                (ownWeight != null ? ownWeight.toString() : Keys.EMPTY);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.ID, id);
        jsonObject.put(Keys.UUID, uuid);
        if (driver != null) {
            jsonObject.put(Keys.DRIVER, driver.toJson());
        }
        if (ownWeight != null){
            jsonObject.put(Keys.OWN_WEIGHT, ownWeight.toJson());
        }
        if(product != null){
            jsonObject.put(Keys.PRODUCT, product.toJson());
        }
        jsonObject.put(Keys.COUNTERPARTY_WEIGHT,counterpartyWeight());
        return jsonObject;
    }

    private JSONArray counterpartyWeight() {
        JSONArray array = new JSONArray();
        for (Map.Entry<String, Weight> entry : counterpartyWeight.entrySet()){
            JSONObject json = new JSONObject();
            json.put(Keys.FIELD, entry.getKey());
            json.put(Keys.WEIGHT, entry.getValue().toJson());
            array.add(json);
        }
        return array;
    }

    public void setCounterpartyWeight(String uuid, Weight weight) {
        counterpartyWeight.put(uuid, weight);
    }
}

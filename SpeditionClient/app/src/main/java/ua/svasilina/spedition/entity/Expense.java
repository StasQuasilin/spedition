package ua.svasilina.spedition.entity;

import androidx.annotation.NonNull;

import org.json.simple.JSONObject;

import static ua.svasilina.spedition.constants.Keys.AMOUNT;
import static ua.svasilina.spedition.constants.Keys.COLON;
import static ua.svasilina.spedition.constants.Keys.DESCRIPTION;
import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.SPACE;

public class Expense implements JsonAble {
    private String uuid;
    private String description;
    private int amount;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject json = new JSONObject();
        json.put(ID, uuid);
        json.put(DESCRIPTION, description);
        json.put(AMOUNT, amount);
        return json;
    }

    @NonNull
    @Override
    public String toString() {
        return description.toUpperCase() + COLON + SPACE + amount;
    }
}

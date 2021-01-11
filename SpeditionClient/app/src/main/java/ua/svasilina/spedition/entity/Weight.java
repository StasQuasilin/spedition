package ua.svasilina.spedition.entity;

import androidx.annotation.NonNull;

import org.json.simple.JSONObject;

import static ua.svasilina.spedition.constants.Keys.GROSS;
import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.TARE;
import static ua.svasilina.spedition.constants.Keys.UUID;

public class Weight implements JsonAble {
    private int id;
    private String uuid;
    private int gross;
    private int tare;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getGross() {
        return gross;
    }
    public void setGross(int gross) {
        this.gross = gross;
    }

    public int getTare() {
        return tare;
    }
    public void setTare(int tare) {
        this.tare = tare;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, id);
        jsonObject.put(UUID, uuid);
        jsonObject.put(GROSS, gross);
        jsonObject.put(TARE, tare);
        return jsonObject;
    }

    public int getNet() {
        if(gross > 0 && tare > 0){
            return gross - tare;
        }
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

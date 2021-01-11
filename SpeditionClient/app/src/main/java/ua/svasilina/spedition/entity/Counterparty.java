package ua.svasilina.spedition.entity;

import org.json.simple.JSONObject;

import ua.svasilina.spedition.constants.Keys;

public class Counterparty implements JsonAble{
    private int id;
    private String uuid;
    private String name;

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.ID, id);
        jsonObject.put(Keys.UUID, uuid);
        jsonObject.put(Keys.NAME, name);
        return jsonObject;
    }
}

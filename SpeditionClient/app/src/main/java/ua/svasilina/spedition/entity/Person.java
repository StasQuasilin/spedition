package ua.svasilina.spedition.entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static ua.svasilina.spedition.constants.Keys.EMPTY;
import static ua.svasilina.spedition.constants.Keys.FORENAME;
import static ua.svasilina.spedition.constants.Keys.PATRONYMIC;
import static ua.svasilina.spedition.constants.Keys.PHONES;
import static ua.svasilina.spedition.constants.Keys.SPACE;
import static ua.svasilina.spedition.constants.Keys.SURNAME;

public class Person implements JsonAble {
    private String surname;
    private String forename;
    private String patronymic;
    private final ArrayList<String> phones = new ArrayList<>();

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }
    public void setForename(String forename) {
        this.forename = forename;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(FORENAME, forename);
        json.put(SURNAME, surname);
        json.put(PATRONYMIC, patronymic);
        json.put(PHONES, phones());
        return json;
    }

    private JSONArray phones() {
        JSONArray array = new JSONArray();
        for (String phone : phones){
            array.add(phone);
        }
        return array;
    }

    public String getValue() {

        return (surname != null ? surname : EMPTY) +
                (surname != null && forename != null ? SPACE : EMPTY) +
                (forename != null ? forename : EMPTY);
    }

    public boolean isEmpty() {
        return (surname == null || surname.isEmpty()) && (forename == null || forename.isEmpty());
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }

    public boolean anyChanges() {
        return !isEmpty() || (patronymic != null && !patronymic.isEmpty()) || phones.size() > 0;
    }
}

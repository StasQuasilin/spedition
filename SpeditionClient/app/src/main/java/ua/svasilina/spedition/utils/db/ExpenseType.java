package ua.svasilina.spedition.utils.db;

import java.util.HashMap;
import java.util.Map;

public enum ExpenseType {
    expense(0),
    fare(1);

    private final int value;
    private static final Map<Integer, ExpenseType> map = new HashMap<>();

    static {
        for (ExpenseType type : ExpenseType.values()){
            map.put(type.getValue(), type);
        }
    }

    ExpenseType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ExpenseType valueOf(int type){
        return map.get(type);
    }
}

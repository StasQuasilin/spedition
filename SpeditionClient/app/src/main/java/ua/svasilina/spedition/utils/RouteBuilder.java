package ua.svasilina.spedition.utils;

import java.util.LinkedList;

import ua.svasilina.spedition.constants.Keys;

public class RouteBuilder {

    public String build(LinkedList<String> route){
        return build(route, Keys.ARROW);
    }

    public String build(LinkedList<String> route, String divider){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < route.size(); i++){
            final String r = route.get(i);
            if (r != null){
                builder.append(r.toUpperCase());
                if (i < route.size()- 1){
                    builder.append(divider);
                }
            }
        }
        return builder.toString();
    }
}

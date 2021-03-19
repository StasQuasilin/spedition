package ua.svasilina.spedition.utils.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ua.svasilina.spedition.utils.LoginUtil;

import static ua.svasilina.spedition.constants.Keys.TOKEN;

public class DataSender {

    private final LoginUtil loginUtil;
    private final Connector connector;
    private final Context context;

    public DataSender(Context context){
        this.context = context;
        loginUtil = new LoginUtil(context);
        connector = Connector.getConnector();
    }

    public void send(String api, JSONObject json, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError){
        final String token = loginUtil.getToken();
        if (token != null) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    api, json, onSuccess,
                    onError) {
                @Override
                public Map<String, String> getHeaders() {
                    final HashMap<String, String> map = new HashMap<>();
                    map.put(TOKEN, token);
                    map.put("content-type", "application/json; charset=utf-8");
                    return map;
                }
            };
            connector.addRequest(context, request);
        }
    }
}

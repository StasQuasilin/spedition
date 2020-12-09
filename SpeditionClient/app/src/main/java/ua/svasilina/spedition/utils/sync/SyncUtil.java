package ua.svasilina.spedition.utils.sync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.JsonParser;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.NetworkUtil;
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.db.ReportUtil;
import ua.svasilina.spedition.utils.network.Connector;

import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.STATUS;
import static ua.svasilina.spedition.constants.Keys.SUCCESS;
import static ua.svasilina.spedition.constants.Keys.TOKEN;

public class SyncUtil {

    private final Context context;
    private final ReportUtil reportsUtil;
    private final NetworkUtil networkUtil;
    private final LoginUtil loginUtil;
    private final JsonParser parser;

    public SyncUtil(Context context, ReportUtil reportsUtil) {
        this.context = context;
        this.reportsUtil = reportsUtil;
        networkUtil = new NetworkUtil();
        loginUtil  = new LoginUtil(context);
        parser = new JsonParser();
    }

    private boolean runTimer;

    public void sync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runTimer = false;
                for (Report r : reportsUtil.getUnSyncReports()){
                    saveReport(r, false);
                }
                
                for (String item : reportsUtil.getRemoved()){
                    remove(item, false);
                }
                if (runTimer){
                    runBackground();
                }
            }
        }).start();
    }

    public void remove(final String uuid, final boolean runBackground) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Keys.REPORT, uuid);
        sendJson(ApiLinks.REPORT_REMOVE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(STATUS);
                    if (status.equals(SUCCESS)) {
                        reportsUtil.forgotAbout(uuid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if(runBackground){
                    runBackground();
                } else {
                    runTimer = true;
                }
            }
        });
    }

    private void sendJson(String api, JSONObject json,  Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError){
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
            Connector.getConnector().addRequest(context, request);
        }
    }

    private final Set<Integer> nowSync = new HashSet<>();

    public void saveReport(final Report report, final boolean runBackground){
        if (report != null) {
            final long localId = (report.getId());
            final JSONObject object = new JSONObject(report.toJson());
            sendJson(ApiLinks.REPORT_SAVE, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try {
                        final String status = response.getString(STATUS);
                        if (status.equals(SUCCESS)) {
                            final int serverId = response.getInt(ID);
                            reportsUtil.markSync(localId, serverId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (runBackground){
                        runBackground();
                    } else {
                        runTimer = true;
                    }
                }
            });
        }
    }

    private void runBackground() {
        BackgroundWorkerUtil.getInstance().runWorker(context);
    }

    public void saveThread(final Report report) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveReport(report, true);
            }
        }).start();
    }

    public void saveReports(final LinkedList<Report> reports) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Report report : reports){
                    System.out.println("!!!!!!!!!!!!Save report " + report.getUuid());
                    saveReport(report, false);
                }
            }
        }).start();
    }
}

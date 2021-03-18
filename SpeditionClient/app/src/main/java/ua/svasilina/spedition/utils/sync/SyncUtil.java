package ua.svasilina.spedition.utils.sync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.db.AbstractReportUtil;
import ua.svasilina.spedition.utils.network.Connector;

import static ua.svasilina.spedition.constants.Keys.STATUS;
import static ua.svasilina.spedition.constants.Keys.SUCCESS;
import static ua.svasilina.spedition.constants.Keys.TOKEN;
import static ua.svasilina.spedition.constants.Keys.UUID;

public class SyncUtil {

    private final Context context;
    private final AbstractReportUtil reportsUtil;
    private final LoginUtil loginUtil;

    public SyncUtil(Context context, AbstractReportUtil reportsUtil) {
        this.context = context;
        this.reportsUtil = reportsUtil;
        loginUtil  = new LoginUtil(context);
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

    public void sendReport(final Report report, final boolean runBackground){
        if (report != null) {
            final JSONObject object = new JSONObject(report.toJson());

            sendJson(ApiLinks.REPORT_SAVE, object, response -> {
                try {
                    final String status = response.getString(STATUS);
                    if (status.equals(SUCCESS)) {
                        final String uuid = response.getString(UUID);
                        reportsUtil.markSync(uuid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                error.printStackTrace();
                if (runBackground){
                    runBackground();
                }
            });
        }
    }

    private void runBackground() {
        BackgroundWorkerUtil.getInstance().runWorker(context);
    }

    public void saveThread(final Report report) {
        new Thread(() -> sendReport(report, true)).start();
    }

    public void sendReports(final Report[] reports, final String[] removeIds) {
        new Thread(() -> {
            for (Report report : reports){
                sendReport(report, false);
            }
            for (String report : removeIds){
                remove(report, false);
            }

        }).start();
    }
}

package ua.svasilina.spedition.utils.background;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.UUID;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.utils.db.ReportUtil;
import ua.svasilina.spedition.utils.network.Connector;
import ua.svasilina.spedition.utils.sync.SyncUtil;


public class BackgroundWorker extends Worker {

    public static final String TAG = "Background Worker";
    private final Context context;
    private final BackgroundWorkerUtil backgroundWorkerUtil;
    private final SyncUtil syncUtil;
    private final String id = UUID.randomUUID().toString();
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public BackgroundWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.context = appContext;
        backgroundWorkerUtil = BackgroundWorkerUtil.getInstance();
        syncUtil = new SyncUtil(context, new ReportUtil(context));
    }
    @NonNull
    @Override
    public Result doWork() {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiLinks.PING, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        syncUtil.sync();
//                        backgroundWorkerUtil.runWorker(context);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        backgroundWorkerUtil.runWorker(context);
                    }
                }
        );
        Connector.getConnector().addRequest(context, request);
        return Result.success();
    }
}

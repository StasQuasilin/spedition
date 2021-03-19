package ua.svasilina.spedition.utils.sync;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.db.AbstractReportUtil;
import ua.svasilina.spedition.utils.network.DataSender;

import static ua.svasilina.spedition.constants.Keys.STATUS;
import static ua.svasilina.spedition.constants.Keys.SUCCESS;
import static ua.svasilina.spedition.constants.Keys.UUID;

public class SyncUtil {

    private final Context context;
    private final AbstractReportUtil reportsUtil;
    private final DataSender sender;

    public SyncUtil(Context context, AbstractReportUtil reportsUtil) {
        this.context = context;
        this.reportsUtil = reportsUtil;
        sender = new DataSender(context);
    }

    public void remove(final String uuid, final boolean runBackground) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Keys.REPORT, uuid);
        sender.send(ApiLinks.REPORT_REMOVE, new JSONObject(hashMap), response -> {
            try {
                final String status = response.getString(STATUS);
                if (status.equals(SUCCESS)) {
                    reportsUtil.forgotAbout(uuid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            if(runBackground){
                runBackground();
            }
        });
    }

    public void sendReport(final Report report, final boolean runBackground){
        if (report != null) {
            final JSONObject object = new JSONObject(report.toJson());
            sender.send(ApiLinks.REPORT_SAVE, object, response -> {
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

package ua.svasilina.spedition.utils.background;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ua.svasilina.spedition.utils.db.AbstractReportUtil;


public class BackgroundWorker extends Worker {

    public static final String TAG = "Background Worker";
    private final Context context;
    private final BackgroundWorkerUtil backgroundWorkerUtil;
    private final AbstractReportUtil reportUtil;
    private final OnActiveReport activeReport;
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public BackgroundWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.context = appContext;
        backgroundWorkerUtil = BackgroundWorkerUtil.getInstance();
        reportUtil = AbstractReportUtil.getReportUtil(context);
        activeReport = new OnActiveReport(context);
    }
    @NonNull
    @Override
    public Result doWork() {
        reportUtil.syncReports();
        activeReport.checkReports();
        backgroundWorkerUtil.runWorker(context);
        return Result.success();
    }
}

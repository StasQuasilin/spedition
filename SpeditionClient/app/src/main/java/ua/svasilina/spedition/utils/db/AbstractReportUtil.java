package ua.svasilina.spedition.utils.db;

import android.content.Context;

import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.sync.SyncUtil;

public abstract class AbstractReportUtil implements IReportUtil {

    SyncUtil syncUtil;

    AbstractReportUtil(Context context){
        syncUtil = new SyncUtil(context, this);
    }

    public static AbstractReportUtil getReportUtil(Context context){
        final AbstractReportUtil util = new SqLiteReportUtil(context);
        util.saveData(context);
        return util;
    }

    public void syncReports(){
        syncUtil.sendReports(getNotSyncReports(), getRemovedReports());
    }

    abstract Report[] getNotSyncReports();
    abstract String[] getRemovedReports();
    public abstract void markSync(String uuid);

    abstract AbstractReportUtil getDataSource(Context context);

    void saveData(Context context) {
        final AbstractReportUtil source = getDataSource(context);
        for (SimpleReport r : source.getReports()){
            final Report report = source.getReport(r.getUuid());
            saveReport(report);
        }
    }

    public abstract void forgotAbout(String uuid);
}

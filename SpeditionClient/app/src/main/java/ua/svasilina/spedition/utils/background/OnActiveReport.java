package ua.svasilina.spedition.utils.background;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.services.ActiveReportService;
import ua.svasilina.spedition.utils.RouteBuilder;
import ua.svasilina.spedition.utils.db.ReportUtil;

public class OnActiveReport {

    private final Context context;
    private final ReportUtil reportUtil;
    private final RouteBuilder routeBuilder;

    public OnActiveReport(Context context) {
        this.context = context;
        reportUtil = new ReportUtil(context);
        routeBuilder = new RouteBuilder();
    }

    public void checkReports(){
        final Report activeReport = reportUtil.getActiveReport();
        if (activeReport != null){
            startService(activeReport);
        } else {
            hideNotification();
        }
    }

    private void hideNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null){
                manager.cancel(ActiveReportService.NOTIFICATION_ID);
            }
        } else {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager manager = (NotificationManager) context.getSystemService(ns);
            if (manager != null) {
                manager.cancel(ActiveReportService.NOTIFICATION_ID);
            }
        }
        Intent stopIntent = new Intent(context, ActiveReportService.class);
        stopIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        stopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.stopService(stopIntent);
    }

    public void startService(Report report){
        updateNotification(report);
    }

    public void updateNotification(Report report){
        final Calendar leaveTime = report.getLeaveTime();
        final Calendar doneDate = report.getDoneDate();
        if (leaveTime != null && doneDate == null){
            final Intent intent = new Intent(context, ActiveReportService.class);
            intent.putExtra(Keys.ROUTE, routeBuilder.build(report.getRoute()));
            intent.putExtra(Keys.ID, report.getId());
            ContextCompat.startForegroundService(context, intent);
        } else {
            hideNotification();
        }
    }
}

package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.db.parsers.ReportParser;
import ua.svasilina.spedition.utils.sync.SyncUtil;

public class ReportUtil {

    private static final String ONE_ROW = "1";
    private static final String SYNC_COLUMN = "is_sync";
    private static final String ID_COLUMN = "id";
    private static final String LEAVE_COLUMN = "leave_time";
    private static final String DONE_COLUMN = "done_time";
    private static final String ROUTE_COLUMN = "route";
    private static final String PRODUCT_COLUMN = "product";
    private static final String SEPARATED_PRODUCT_COLUMN = "separated_products";
    private final String[] simpleReportFields = new String[]{
            ID_COLUMN, Keys.UUID, LEAVE_COLUMN, DONE_COLUMN, ROUTE_COLUMN, PRODUCT_COLUMN, SEPARATED_PRODUCT_COLUMN
    };
    private final ReportDetailUtil detailUtil;
    private final ReportFieldUtil reportFieldUtil;
    private final SyncUtil syncUtil;
    private final DBHelper dbHelper;
    private final ReportParser reportParser;
    private final SimpleParser simpleParser;
    private final Context context;

    public ReportUtil(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        reportParser = new ReportParser(context);
        simpleParser = new SimpleParser(context);
        reportFieldUtil = new ReportFieldUtil(context);
        detailUtil = new ReportDetailUtil(context);
        syncUtil = new SyncUtil(context, this);
    }

    public LinkedList<SimpleReport> getReportsList(){
        final LinkedList<SimpleReport> reports = new LinkedList<>();
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORTS, simpleReportFields, null, null, null, null, null);
        if(query.moveToFirst()){
            simpleParser.init(query);
            do {
                reports.add(simpleParser.parse(query));
            } while (query.moveToNext());
        }

        for (SimpleReport report :reports){
            detailUtil.getDetails(report,database);
        }
        database.close();
        Collections.sort(reports);
        return reports;
    }

    public Report getReport(long id){
        Log.i("Open report", String.valueOf(id));
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor query = db.query(Tables.REPORTS, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            reportParser.init(query);
            return reportParser.parse(query);
        }

        return null;
    }

    public void saveReport(Report report) {
        final ContentValues cv = new ContentValues();

        final Calendar leaveTime = report.getLeaveTime();
        String uuid = report.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            report.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);
        if (leaveTime != null) {
            cv.put(LEAVE_COLUMN, leaveTime.getTimeInMillis());
        }
        final Calendar doneDate = report.getDoneDate();
        if(doneDate != null){
            cv.put(DONE_COLUMN, doneDate.getTimeInMillis());
        }
        final Product product = report.getProduct();
        if(product != null){
            cv.put(PRODUCT_COLUMN, product.getId());
        }
        final String route = report.getRouteString();
        if (!route.isEmpty()) {
            cv.put(ROUTE_COLUMN, route);
        }
        cv.put(SYNC_COLUMN, false);

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = new String[]{uuid};
        final Cursor query = db.query(Tables.REPORTS, new String[]{Keys.ID}, DBConstants.UUID_PARAM, args, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            db.update(Tables.REPORTS, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.REPORTS, null, cv);
        }
        detailUtil.saveDetails(report);
        reportFieldUtil.saveFields(report);
        syncUtil.saveThread(report);

    }

    public boolean removeReport(String id) {
        String[] par = new String[]{id};
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int delete = db.delete(Tables.REPORTS, DBConstants.UUID_PARAM, par);

        db.delete(Tables.REPORT_DETAILS, DBConstants.REPORT_UUID, par);
        db.delete(Tables.REPORT_FIELDS, DBConstants.REPORT_UUID, par);
        db.delete(Tables.REPORT_NOTES, DBConstants.REPORT_UUID, par);
        if (delete > 0) {
            final ContentValues cv = new ContentValues();
            cv.put(ID_COLUMN, id);
            db.insert(Tables.REMOVE_REPORTS, null, cv);
        }
        return delete > 0;
    }

    public void markSync(long localId, int serverId) {
        final ContentValues cv = new ContentValues();
        cv.put(Keys.SERVER_ID, serverId);
        cv.put(SYNC_COLUMN, true);
        
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.update(Tables.REPORTS, cv, "id=?", new String[]{String.valueOf(localId)});
    }

    public LinkedList<Report> getUnSyncReports() {
        final LinkedList<Report> reports = new LinkedList<>();
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORTS, null, SYNC_COLUMN + "=?", new String[]{String.valueOf(false)}, null, null, null);
        if (query.moveToFirst()){
            reportParser.init(query);
            do {
                reports.add(reportParser.parse(query));
            } while (query.moveToNext());

        }
        return reports;
    }


    public String[] getRemoved() {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] result;
        final Cursor query = database.query(Tables.REMOVE_REPORTS, null, null, null, null, null, null);
        if (query.moveToFirst()){
            result = new String[query.getCount()];
            int i = 0;
            final int idColumn = query.getColumnIndex(ID_COLUMN);
            do {
                result[i] = query.getString(idColumn);
                i++;
            } while (query.moveToNext());
        } else {
            result = new String[0];
        }
        database.close();
        return result;
    }

    public void forgotAbout(String uuid) {
        final SQLiteDatabase da = dbHelper.getWritableDatabase();
        da.delete(Tables.REMOVE_REPORTS, "id=?", new String[]{uuid});
        da.close();
    }

    public void checkSync() {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORTS, null, SYNC_COLUMN + "=?", new String[]{"0"}, null, null, null);

        final LinkedList<Report> reports = new LinkedList<>();
        if(query.moveToFirst()){
            reportParser.init(query);
            do{
                reports.add(reportParser.parse(query));
            } while (query.moveToNext());
        }

        final Cursor cursor = database.query(Tables.REMOVE_REPORTS, null, null, null, null, null, null);
        final LinkedList<String> removeIds = new LinkedList<>();
        if (cursor.moveToFirst()){
            final int idColumn = cursor.getColumnIndex(ID_COLUMN);
            do {
                removeIds.add(cursor.getString(idColumn));
            } while (cursor.moveToNext());
        }
        database.close();
        syncUtil.saveReports(reports, removeIds);
    }

    public SimpleReport getActiveReport() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor query = db.query(Tables.REPORTS, null, DONE_COLUMN + " is NULL", null, null, null, null);
        SimpleReport report;
        if (query.moveToFirst()){
            simpleParser.init(query);
            do{
                report = simpleParser.parse(query);
                if (report.getLeaveTime() != null){
                    return report;
                }
            } while (query.moveToNext());
        }
        return null;
    }
}

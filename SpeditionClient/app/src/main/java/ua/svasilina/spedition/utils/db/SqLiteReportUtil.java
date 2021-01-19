package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.db.parsers.SqlReportParser;
import ua.svasilina.spedition.utils.sync.SyncUtil;

public class SqLiteReportUtil extends AbstractReportUtil {

    private static final String ONE_ROW = "1";
    private static final String SYNC_COLUMN = "is_sync";
    private static final String ID_COLUMN = "id";
    private static final String LEAVE_COLUMN = "leave_time";
    private static final String DONE_COLUMN = "done_time";
    private static final String ROUTE_COLUMN = "route";
    private static final String PRODUCT_COLUMN = "product";
    private static final String SEPARATED_PRODUCT_COLUMN = "separated_products";
    private final String[] simpleReportFields = new String[]{
            ID_COLUMN,
            Keys.UUID,
            LEAVE_COLUMN,
            DONE_COLUMN,
            ROUTE_COLUMN,
            PRODUCT_COLUMN,
            SEPARATED_PRODUCT_COLUMN,
            SYNC_COLUMN
    };
    private final ExpensesUtil expensesUtil;
    private final ReportDetailUtil detailUtil;
    private final ReportFieldUtil reportFieldUtil;
    private final NoteUtil noteUtil;
    private final SyncUtil syncUtil;

    private final DBHelper dbHelper;
    private final SqlReportParser reportParser;
    private final SimpleParser simpleParser;
    private static final int LIST_SIZE = 10;

    public SqLiteReportUtil(Context context) {
        super(context);
        dbHelper = new DBHelper(context);
        reportParser = new SqlReportParser(context);
        simpleParser = new SimpleParser(context);
        reportFieldUtil = new ReportFieldUtil(context);
        detailUtil = new ReportDetailUtil(context);
        expensesUtil = new ExpensesUtil(context);
        noteUtil = new NoteUtil(context);
        syncUtil = new SyncUtil(context, this);
    }

    private static final String[] NON_SYNC = new String[]{String.valueOf(0)};

    @Override
    Report[] getNotSyncReports() {

        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORTS, null, SYNC_COLUMN + "=?", NON_SYNC, null, null, null);
        Report[] reports = new Report[query.getCount()];
        int i = 0;
        if (query.moveToFirst()){
            reportParser.init(query);
            do {
                reports[i++] = reportParser.parse(query);
            } while (query.moveToNext());
        }

        database.close();
        return reports;
    }

    @Override
    String[] getRemovedReports() {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] result = new String[0];
        try {
            final Cursor query = database.query(Tables.REMOVE_REPORTS, null, null, null, null, null, null);
            result = new String[query.getCount()];
            if (query.moveToFirst()) {
                int i = 0;
                final int idColumn = query.getColumnIndex(ID_COLUMN);
                do {
                    result[i++] = query.getString(idColumn);
                } while (query.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        database.close();
        return result;
    }

    @Override
    public void markSync(String uuid) {
        final ContentValues cv = new ContentValues();
        cv.put(SYNC_COLUMN, true);

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.update(Tables.REPORTS, cv, DBConstants.UUID_PARAM, new String[]{uuid});
        database.close();
    }

    @Override
    AbstractReportUtil getDataSource(Context context) {
        return new FileReportUtil(context);
    }

    public LinkedList<SimpleReport> getReports(){
        final LinkedList<SimpleReport> reports = new LinkedList<>();
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORTS, simpleReportFields, null, null, null, null, null);
        final HashMap<String, Boolean> isSyncMap = new HashMap<>();
        if(query.moveToFirst()){

            simpleParser.init(query);
            int isSyncColumn = query.getColumnIndex(SYNC_COLUMN);

            do {
                final SimpleReport report = simpleParser.parse(query);
                reports.add(report);
                final int sync = query.getInt(isSyncColumn);
                isSyncMap.put(report.getUuid(), sync == 1);
            } while (query.moveToNext());
        }

        for (SimpleReport report :reports){
            detailUtil.getDetails(report,database);
        }
        database.close();
        Collections.sort(reports);

        int removeItems = reports.size() - LIST_SIZE;
        for (int i = reports.size() - 1; i >= 0 && removeItems > 0; i--){
            final SimpleReport report = reports.get(i);
            final String uuid = report.getUuid();
            if (report.isDone() && isSyncMap.containsKey(uuid)){
                removeItems--;
                removeReport(uuid, false);
            }
        }
        return reports;
    }

    @Override
    public Report getReport(String uuid){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor query = db.query(Tables.REPORTS, null, DBConstants.UUID_PARAM, new String[]{uuid}, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            reportParser.init(query);
            return reportParser.parse(query);
        }

        return null;
    }

    public void saveReport(Report report) {
        final ContentValues cv = new ContentValues();
        String uuid = report.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            report.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);

        final Calendar leaveTime = report.getLeaveTime();
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

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{uuid};
        final Cursor query = db.query(Tables.REPORTS, new String[]{Keys.ID}, DBConstants.UUID_PARAM, args, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            db.update(Tables.REPORTS, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.REPORTS, null, cv);
        }
        db.close();

        detailUtil.saveDetails(report);

        reportFieldUtil.saveFields(report);
        syncUtil.saveThread(report);
        expensesUtil.saveExpenses(uuid, report.getExpenses(), ExpenseType.expense);
        expensesUtil.saveExpenses(uuid, report.getFares(), ExpenseType.fare);
        noteUtil.saveNotes(uuid, report.getNotes());
        syncUtil.saveThread(report);
    }

    public boolean removeReport(String id, boolean remoteRemove) {
        String[] par = new String[]{id};
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int delete = db.delete(Tables.REPORTS, DBConstants.UUID_PARAM, par);

        db.delete(Tables.REPORT_DETAILS, DBConstants.REPORT_UUID, par);
        db.delete(Tables.REPORT_FIELDS, DBConstants.REPORT_UUID, par);
        db.delete(Tables.REPORT_NOTES, DBConstants.REPORT_UUID, par);
        db.delete(Tables.EXPENSES, DBConstants.REPORT_UUID, par);
        //weights
        if (remoteRemove && delete > 0) {
            final ContentValues cv = new ContentValues();
            cv.put(Keys.ID, id);
            db.insert(Tables.REMOVE_REPORTS, null, cv);
        }
        db.close();
        return delete > 0;
    }

    public void forgotAbout(String uuid) {
        final SQLiteDatabase da = dbHelper.getWritableDatabase();
        da.delete(Tables.REMOVE_REPORTS, "id=?", new String[]{uuid});
        da.close();
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

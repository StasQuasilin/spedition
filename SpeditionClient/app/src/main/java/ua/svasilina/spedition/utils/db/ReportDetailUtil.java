package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;

import static ua.svasilina.spedition.constants.DBConstants.DETAIL_AND_FIELD_PARAM;
import static ua.svasilina.spedition.constants.DBConstants.ONE_ROW;
import static ua.svasilina.spedition.constants.DBConstants.UUID_PARAM;

public class ReportDetailUtil {

    private static final String ID_COLUMN = "id";
    private static final String DRIVER_COLUMN = "driver";
    private static final String REPORT_COLUMN = "report";
    private static final String OWN_WEIGHT_COLUMN = "own_weight";
    private final DriverUtil driverUtil;
    private final WeightUtil weightUtil;
    private final DBHelper helper;

    public ReportDetailUtil(Context context) {
        helper = new DBHelper(context);
        driverUtil = new DriverUtil(context);
        weightUtil = new WeightUtil(context);
    }

    public void getDetails(Report report){
        final String id = report.getUuid();
        final LinkedList<ReportDetail> details = report.getDetails();
        getDetails(id, details);
    }

    public void getDetails(String reportId, LinkedList<ReportDetail> details){
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor query = db.query(Tables.REPORT_DETAILS, null, "report=?", new String[]{reportId}, null, null, null);
        if (query.moveToFirst()){
            final int idColumn = query.getColumnIndex(ID_COLUMN);
            final int uuidColumn = query.getColumnIndex(Keys.UUID);
            final int driverColumn = query.getColumnIndex(DRIVER_COLUMN);
            final int ownWeightColumn = query.getColumnIndex(OWN_WEIGHT_COLUMN);
            do{
                ReportDetail detail = new ReportDetail();
                detail.setId(query.getInt(idColumn));
                detail.setUuid(query.getString(uuidColumn));

                final String driverId = query.getString(driverColumn);
                final Driver driver = driverUtil.getDriver(driverId);
                detail.setDriver(driver);
                final String weightUUID = query.getString(ownWeightColumn);
                if(weightUUID != null) {
                    final Weight weight = weightUtil.getWeight(weightUUID);
                    detail.setOwnWeight(weight);
                }
                getCounterpartyWeight(detail, db);
                details.add(detail);

            }while (query.moveToNext());
        }

        db.close();
    }

    private void getCounterpartyWeight(ReportDetail detail, SQLiteDatabase db) {
        final String uuid = detail.getUuid();
        final Cursor query = db.query(Tables.COUNTERPARTY_WEIGHT, null, DBConstants.DETAIL_PARAM, new String[]{uuid}, null, null, null);
        if (query.moveToFirst()){
            final int fieldColumn = query.getColumnIndex(Keys.FIELD);
            final int weightColumn = query.getColumnIndex(Keys.WEIGHT);
            do {
                final String field = query.getString(fieldColumn);
                final String weightUuid = query.getString(weightColumn);
                final Weight weight = weightUtil.getWeight(weightUuid);
                detail.setCounterpartyWeight(field, weight);
            } while (query.moveToNext());
        }
    }

    public void saveDetail(ReportDetail detail, Report report, SQLiteDatabase db){
        final ContentValues cv = new ContentValues();

        cv.put(REPORT_COLUMN, report.getUuid());
        String uuid = detail.getUuid();
        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            detail.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);
        final Driver driver = detail.getDriver();
        if(driver != null){
            driverUtil.saveDriver(driver);
            cv.put(DRIVER_COLUMN, driver.getUuid());
        } else {
            cv.put(DRIVER_COLUMN, -1);
        }
        final Weight ownWeight = detail.getOwnWeight();
        if(ownWeight != null){
            weightUtil.saveWeight(ownWeight);
            cv.put(OWN_WEIGHT_COLUMN, ownWeight.getUuid());
        } else {
            cv.put(OWN_WEIGHT_COLUMN, -1);
        }
        String[] id = new String[]{uuid};

        final Cursor query = db.query(Tables.REPORT_DETAILS, null, UUID_PARAM, id, null, null, null, ONE_ROW);
        if(query.moveToFirst()){
            db.update(Tables.REPORT_DETAILS, cv, UUID_PARAM, id);
        } else {
            db.insert(Tables.REPORT_DETAILS, null, cv);
        }
        saveCounterpartyWeight(uuid, detail.getCounterpartyWeight(), db);
    }

    private void saveCounterpartyWeight(String uuid, HashMap<String, Weight> counterpartyWeight, SQLiteDatabase db) {
        ContentValues cv;
        for (Map.Entry<String, Weight> entry : counterpartyWeight.entrySet()){
            cv = new ContentValues();
            cv.put(Keys.DETAIL, uuid);
            final String key = entry.getKey();
            cv.put(Keys.FIELD, key);
            final Weight weight = entry.getValue();
            weightUtil.saveWeight(weight);
            cv.put(Keys.WEIGHT, weight.getUuid());
            final String args[] = new String[]{uuid, key};
            final Cursor query = db.query(Tables.COUNTERPARTY_WEIGHT, null, DBConstants.DETAIL_AND_FIELD_PARAM, args, null, null, null, ONE_ROW);
            if (query.moveToFirst()){
                db.update(Tables.COUNTERPARTY_WEIGHT, cv, DETAIL_AND_FIELD_PARAM, args);
            } else {
                db.insert(Tables.COUNTERPARTY_WEIGHT, null, cv);
            }
        }

    }

    public void saveDetails(Report report) {

        final LinkedList<ReportDetail> details = new LinkedList<>();
        getDetails(report.getUuid(), details);

        final SQLiteDatabase db = helper.getWritableDatabase();
        for (ReportDetail detail : report.getDetails()){
            details.remove(detail);
            saveDetail(detail, report, db);
        }

        for (ReportDetail detail : details){
            removeDetail(detail, db);
        }
        db.close();
    }

    private void removeDetail(ReportDetail detail, SQLiteDatabase db) {
        final String id = String.valueOf(detail.getId());
        db.delete(Tables.REPORT_DETAILS, "id=?", new String[]{id});
    }

    public void getDetails(SimpleReport simpleReport) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor query = db.query(Tables.REPORT_DETAILS, new String[]{DRIVER_COLUMN}, "report=?", new String[]{simpleReport.getUuid()}, null, null, null);
        if (query.moveToFirst()){
            final int driverColumn = query.getColumnIndex(DRIVER_COLUMN);

            do{
                final String driverId = query.getString(driverColumn);
                final Driver driver = driverUtil.getDriver(driverId);
                simpleReport.addDriver(driver);
            }while (query.moveToNext());
        }
        db.close();
    }
}

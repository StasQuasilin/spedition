package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Counterparty;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.reports.Report;

import static ua.svasilina.spedition.constants.DBConstants.ONE_ROW;
import static ua.svasilina.spedition.constants.DBConstants.UUID_PARAM;

public class ReportFieldUtil {
    private static final String ARRIVE_TIME = "arrive_time";
    private static final String LEAVE_TIME = "leave_time";
    private final CounterpartyUtil counterpartyUtil;
    private final DBHelper helper;
    public ReportFieldUtil(Context context) {
        helper= new DBHelper(context);
        counterpartyUtil =new CounterpartyUtil(context);
    }

    public void saveField(SQLiteDatabase database, String reportUUID, ReportField field){

        final ContentValues cv = new ContentValues();
        cv.put(Keys.REPORT, reportUUID);
        String uuid = field.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            field.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);
        final Counterparty counterparty = field.getCounterparty();
        if (counterparty != null){
            counterpartyUtil.saveCounterparty(counterparty);
            cv.put(Keys.COUNTERPARTY,counterparty.getUuid());
        } else {
            cv.put(Keys.COUNTERPARTY, Keys.EMPTY);
        }
        final Calendar arriveTime = field.getArriveTime();
        if (arriveTime != null){
            cv.put(ARRIVE_TIME, arriveTime.getTimeInMillis());
        } else {
            cv.put(ARRIVE_TIME, -1);
        }
        final Calendar leaveTime = field.getLeaveTime();
        if (leaveTime!= null){
            cv.put(LEAVE_TIME, leaveTime.getTimeInMillis());
        } else {
            cv.put(LEAVE_TIME, -1);
        }
        final Product product = field.getProduct();
        if(product!= null){
            cv.put(Keys.PRODUCT, product.getId());
        }

        cv.put(Keys.MONEY, field.getMoney());


        final String[] id = new String[]{uuid};
        final Cursor query = database.query(Tables.REPORT_FIELDS, null, UUID_PARAM, id, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            database.update(Tables.REPORT_FIELDS,cv, UUID_PARAM, id);
        } else {
            database.insert(Tables.REPORT_FIELDS, null, cv);
        }
    }

    private HashSet<String> existFields(String reportUUID) {
        final LinkedList<ReportField> rf= new LinkedList<>();
        getFields(reportUUID, rf);
        final HashSet<String> fieldHashMap = new HashSet<>();

        for (ReportField f : rf){
            fieldHashMap.add(f.getUuid());
        }
        return fieldHashMap;
    }

    public void readFields(Report report){

    }

    public void saveFields(Report report) {
        final String reportUuid = report.getUuid();
        final HashSet<String> existFields = existFields(reportUuid);
        final SQLiteDatabase database = helper.getWritableDatabase();
        for (ReportField rf : report.getFields()){
            final String uuid = rf.getUuid();
            existFields.remove(uuid);
            saveField(database, reportUuid, rf);
        }
        String[] p = new String[1];
        for (String key : existFields){
            p[0] = key;
            database.delete(Tables.REPORT_FIELDS, UUID_PARAM, p);
        }
        database.close();
    }

    public void getFields(Report report) {
        final String uuid = report.getUuid();
        final List<ReportField> fields = report.getFields();
        getFields(uuid, fields);
    }

    private void getFields(String reportUuid, List<ReportField> fields) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORT_FIELDS, null, DBConstants.REPORT_UUID, new String[]{reportUuid}, null, null, null);
        if (query.moveToFirst()){
            final int uuidColumn = query.getColumnIndex(Keys.UUID);
            final int counterpartColumn = query.getColumnIndex(Keys.COUNTERPARTY);
            final int arriveColumn = query.getColumnIndex(ARRIVE_TIME);
            final int leaveColumn = query.getColumnIndex(LEAVE_TIME);
            do{
                final ReportField field = new ReportField();
                field.setUuid(query.getString(uuidColumn));
                final String counterpartyUuid = query.getString(counterpartColumn);
                if(counterpartyUuid != null && !counterpartyUuid.isEmpty()){
                    field.setCounterparty(counterpartyUtil.getCounterparty(counterpartyUuid));
                }
                final long aLong = query.getLong(arriveColumn);
                if (aLong > 0){
                    Calendar arrive = Calendar.getInstance();
                    arrive.setTimeInMillis(aLong);
                    field.setArriveTime(arrive);
                }
                final long lLong = query.getLong(leaveColumn);
                if (lLong > 0){
                    Calendar leave = Calendar.getInstance();
                    leave.setTimeInMillis(lLong);
                    field.setLeaveTime(leave);
                }
                fields.add(field);
            } while (query.moveToNext());
        }
        database.close();
    }
}

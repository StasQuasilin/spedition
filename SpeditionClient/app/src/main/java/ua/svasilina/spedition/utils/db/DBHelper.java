package ua.svasilina.spedition.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db";
    private static final String TAG = "Db Helper";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Create table 'Products'");
        db.execSQL("create table " + Tables.PRODUCTS + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "name text)");
        Log.i(TAG, "Create table 'Drivers'");
        db.execSQL("create table " + Tables.DRIVERS + " (" +
                "id integer unique primary key autoincrement," +
                "uuid text," +
                "server_id integer default -1," +
                "surname text," +
                "forename text," +
                "patronymic text)");

        Log.i(TAG, "Create table 'Counterparty'");
        db.execSQL("create table " + Tables.COUNTERPARTY + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "uuid text," +
                "name text)");
        Log.i(TAG, "Create table 'Last sync'");
        db.execSQL("create table " + Tables.LAST_SYNC + " (" +
                "id integer primary key autoincrement, " +
                "table_name text," +
                "time text)");
        Log.i(TAG, "Create table 'Reports'");
        db.execSQL("create table " + Tables.REPORTS + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "uuid text," +
                "leave_time long," +
                "done_time long," +
                "route text," +
                "product integer," +
                "separated_products boolean," +
                "per_diem integer," +
                "fone boolean," +
                "is_sync boolean)");
        Log.i(TAG, "Create table 'Report Details'");
        db.execSQL("create table " + Tables.REPORT_DETAILS + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "uuid text," +
                "report text not null," +
                "driver integer," +
                "product integer," +
                "own_weight integer)");
        Log.i(TAG, "Create table 'Report Fields'");
        db.execSQL("create table " + Tables.REPORT_FIELDS + " (" +
                "id integer unique primary key autoincrement," +
                "uuid text," +
                "server_id integer unique," +
                "report integer not null," +
                "counterparty integer," +
                "arrive_time long," +
                "leave_time long," +
                "product integer," +
                "money integer," +
                "weight integer)");
        createCounterpartyWeightTable(db);
        Log.i(TAG, "Create table 'Expenses'");
        db.execSQL("create table " + Tables.EXPENSES + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "report integer not null," +
                "type integer," +
                "description text," +
                "amount integer)");
        Log.i(TAG, "Create table 'Report Notes'");
        db.execSQL("create table " + Tables.REPORT_NOTES + " (" +
                "id integer unique primary key autoincrement," +
                "server_id integer unique," +
                "report integer not null," +
                "time text," +
                "note text)");
        Log.i(TAG, "Create table 'Location Log'");
        db.execSQL("create table " + Tables.LOCATION_LOG + " (" +
                "id integer unique primary key autoincrement," +
                "report integer not null," +
                "time time," +
                "latitude float," +
                "longitude float)");
        Log.i(TAG, "Create table 'Remove reports");
        db.execSQL("create table " + Tables.REMOVE_REPORTS + " (" +
                "id text)");
        createWeightsTable(db);
    }

    private void createCounterpartyWeightTable(SQLiteDatabase db) {
        Log.i(TAG, "Create table 'Counterparty Weights'");
        db.execSQL("create table " + Tables.COUNTERPARTY_WEIGHT + " (" +
                "id integer unique primary key autoincrement," +
                "detail text," +
                "field text," +
                "weight text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Old version: " + oldVersion + ", new version: " + newVersion);
        if (oldVersion == 1){
            createWeightsTable(db);
        }
        if(oldVersion <= 2){
            db.execSQL("alter table " + Tables.REPORT_FIELDS + " add uuid text");
        }
        if( oldVersion <= 3){
            db.execSQL("alter table " + Tables.COUNTERPARTY + " add uuid text");
        }
        if (oldVersion <= 4){
            createCounterpartyWeightTable(db);
        }
    }

    private void createWeightsTable(SQLiteDatabase db) {
        Log.i(TAG, "Create table 'Weights'");
        db.execSQL("create table " + Tables.WEIGHTS + " (" +
                "id integer unique primary key autoincrement," +
                "uuid text," +
                "gross integer," +
                "tare intger)" );

    }
}

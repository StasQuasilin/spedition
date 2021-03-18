package ua.svasilina.spedition.utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.activity.StartActivity;
import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.network.Connector;

import static ua.svasilina.spedition.constants.Keys.TOKEN;

public class DBUtil {

    private static final String TAG = "DB Util";
    private final Context context;
    private final LoginUtil loginUtil;
    private final DBHelper helper;

    public DBUtil(Context context){
        helper = new DBHelper(context);
        this.context = context;
        loginUtil = new LoginUtil(context);
    }

    private TextView message;
    private View loadGroup;
    private TextView loadTitle;
    private ProgressBar loadProgress;
    private TextView loadStatus;

    public void syncDB(final StartActivity view, final OnDone onDone){

        message = view.findViewById(R.id.textInfo);
        loadGroup = view.findViewById(R.id.loadGroup);
        loadTitle = view.findViewById(R.id.loadTitle);
        loadProgress = view.findViewById(R.id.loadProgres);
        loadStatus = view.findViewById(R.id.loadStatus);

        final HashMap<String,String> data = new HashMap<>();

        final SQLiteDatabase db = helper.getWritableDatabase();

        data.put(Keys.PRODUCTS, getLastSync(Tables.PRODUCTS, db));
//        data.put(Keys.PRODUCTS, null);
        data.put(Keys.DRIVERS, getLastSync(Tables.DRIVERS, db));
//        data.put(Keys.DRIVERS, null);
        data.put(Keys.COUNTERPARTY, getLastSync(Tables.COUNTERPARTY, db));
//        data.put(Keys.COUNTERPARTY, null);
        data.put(Keys.VERSION, String.valueOf(getVersion()));

        Log.i(TAG, data.toString());
        sendJson(ApiLinks.SYNC_REFERENCES,
            new JSONObject(data), response -> {
            try {
                final String status = response.getString(Keys.STATUS);
                if (status.equals(Keys.SUCCESS)){
                    final JSONArray products = response.getJSONArray(Keys.PRODUCTS);
                    if (products.length() > 0) {
                        load(R.string.sync_products, products, id -> syncProducts(id, db));
                        updateLastSync(Tables.PRODUCTS, db);
                    }
                    final JSONArray drivers = response.getJSONArray(Keys.DRIVERS);
                    if (drivers.length() > 0){
                        load(R.string.sync_drivers, drivers, id -> syncDrivers(id, db));
                        updateLastSync(Tables.DRIVERS, db);
                    }
                    if(response.has(Keys.COUNTERPARTY)){
                        final JSONArray counterparty = response.getJSONArray(Keys.COUNTERPARTY);

                        final int length = counterparty.length();
                        if (length > 0){
                            load(R.string.sync_counterparty, counterparty, id -> syncCounterparty(id, db));
                            updateLastSync(Tables.COUNTERPARTY, db);
                        }
                    }
                    Toast.makeText(context, R.string.sync_success, Toast.LENGTH_LONG).show();
                    db.close();
                    onDone.done();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                final String msg = context.getResources().getString(R.string.json_error) + " " + e.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                db.close();
                onDone.done();
            }
//                updateData(response);
            }, error -> {
                error.printStackTrace();
                final String msg = context.getResources().getString(R.string.sync_error) + " " + error.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                db.close();
                onDone.done();
            });
    }

    private long getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        long versionCode = -1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            if (pInfo != null) {
                versionCode = pInfo.getLongVersionCode();
            }
        } else {
            if (pInfo != null) {
                versionCode = pInfo.versionCode;
            }
        }
        return versionCode;
    }

    private void updateData(JSONObject response) {
        updateItems(response, Keys.P);
        updateItems(response, Keys.D);
        updateItems(response, Keys.C);
    }

    private void updateItems(JSONObject response, String key) {
        if (response.has(key)) {
            try {
                final JSONObject jsonObject = response.getJSONObject(key);
                final JSONArray update = jsonObject.getJSONArray(Keys.UPDATE);
                final JSONArray remove = jsonObject.getJSONArray(Keys.REMOVE);

                for (int i = 0; i < update.length(); i++) {
                    final Object o = update.get(i);
                    int id = Integer.parseInt(String.valueOf(o));

                }
                for (int i = 0; i < remove.length(); i++) {
                    final Object o = remove.get(i);
                    int id = Integer.parseInt(String.valueOf(o));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void load(int title, JSONArray array, Loader loader) {
        loadTitle.setText(title);
        final int length = array.length();
        loadProgress.setMax(length);
        for (int i = 0; i < length; i++) {
            loadProgress.setProgress(i);
            loadStatus.setText(i + "/" + length);
            final int anInt;
            try {
                anInt = array.getInt(i);
                loader.load(anInt);

                loadProgress.setProgress(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncCounterparty(int anInt, final SQLiteDatabase db) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, anInt);
        sendJson(ApiLinks.GET_COUNTERPARTY, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject counterparty = response.getJSONObject(Keys.COUNTERPARTY);
                        saveCounterparty(counterparty, db);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void syncDrivers(int driverId, final SQLiteDatabase db) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, driverId);
        sendJson(ApiLinks.GET_DRIVER, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject driver = response.getJSONObject(Keys.DRIVER);
                        saveDriver(driver, db);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void syncProducts(int productId, final SQLiteDatabase db) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, productId);
        sendJson(ApiLinks.GET_PRODUCT, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject product = response.getJSONObject(Keys.PRODUCT);
                        saveProduct(product, db);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void saveProduct(JSONObject product, SQLiteDatabase db) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String serverId = product.getString(Keys.ID);
        cv.put(Keys.SERVER_ID, serverId);
        cv.put(Keys.NAME, product.getString(Keys.NAME));
        if (!db.isOpen()){
            db = helper.getWritableDatabase();
        }
        final Cursor query = db.query(Tables.PRODUCTS, new String[]{}, "server_id=?", new String[]{serverId}, null, null, null);
        if (query.moveToFirst()){
            db.update(Tables.PRODUCTS, cv, "server_id=?", new String[]{serverId});
        } else {
            db.insert(Tables.PRODUCTS, null, cv);
        }
    }

    private void sendJson(String api, JSONObject json,  Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError){
        final String token = loginUtil.getToken();
        if (token != null) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    api, json, onSuccess,
                    onError) {
                @Override
                public Map<String, String> getHeaders() {
                    final HashMap<String, String> map = new HashMap<>();
                    map.put(TOKEN, token);
                    map.put("content-type", "application/json; charset=utf-8");
                    return map;
                }
            };
            Connector.getConnector().addRequest(context, request);
        }
    }

    private void saveDriver(JSONObject driver, SQLiteDatabase db) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String uuid = driver.getString(Keys.UUID);
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.SERVER_ID, driver.getInt(Keys.ID));
        cv.put(Keys.SURNAME, driver.getString(Keys.SURNAME).toUpperCase());
        cv.put(Keys.FORENAME, driver.getString(Keys.FORENAME).toUpperCase());
        cv.put(Keys.PATRONYMIC, driver.getString(Keys.PATRONYMIC).toUpperCase());
        final String[] args = new String[]{uuid};
        if (!db.isOpen()){
            db = helper.getWritableDatabase();
        }
        final Cursor query = db.query(Tables.DRIVERS, new String[]{}, DBConstants.UUID_PARAM, args, null, null, null, DBConstants.ONE_ROW);
        if (query.moveToFirst()){
            db.update(Tables.DRIVERS, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.DRIVERS, null, cv);
        }
    }

    private void saveCounterparty(JSONObject counterparty, SQLiteDatabase db) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String uuid = counterparty.getString(Keys.UUID);
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.SERVER_ID, counterparty.getString(Keys.ID));
        cv.put(Keys.NAME, counterparty.getString(Keys.NAME).toUpperCase());
        String[] args = new String[]{uuid};
        if (!db.isOpen()){
            db = helper.getWritableDatabase();
        }
        final Cursor query = db.query(Tables.COUNTERPARTY, new String[]{}, DBConstants.UUID_PARAM, args, null, null, null);
        if (query.moveToFirst()){
            db.update(Tables.COUNTERPARTY, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.COUNTERPARTY, null, cv);
        }
    }

    private void updateLastSync(String table, SQLiteDatabase db) {
        final ContentValues cv = new ContentValues();
        cv.put("table_name", table);
        cv.put("time", Calendar.getInstance().getTimeInMillis());

        final String lastSync = getLastSync(table, db);
        if (lastSync == null){
            db.insert(Tables.LAST_SYNC, null, cv);
        } else {
            db.update(Tables.LAST_SYNC, cv, "table_name=?", new String[]{table});
        }
    }

    private String getLastSync(String table, SQLiteDatabase db) {
        Cursor cursor = db.query(Tables.LAST_SYNC, null, "table_name=?", new String[]{table},null,null,null);
        int timeColIndex = cursor.getColumnIndex("time");
        if (cursor.moveToFirst()){
            return cursor.getString(timeColIndex);
        }
        return null;
    }

    public void getReports(OnDone onDone) {
        sendJson(ApiLinks.GET_REPORTS, new JSONObject(), response -> {
            System.out.println(response);
            onDone.done();
        }, error -> {
            onDone.done();
        });

    }
}

package ua.svasilina.spedition.utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
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
    private final SQLiteDatabase db;
    private final Connector connector;
    private final Context context;
    private final LoginUtil loginUtil;


    public DBUtil(Context context){
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        connector = Connector.getConnector();
        this.context = context;
        loginUtil = new LoginUtil(context);
    }

    private TextView message;
    private View loadGroup;
    private TextView loadTitle;
    private ProgressBar loadProgress;
    private TextView loadStatus;

    public void syncDB(final StartActivity view, final OnSyncDone onSyncDone){
        syncDB(view, onSyncDone, loginUtil.getToken());
    }

    public void syncDB(final StartActivity view, final OnSyncDone onSyncDone, final String token){
        message = view.findViewById(R.id.textInfo);
        loadGroup = view.findViewById(R.id.loadGroup);
        loadTitle = view.findViewById(R.id.loadTitle);
        loadProgress = view.findViewById(R.id.loadProgres);
        loadStatus = view.findViewById(R.id.loadStatus);

        final HashMap<String,String> data = new HashMap<>();

        data.put(Keys.PRODUCTS, getLastSync(Tables.PRODUCTS));
//        data.put(Keys.PRODUCTS, null);
//        data.put(Keys.DRIVERS, getLastSync(Tables.DRIVERS));
        data.put(Keys.DRIVERS, null);
        data.put(Keys.COUNTERPARTY, getLastSync(Tables.COUNTERPARTY));
//        data.put(Keys.COUNTERPARTY, null);

        Log.i(TAG, data.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiLinks.SYNC_REFERENCES,
                new JSONObject(data), new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONArray products = response.getJSONArray(Keys.PRODUCTS);
                        if (products.length() > 0) {
                            loadTitle.setText(R.string.sync_products);
                            loadProgress.setMax(products.length());
                            for (int i = 0; i < products.length(); i++) {
                                loadStatus.setText(i + "/" + products.length());
                                final int anInt = products.getInt(i);
                                syncProducts(anInt);
                                loadProgress.setProgress(i);
                            }
                            updateLastSync(Tables.PRODUCTS);
                        }
                        final JSONArray drivers = response.getJSONArray(Keys.DRIVERS);
                        if (drivers.length() > 0){
                            loadTitle.setText(R.string.sync_drivers);
                            loadProgress.setMax(drivers.length());
                            for (int i = 0; i < drivers.length(); i++){
                                loadStatus.setText(i + "/" + drivers.length());
                                final int anInt = drivers.getInt(i);
                                syncDrivers(anInt);
                                loadProgress.setProgress(i);
                            }
                            updateLastSync(Tables.DRIVERS);
                        }
                        if(response.has(Keys.COUNTERPARTY)){
                            final JSONArray counterparty = response.getJSONArray(Keys.COUNTERPARTY);
                            final int length = counterparty.length();
                            if (length > 0){

                                loadTitle.setText(R.string.sync_counterparty);
                                loadProgress.setMax(length);
                                for (int i = 0; i < length; i++){
                                    loadStatus.setText(i + "/" + length);
                                    final int anInt = drivers.getInt(i);
                                    syncCounterparty(anInt);
                                    loadProgress.setProgress(i);
                                }
                            }
                            updateLastSync(Tables.COUNTERPARTY);
                        }
                        Toast.makeText(context, R.string.sync_success, Toast.LENGTH_LONG).show();
                        onSyncDone.done();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    final String msg = context.getResources().getString(R.string.sync_error) + " " + e.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    onSyncDone.done();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                final String msg = context.getResources().getString(R.string.sync_error) + " " + error.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                onSyncDone.done();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                final HashMap<String, String> map = new HashMap<>();
                map.put(TOKEN, token);
                map.put("content-type", "application/json; charset=utf-8");
                return map;
            }
        };
        connector.addRequest(context, request);
    }

    private void syncCounterparty(int anInt) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, anInt);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiLinks.GET_COUNTERPARTY, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject counterparty = response.getJSONObject(Keys.COUNTERPARTY);
                        saveCounterparty(counterparty);
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
        connector.addRequest(context, request);
    }

    private void syncDrivers(int driverId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, driverId);
        sendJson(ApiLinks.GET_DRIVER, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject driver = response.getJSONObject(Keys.DRIVER);
                        saveDriver(driver);
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

    private void syncProducts(int productId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Keys.ID, productId);
        sendJson(ApiLinks.GET_PRODUCT, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String status = response.getString(Keys.STATUS);
                    if (status.equals(Keys.SUCCESS)){
                        final JSONObject product = response.getJSONObject(Keys.PRODUCT);
                        saveProduct(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void saveProduct(JSONObject product) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String serverId = product.getString(Keys.ID);
        cv.put(Keys.SERVER_ID, serverId);
        cv.put(Keys.NAME, product.getString(Keys.NAME));

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

    private void saveDriver(JSONObject driver) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String uuid = driver.getString(Keys.UUID);
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.SERVER_ID, driver.getInt(Keys.ID));
        cv.put(Keys.SURNAME, driver.getString(Keys.SURNAME).toUpperCase());
        cv.put(Keys.FORENAME, driver.getString(Keys.FORENAME).toUpperCase());
        cv.put(Keys.PATRONYMIC, driver.getString(Keys.PATRONYMIC).toUpperCase());
        final String[] args = new String[]{uuid};
        final Cursor query = db.query(Tables.DRIVERS, new String[]{}, DBConstants.UUID_PARAM, args, null, null, null, DBConstants.ONE_ROW);
        if (query.moveToFirst()){
            db.update(Tables.DRIVERS, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.DRIVERS, null, cv);
        }
    }

    private void saveCounterparty(JSONObject counterparty) throws JSONException {
        final ContentValues cv = new ContentValues();
        final String id = counterparty.getString(Keys.ID);
        cv.put(Keys.SERVER_ID, id);
        cv.put(Keys.NAME, counterparty.getString(Keys.NAME));

        final Cursor query = db.query(Tables.COUNTERPARTY, new String[]{}, "server_id=?", new String[]{id}, null, null, null);
        if (query.moveToFirst()){
            db.update(Tables.COUNTERPARTY, cv, "server_id = ?", new String[]{id});
        } else {
            db.insert(Tables.COUNTERPARTY, null, cv);
        }
    }

    private void updateLastSync(String table) {
        final ContentValues cv = new ContentValues();
        cv.put("table_name", table);
        cv.put("time", Calendar.getInstance().getTimeInMillis());
        Log.i(TAG, cv.toString());
        final String lastSync = getLastSync(table);
        if (lastSync == null){
            Log.i(TAG, "Insert time value for table " + table);
            db.insert(Tables.LAST_SYNC, null, cv);
        } else {
            Log.i(TAG, "Update time value for table " + table);
            db.update(Tables.LAST_SYNC, cv, "table_name=?", new String[]{table});
        }
    }

    private String getLastSync(String table) {
        Cursor cursor = db.query(Tables.LAST_SYNC, null, "table_name=?", new String[]{table},null,null,null);
        int timeColIndex = cursor.getColumnIndex("time");
        if (cursor.moveToFirst()){
            return cursor.getString(timeColIndex);
        } else {
            Log.e(TAG, "No lines in table 'Last Sync'");
        }
        return null;
    }
}

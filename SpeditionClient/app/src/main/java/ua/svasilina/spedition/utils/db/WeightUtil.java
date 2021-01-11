package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Weight;

import static ua.svasilina.spedition.constants.DBConstants.ONE_ROW;
import static ua.svasilina.spedition.constants.DBConstants.UUID_PARAM;

public class WeightUtil {
    private final DBHelper helper;
    public WeightUtil(Context context) {
        this.helper = new DBHelper(context);
    }

    public void saveWeight(Weight weight) {
        final ContentValues cv = new ContentValues();

        String uuid = weight.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            weight.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.GROSS, weight.getGross());
        cv.put(Keys.TARE, weight.getTare());
        final SQLiteDatabase db = helper.getWritableDatabase();
        final String[] id = new String[]{uuid};
        final Cursor query = db.query(Tables.WEIGHTS, null, UUID_PARAM, id, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            db.update(Tables.WEIGHTS, cv, UUID_PARAM, id);
        } else {
            db.insert(Tables.WEIGHTS, null, cv);
        }

        db.close();
    }

    public Weight getWeight(String uuid){
        Weight weight = null;
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor query = db.query(Tables.WEIGHTS, null, UUID_PARAM, new String[]{uuid}, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            final int idColumn = query.getColumnIndex(Keys.ID);
            final int grossColumn = query.getColumnIndex(Keys.GROSS);
            final int tareColumn = query.getColumnIndex(Keys.TARE);

            weight= new Weight();
            weight.setId(query.getInt(idColumn));
            weight.setUuid(uuid);
            weight.setGross(query.getInt(grossColumn));
            weight.setTare(query.getInt(tareColumn));

        }
        db.close();
        return weight;
    }
}

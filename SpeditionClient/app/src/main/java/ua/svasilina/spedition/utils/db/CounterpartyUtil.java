package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Counterparty;
import ua.svasilina.spedition.utils.db.parsers.CounterpartyParser;

import static ua.svasilina.spedition.constants.DBConstants.ONE_ROW;
import static ua.svasilina.spedition.constants.DBConstants.UUID_PARAM;

public class CounterpartyUtil {
    private final DBHelper helper;
    private final CounterpartyParser parser;
    public CounterpartyUtil(Context context) {
        helper = new DBHelper(context);
        parser = new CounterpartyParser();
    }

    public void saveCounterparty(Counterparty counterparty){
        final ContentValues values = new ContentValues();
        String uuid = counterparty.getUuid();
        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            counterparty.setUuid(uuid);
        }
        values.put(Keys.UUID, uuid);
        values.put(Keys.NAME, counterparty.getName());
        final SQLiteDatabase database = helper.getWritableDatabase();
        String[] p = new String[]{uuid};
        final Cursor query = database.query(Tables.COUNTERPARTY, null, UUID_PARAM, p, null, null, null, ONE_ROW);
        if(query.moveToFirst()){
            database.update(Tables.COUNTERPARTY, values, UUID_PARAM, p);
        } else {
            database.insert(Tables.COUNTERPARTY,null, values);
        }
        database.close();
    }

    public Counterparty getCounterparty(String counterpartyUuid) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        Counterparty counterparty = null;
        final Cursor query = database.query(Tables.COUNTERPARTY, null, UUID_PARAM, new String[]{counterpartyUuid}, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            parser.init(query);
            counterparty = parser.parse(query);
        }
        database.close();
        return counterparty;
    }
}

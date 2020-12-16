package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.utils.db.parsers.DriverParser;

import static ua.svasilina.spedition.constants.DBConstants.ONE_ROW;

public class DriverUtil {

    DBHelper helper;
    final DriverParser parser;

    public DriverUtil(Context context) {
        helper = new DBHelper(context);
        parser = new DriverParser();
    }

    public void saveDriver(Driver driver) {
        final ContentValues cv = new ContentValues();
        String uuid = driver.getUuid();
        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            driver.setUuid(uuid);
        }
        cv.put(Keys.UUID, uuid);

        final Person person = driver.getPerson();
        final String surname = person.getSurname();
        if(surname != null) {
            cv.put(Keys.SURNAME, surname.toUpperCase());
        }
        final String forename = person.getForename();
        if (forename != null){
            cv.put(Keys.FORENAME, forename.toUpperCase());
        }
        final String patronymic = person.getPatronymic();
        if (patronymic != null){
            cv.put(Keys.PATRONYMIC, patronymic.toUpperCase());
        }
        final SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = new String[]{uuid};
        final Cursor query = db.query(Tables.DRIVERS, new String[]{}, DBConstants.UUID_PARAM, args, null, null, null, "1");
        if (query.moveToFirst()){
            db.update(Tables.DRIVERS, cv, DBConstants.UUID_PARAM, args);
        } else {
            db.insert(Tables.DRIVERS, null, cv);
        }
        db.close();
    }

    public Driver getDriver(String uuid) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor query = db.query(Tables.DRIVERS, null, DBConstants.UUID_PARAM, new String[]{uuid}, null, null, null, ONE_ROW);
        if (query.moveToFirst()){
            parser.init(query);
            db.close();
            return parser.parse(query);
        }
        db.close();
        return null;
    }
}

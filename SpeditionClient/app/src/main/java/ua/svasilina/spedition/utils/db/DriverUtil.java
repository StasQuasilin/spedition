package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.UUID;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;

public class DriverUtil {

    private static final String TAG = "Find Driver Util";
    private static final String SURNAME = Keys.SURNAME;
    private static final String FORENAME = Keys.FORENAME;
    private static final String PATRONYMIC = Keys.PATRONYMIC;
    private static final String SERVER_ID = Keys.SERVER_ID;
    private final SQLiteDatabase db;

    public DriverUtil(Context context) {
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public LinkedList<Driver> findDrivers(String key){
        LinkedList<Driver> drivers = new LinkedList<>();
        final Cursor query = db.query(Tables.DRIVERS, null, Keys.SURNAME + " LIKE ?", new String[]{'%' + key.toUpperCase() + '%'}, null, null, null);
        if (query.moveToFirst()){
            final int serverIdIdx = query.getColumnIndex(Keys.ID);
            final int surnameIdx = query.getColumnIndex(Keys.SURNAME);
            final int forenameIdx = query.getColumnIndex(Keys.FORENAME);
            final int patronymicIdx = query.getColumnIndex(Keys.PATRONYMIC);
            do {
                final int id = query.getInt(serverIdIdx);
                final String surname = query.getString(surnameIdx);
                final String forename = query.getString(forenameIdx);
                final String patronymic = query.getString(patronymicIdx);
                Driver driver = new Driver();
                driver.setId(id);
                final Person person = driver.getPerson();
                person.setSurname(surname);
                person.setForename(forename);
                person.setPatronymic(patronymic);
                drivers.add(driver);

            } while (query.moveToNext());
        } else {
            Log.i(TAG, "Drivers by key '" + key + "' not found");
        }
        return drivers;
    }

    public void saveDriver(Driver driver) {
        final ContentValues cv = new ContentValues();
        final String id = String.valueOf(driver.getId());
        String uuid = driver.getUuid();
        if(uuid== null){
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

        final Cursor query = db.query(Tables.DRIVERS, new String[]{}, "id=?", new String[]{id}, null, null, null, "1");
        if (query.moveToFirst()){
            db.update(Tables.DRIVERS, cv, "id=?", new String[]{id});
        } else {
            final long insert = db.insert(Tables.DRIVERS, null, cv);
            System.out.println(insert);
            driver.setId(insert);
        }
    }

    public Driver getDriver(String uuid) {

        final Cursor query = db.query(Tables.DRIVERS, null, "uuid=?", new String[]{String.valueOf(uuid)}, null, null, null, String.valueOf(1));
        if (query.moveToFirst()){
            final int idColumn = query.getColumnIndex(Keys.ID);
            final int serverIdColumn = query.getColumnIndex(SERVER_ID);
            final int surnameColumn = query.getColumnIndex(SURNAME);
            final int forenameColumn = query.getColumnIndex(FORENAME);
            final int patronymicColumn = query.getColumnIndex(PATRONYMIC);

            Driver driver = new Driver();
            driver.setId(query.getInt(idColumn));
            driver.setUuid(uuid);
            driver.setServerId(query.getInt(serverIdColumn));
            final Person person = driver.getPerson();
            person.setSurname(query.getString(surnameColumn));
            person.setForename(query.getString(forenameColumn));
            person.setPatronymic(query.getString(patronymicColumn));
            return driver;
        }
        return null;
    }
}

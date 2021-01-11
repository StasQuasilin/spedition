package ua.svasilina.spedition.utils.db.parsers;

import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.utils.search.ItemParser;

public class DriverParser extends ItemParser<Driver> {

    private int uuidColumn;
    private int serverIdColumn;
    private int surnameIdx;
    private int forenameIdx;

    @Override
    public void init(Cursor query) {
        uuidColumn = query.getColumnIndex(Keys.UUID);
        serverIdColumn = query.getColumnIndex(Keys.SERVER_ID);
        surnameIdx = query.getColumnIndex(Keys.SURNAME);
        forenameIdx = query.getColumnIndex(Keys.FORENAME);
    }

    @Override
    public Driver parse(Cursor query) {
        final int serverId = query.getInt(serverIdColumn);
        final String uuid = query.getString(uuidColumn);
        final String surname = query.getString(surnameIdx);
        final String forename = query.getString(forenameIdx);
        Driver driver = new Driver();
        driver.setUuid(uuid);
        driver.setServerId(serverId);
        final Person person = driver.getPerson();
        person.setSurname(surname);
        person.setForename(forename);
        return driver;
    }
}

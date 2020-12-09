package ua.svasilina.spedition.utils.db.parsers;

import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.utils.search.ItemParser;

public class DriverParser extends ItemParser<Driver> {

    private int idIdx;

    private int serverIdColumn;
    private int surnameIdx;
    private int forenameIdx;

    @Override
    public void init(Cursor query) {
        idIdx = query.getColumnIndex(Keys.ID);
        serverIdColumn = query.getColumnIndex(Keys.SERVER_ID);
        surnameIdx = query.getColumnIndex(Keys.SURNAME);
        forenameIdx = query.getColumnIndex(Keys.FORENAME);
    }

    @Override
    public Driver parse(Cursor query) {
        final int id = query.getInt(idIdx);
        final int serverId = query.getInt(serverIdColumn);
        final String surname = query.getString(surnameIdx);
        final String forename = query.getString(forenameIdx);
        Driver driver = new Driver();
        driver.setId(id);
        driver.setServerId(serverId);
        final Person person = driver.getPerson();
        person.setSurname(surname);
        person.setForename(forename);
        return driver;
    }
}

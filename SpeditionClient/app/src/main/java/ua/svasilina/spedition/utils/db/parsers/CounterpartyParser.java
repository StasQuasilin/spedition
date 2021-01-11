package ua.svasilina.spedition.utils.db.parsers;

import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Counterparty;
import ua.svasilina.spedition.utils.search.ItemParser;

public class CounterpartyParser extends ItemParser<Counterparty> {

    private int idColumn;
    private int uuidColumn;
    private int nameColumn;

    @Override
    public void init(Cursor query) {
        idColumn = query.getColumnIndex(Keys.ID);
        uuidColumn = query.getColumnIndex(Keys.UUID);
        nameColumn = query.getColumnIndex(Keys.NAME);
    }

    @Override
    public Counterparty parse(Cursor query) {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(query.getInt(idColumn));
        counterparty.setUuid(query.getString(uuidColumn));
        counterparty.setName(query.getString(nameColumn));
        return counterparty;
    }
}

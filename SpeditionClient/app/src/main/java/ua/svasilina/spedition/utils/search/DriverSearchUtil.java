package ua.svasilina.spedition.utils.search;

import android.content.Context;

import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.utils.db.Tables;
import ua.svasilina.spedition.utils.db.parsers.DriverParser;

public class DriverSearchUtil extends SearchUtil<Driver> {

    public DriverSearchUtil(Context context) {
        super(context, new DriverParser());
    }

    @Override
    String getTableName() {
        return Tables.DRIVERS;
    }

    private static final String SEARCH_PHRASE = "surname like ? or forename like ?";

    @Override
    String getSearchPhrase() {
        return SEARCH_PHRASE;
    }
}

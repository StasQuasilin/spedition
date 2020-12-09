package ua.svasilina.spedition.utils.search;

import android.content.Context;

import ua.svasilina.spedition.entity.Counterparty;
import ua.svasilina.spedition.utils.db.Tables;
import ua.svasilina.spedition.utils.db.parsers.CounterpartyParser;

public class CounterpartySearchUtil extends SearchUtil<Counterparty> {

    public CounterpartySearchUtil(Context context) {
        super(context, new CounterpartyParser());
    }

    @Override
    String getTableName() {
        return Tables.COUNTERPARTY;
    }

    @Override
    String getSearchPhrase() {
        return "name like ?";
    }
}

package ua.svasilina.spedition.utils.db;

import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Expense;
import ua.svasilina.spedition.utils.search.ItemParser;

public class ExpenseParser extends ItemParser<Expense> {

    private int uuidField;
    private int descriptionField;
    private int amountField;

    @Override
    public void init(Cursor query) {
        uuidField = query.getColumnIndex(Keys.UUID);
        descriptionField = query.getColumnIndex(Keys.DESCRIPTION);
        amountField = query.getColumnIndex(Keys.AMOUNT);
    }

    @Override
    public Expense parse(Cursor query) {
        Expense expense = new Expense();
        expense.setUuid(query.getString(uuidField));
        expense.setDescription(query.getString(descriptionField));
        expense.setAmount(query.getInt(amountField));
        return expense;
    }
}

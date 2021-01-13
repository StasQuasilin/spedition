package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Expense;

public class ExpensesUtil {
    private static final String[] UUID_COLUMN =new String[]{Keys.UUID};
    private static final String EXPENSE_SELECTOR = "report=? and type=?";
    DBHelper helper;
    ExpenseParser parser;
    public ExpensesUtil(Context context){
        helper = new DBHelper(context);
        parser = new ExpenseParser();
    }

    public void saveExpenses(String reportUuid, List<Expense> expenses, ExpenseType type){
        for (Expense e : expenses){
            saveExpense(reportUuid, e, type);
        }
    }

    private void saveExpense(String reportUuid, Expense expense, ExpenseType type) {
        ContentValues cv = new ContentValues();
        String uuid = expense.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
        }
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.REPORT, reportUuid);
        cv.put(Keys.TYPE, type.getValue());
        cv.put(Keys.DESCRIPTION, expense.getDescription());
        cv.put(Keys.AMOUNT, expense.getAmount());
        final SQLiteDatabase database = helper.getWritableDatabase();
        String[] args = new String[]{uuid};
        final Cursor query = database.query(Tables.EXPENSES, UUID_COLUMN, DBConstants.UUID_PARAM, args, null, null, null);
        if (query.moveToFirst()){
            database.update(Tables.EXPENSES, cv, DBConstants.UUID_PARAM, args);
        } else {
            database.insert(Tables.EXPENSES, null, cv);
        }
        database.close();
    }
    private final String[] expenseSelectorFields = new String[2];
    public void getExpenses(List<Expense> list, String reportUuid, ExpenseType type){
        expenseSelectorFields[0] = reportUuid;
        expenseSelectorFields[1] = String.valueOf(type.getValue());
        final SQLiteDatabase database = helper.getReadableDatabase();
        final Cursor query = database.query(Tables.EXPENSES, null, EXPENSE_SELECTOR, expenseSelectorFields, null, null, null);
        if (query.moveToFirst()){
            parser.init(query);
            do{
                list.add(parser.parse(query));
            } while (query.moveToNext());
        }
    }
}

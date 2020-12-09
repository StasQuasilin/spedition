package ua.svasilina.spedition.utils.search;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.LinkedList;

import ua.svasilina.spedition.utils.db.DBHelper;

public abstract class SearchUtil<T> {

    final DBHelper helper;
    final ItemParser<T> parser;
    final String searchPhrase;
    final int insertsCount;
    final String[] p;

    public SearchUtil(Context context, ItemParser<T> parser){
        helper = new DBHelper( context);
        this.parser = parser;
        searchPhrase = getSearchPhrase();
        insertsCount = insertsCount(searchPhrase);
        p = new String[insertsCount];
    }

    public void search(ArrayAdapter<T> adapter, String key){
        adapter.clear();
        adapter.addAll(findItems(key));
    }
    private LinkedList<T> findItems(String key){
        LinkedList<T> items = new LinkedList<>();
        final String k = "%" + key + "%";
        final SQLiteDatabase database = helper.getReadableDatabase();

        for (int i = 0 ;i < insertsCount; i++){
            p[i] = k;
        }

        final Cursor query = database.query(getTableName(), null, searchPhrase, p, null, null, null);

        if (query.moveToFirst()){
            parser.init(query);
            do {
                items.add(parser.parse(query));
            } while (query.moveToNext());
        }
        database.close();
        return items;
    }
    private static final char SIGN = '?';
    private int insertsCount(String searchPhrase) {
        int count = 0;
        for (char c :searchPhrase.toCharArray()){
            if (c == SIGN){
                count++;
            }
        }
        return count;
    }

    abstract String getTableName();
    abstract String getSearchPhrase();
}

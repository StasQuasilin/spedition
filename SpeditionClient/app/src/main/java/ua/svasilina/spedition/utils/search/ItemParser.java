package ua.svasilina.spedition.utils.search;

import android.database.Cursor;

public abstract class ItemParser<T> {
    public abstract void init(Cursor query);
    public abstract T parse(Cursor query);
}

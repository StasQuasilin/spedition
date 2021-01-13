package ua.svasilina.spedition.utils.db;

import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.ReportNote;
import ua.svasilina.spedition.utils.search.ItemParser;

public class NoteParser extends ItemParser<ReportNote> {

    private int uuidColumn, noteColumn, timeColumn;

    @Override
    public void init(Cursor query) {
        uuidColumn= query.getColumnIndex(Keys.UUID);
        noteColumn = query.getColumnIndex(Keys.NOTE);
        timeColumn = query.getColumnIndex(Keys.TIME);
    }

    @Override
    public ReportNote parse(Cursor query) {
        ReportNote note = new ReportNote();
        note.setUuid(query.getString(uuidColumn));
        note.setNote(query.getString(noteColumn));
        note.setTime(query.getLong(timeColumn));
        return note;
    }
}

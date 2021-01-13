package ua.svasilina.spedition.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import ua.svasilina.spedition.constants.DBConstants;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.ReportNote;

public class NoteUtil {

    private static final String SELECTION = "report=?";
    final DBHelper helper;
    final NoteParser parser;

    public NoteUtil(Context context){
        helper = new DBHelper(context);
        parser = new NoteParser();
    }

    public void saveNotes(String reportUuid, List<ReportNote> notes) {
        for (ReportNote note : notes){
            saveNote(reportUuid,note);
        }
    }

    private void saveNote(String reportUuid, ReportNote note) {
        String uuid = note.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            note.setUuid(uuid);
        }

        Calendar time = note.getTime();
        if(time == null){
            time = Calendar.getInstance();
        }

        ContentValues cv = new ContentValues();
        cv.put(Keys.UUID, uuid);
        cv.put(Keys.REPORT, reportUuid);
        cv.put(Keys.NOTE, note.getNote());
        cv.put(Keys.TIME, time.getTimeInMillis());

        final SQLiteDatabase database = helper.getWritableDatabase();
        String[] args = new String[]{uuid};
        final Cursor query = database.query(Tables.REPORT_NOTES, new String[]{Keys.UUID}, DBConstants.UUID_PARAM, args, null, null, null);
        if (query.moveToFirst()){
            database.update(Tables.REPORT_NOTES, cv, DBConstants.UUID_PARAM, args);
        } else {
            database.insert(Tables.REPORT_NOTES, null, cv);
        }
        database.close();
    }

    public void getNotes(List<ReportNote> notes, String reportUuid) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        final Cursor query = database.query(Tables.REPORT_NOTES, null, SELECTION, new String[]{reportUuid}, null, null, null);
        if (query.moveToFirst()){
            parser.init(query);
            do {
                notes.add(parser.parse(query));
            } while (query.moveToNext());
        }
    }
}

package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.db.DBHelper;

public class TableViewDialog extends DialogFragment {

    private final Context context;
    private final LayoutInflater inflater;
    private final String table;
    private DBHelper helper;
    public TableViewDialog(String table, Context context) {
        this.context = context;
        this.table = table;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        helper = new DBHelper(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.tables_view, null);

        final CustomAdapter<String> adapter = new CustomAdapter<>(context, android.R.layout.simple_list_item_1, new CustomAdapterBuilder<String>() {
            @Override
            public void build(String item, View view, int position) {
                final TextView text1 = view.findViewById(android.R.id.text1);
                text1.setText(item);
            }
        });

        final ListView tablesList = view.findViewById(R.id.tables_list);
        tablesList.setAdapter(adapter);
        adapter.addAll(readTable());
        builder.setView(view);
        builder.setTitle(table);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }
    final static String[] EMPTY = new String[]{"Nothing to show"};
    private String[] readTable() {
        final SQLiteDatabase database = helper.getReadableDatabase();
        String[] strings;
        final Cursor query = database.query(table, null, null, null, null, null, null);
        if (query.moveToFirst()){
            strings = new String[query.getCount()];
            final String[] names = query.getColumnNames();
            final int[] indices = new int[names.length];
            for(int i = 0; i< indices.length; i++){
                indices[i] = query.getColumnIndex(names[i]);
            }
            StringBuilder builder;
            int j = 0;
            do {
                builder = new StringBuilder();
                for (int i = 0; i < indices.length;i++){
                    builder.append(names[i]).append(Keys.COLON);
                    builder.append(query.getString(indices[i]));
                    builder.append(Keys.NEW_ROW);
                }
                strings[j] = builder.toString();
                j++;
            } while (query.moveToNext());
            database.close();
            return strings;
        } else {
            database.close();
            return EMPTY;
        }

    }
}

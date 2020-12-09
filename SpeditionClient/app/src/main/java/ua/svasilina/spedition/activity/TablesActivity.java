package ua.svasilina.spedition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.dialogs.TableViewDialog;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.db.Tables;

public class TablesActivity extends AppCompatActivity {

    TableViewDialog tableViewDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tables_view);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.tables);
        }
        final ListView tablesList = findViewById(R.id.tables_list);

        final CustomAdapter<String> adapter = new CustomAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new CustomAdapterBuilder<String>() {
            @Override
            public void build(final String item, View view, int position) {
                final TextView text1 = view.findViewById(android.R.id.text1);
                text1.setText(item.toUpperCase());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableViewDialog = new TableViewDialog(item, getApplicationContext());
                        tableViewDialog.show(getSupportFragmentManager(), "TV");
                    }
                });
            }
        });
        
        for (Field f: Tables.class.getDeclaredFields()){
            adapter.add(f.getName().toLowerCase());
        }

        tablesList.setAdapter(adapter);
    }
}

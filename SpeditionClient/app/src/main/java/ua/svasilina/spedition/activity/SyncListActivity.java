package ua.svasilina.spedition.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.entity.sync.SyncList;
import ua.svasilina.spedition.entity.sync.SyncListItem;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.sync.SyncListUtil;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;

import static ua.svasilina.spedition.constants.Keys.EMPTY;
import static ua.svasilina.spedition.constants.Patterns.DATE_TIME_PATTERN;

public class SyncListActivity extends AppCompatActivity {

    private SyncListUtil syncListUtil;
    private ListView listView;
    private DateTimeBuilder dateTimeBuilder = new DateTimeBuilder(DATE_TIME_PATTERN);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_list);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.syncList);
        syncListUtil = new SyncListUtil(getApplicationContext());
        listView = findViewById(R.id.syncList);
        initList();
    }

    private void initList() {
        final Context context = getApplicationContext();
        final SyncList list = syncListUtil.readSyncList();
        CustomAdapterBuilder<SyncListItem> builder = new CustomAdapterBuilder<SyncListItem>() {

            @Override
            public void build(SyncListItem item, View view, int position) {
                final TextView reportView = view.findViewById(R.id.reportId);
                final TextView timeView = view.findViewById(R.id.syncTime);

                reportView.setText(item.getReport());
                final Calendar syncTime = item.getSyncTime();
                if (syncTime != null){
                    timeView.setText(dateTimeBuilder.build(syncTime));
                } else {
                    timeView.setText(EMPTY);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        CustomAdapter<SyncListItem> adapter = new CustomAdapter<>(context, R.layout.sync_item_view, builder);
        adapter.addAll(list.getFields());
        listView.setAdapter(adapter);
    }
}

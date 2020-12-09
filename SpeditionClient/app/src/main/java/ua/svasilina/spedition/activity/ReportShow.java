package ua.svasilina.spedition.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Expense;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;
import ua.svasilina.spedition.utils.builders.WeightStringBuilder;
import ua.svasilina.spedition.utils.db.ReportUtil;

import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.NEW_ROW;

public class ReportShow extends AppCompatActivity {

    private TextView driverView;
    private TextView routeView;
    private TextView productView;
    private TextView weightView;
    private TextView leaveView;
    private View fareContainer;
    private ListView fareList;
    private View expensesContainer;
    private ListView expensesList;
    private TextView doneView;
    private ListView fieldList;
    private WeightStringBuilder weightStringBuilder;
    private Report report;
    DateTimeBuilder dateTimeBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_show);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.show_title);
        weightStringBuilder = new WeightStringBuilder(getResources());
        dateTimeBuilder = new DateTimeBuilder("HH:mm dd.MM.yy");
        driverView = findViewById(R.id.driverButton);
        routeView = findViewById(R.id.routeButton);
        productView = findViewById(R.id.productSpinner);
        weightView = findViewById(R.id.weightButton);
        leaveView = findViewById(R.id.leaveText);
        fareContainer = findViewById(R.id.fareBox);
        fareList = findViewById(R.id.fareList);
        expensesContainer = findViewById(R.id.expensesContainer);
        expensesList = findViewById(R.id.expensesList);
        doneView = findViewById(R.id.doneView);
        fieldList = findViewById(R.id.fields);

        final Intent intent = getIntent();
        final long id = intent.getLongExtra(ID, -1);
        ReportUtil reportUtil = new ReportUtil(getApplicationContext());
        report = reportUtil.getReport(id);
        buildReport();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.show_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.close){
            final Context context = getApplicationContext();
            Intent intent = new Intent(context, Reports.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (itemId == R.id.copy){

        }
        return false;
    }

    private void buildReport() {
        final LinkedList<ReportDetail> details = report.getDetails();
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (ReportDetail detail : details){
            final Driver driver = detail.getDriver();
            if (driver != null){
                builder.append(driver.getValue());
            }
            if(i < details.size()- 1){
                builder.append(NEW_ROW);
            }
            i++;
        }
        driverView.setText(builder.toString().toUpperCase());

//        routeView.setText(report.getRoute());
        productView.setText(report.getProduct().getName());
//        final Weight weight = report.getWeight();
//        if (weight != null){
//            weightView.setText(weightStringBuilder.build(weight));
//        } else {
//            weightView.setVisibility(View.GONE);
//        }
        leaveView.setText(dateTimeBuilder.build(report.getLeaveTime()));
        initExpenses(fareContainer, fareList, report.getFares());
        initExpenses(expensesContainer, expensesList, report.getExpenses());
        doneView.setText(dateTimeBuilder.build(report.getDoneDate()));
        initFields();
    }

    private void initFields() {
        CustomAdapterBuilder<ReportField> builder = new CustomAdapterBuilder<ReportField>() {
            @Override
            public void build(ReportField item, View view, int position) {
                final TextView indexView = view.findViewById(R.id.indexView);
                final TextView arriveView = view.findViewById(R.id.arriveTimeView);
                final TextView leaveView = view.findViewById(R.id.leaveTimeView);
                final TextView productView = view.findViewById(R.id.productView);
                final TextView actionView = view.findViewById(R.id.actionView);
                final TextView amountView = view.findViewById(R.id.amountView);
                final View weightContainer = view.findViewById(R.id.weightBox);
                final TextView grossView = view.findViewById(R.id.grossView);
                final TextView tareView = view.findViewById(R.id.tareView);
                final TextView netView = view.findViewById(R.id.netView);

                indexView.setText(String.valueOf(position + 1));
                final Calendar arriveTime = item.getArriveTime();
                if (arriveTime != null) {
                    arriveView.setText(dateTimeBuilder.build(item.getArriveTime()));
                }

                final Calendar leaveTime = item.getLeaveTime();
                if (leaveTime != null){
                    leaveView.setText(dateTimeBuilder.build(leaveTime));
                }
                
                if (item.getProduct() != null){
                    productView.setText(item.getProduct().getName());
                }

                final int money = item.getMoney();
                final Resources resources = getResources();
                if (money != 0) {
                    if (money > 0){
                        actionView.setText(resources.getString(R.string.discarded));
                    } else {
                        actionView.setText(resources.getString(R.string.give));
                    }
                    amountView.setText(String.valueOf(Math.abs(money)));
                } else {
                    actionView.setVisibility(View.GONE);
                    amountView.setVisibility(View.GONE);
                }

//                final Weight weight = report.getWeight();
//                if (weight == null){
//                    weightContainer.setVisibility(View.GONE);
//                } else {
//                    grossView.setText(String.valueOf(weight.getGross()));
//                    tareView.setText(String.valueOf(weight.getTare()));
//                 +   netView.setText(String.valueOf(weight.getNet()));
//                }
            }
        };

        CustomAdapter<ReportField> adapter = new CustomAdapter<>(getApplicationContext(), R.layout.report_field_view, builder);
        fieldList.setAdapter(adapter);
        adapter.addAll(report.getFields());
    }

    private void initExpenses(View container, ListView view, ArrayList<Expense> list) {
        if (list.size() > 0){
            SimpleListAdapter<Expense> adapter = new SimpleListAdapter<>(getApplicationContext(), R.layout.simple_list_item, null);
            adapter.addAll(list);
            view.setAdapter(adapter);
        } else {
            container.setVisibility(View.GONE);
        }
    }


}

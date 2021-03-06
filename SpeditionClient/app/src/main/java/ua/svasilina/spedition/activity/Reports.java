package ua.svasilina.spedition.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.ReportListAdapter;
import ua.svasilina.spedition.dialogs.AlertActiveReportDialog;
import ua.svasilina.spedition.dialogs.LoginDialog;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.db.SqLiteReportUtil;

public class Reports extends AppCompatActivity {

    private final ArrayList<SimpleReport> reports = new ArrayList<>();
    private long backPressedTime;
    private Toast backToast;
    private boolean haveActive = false;
    private String activeReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        SqLiteReportUtil reportUtil = new SqLiteReportUtil(context);

        final LinkedList<SimpleReport> reportsList = reportUtil.getReports();
        reports.addAll(reportsList);
        ReportListAdapter adapter = new ReportListAdapter(context, R.layout.report_list_row, this.reports);
        ListView view = findViewById(R.id.report_list);
        if (view != null){
            view.setAdapter(adapter);
        }
        checkActives();
    }

    private void checkActives(){
        haveActive = false;
        activeReport = null;
        for (SimpleReport report : reports){
            haveActive = report.isActive();
            if(haveActive){
                activeReport = report.getUuid();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            final MenuItem item = menu.findItem(R.id.versionCode);
            long versionCode;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                versionCode = pInfo.getLongVersionCode();
            } else {
                versionCode = pInfo.versionCode;
            }
            item.setTitle(pInfo.versionName + " ( " + versionCode + " )");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.add){
            if (haveActive){
                new AlertActiveReportDialog(getApplicationContext(), activeReport).show(getSupportFragmentManager(), null);
            } else {
                newItem();
            }
        } else if (itemId == R.id.login){
            showLoginDialog();
        } else if (itemId == R.id.versionCode){
            showTables();
        } else if(itemId == R.id.location){
            final Context context = getApplicationContext();
            Intent intent = new Intent(context, LocationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTables() {
        final Context applicationContext = getApplicationContext();
        Intent intent = new Intent(applicationContext, TablesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    public void showLoginDialog(){
        LoginDialog.showLoginDialog(getApplicationContext(),getSupportFragmentManager(), null);
    }

    private void newItem(){
        final Context context = getApplicationContext();
        Intent intent = new Intent(context, ReportEdit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finish();
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.pressBack, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundWorkerUtil.getInstance().runWorker(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

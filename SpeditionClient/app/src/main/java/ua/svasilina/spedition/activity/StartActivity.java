package ua.svasilina.spedition.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.dialogs.LoginDialog;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.background.OnActiveReport;
import ua.svasilina.spedition.utils.db.DBUtil;
import ua.svasilina.spedition.utils.db.OnSyncDone;
import ua.svasilina.spedition.utils.db.ReportUtil;

public class StartActivity extends AppCompatActivity {

    private DBUtil dbUtil;
    private ReportUtil reportUtil;
    private OnActiveReport onActiveReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();

        LoginUtil loginUtil = new LoginUtil(context);
        reportUtil = new ReportUtil(context);
        onActiveReport = new OnActiveReport(context);

        setContentView(R.layout.start_activity);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setTitle(R.string.sync_title);
        }
        if (loginUtil.getToken() != null) {
            dbUtil = new DBUtil(getApplicationContext());
            syncNow();
        } else {
            LoginDialog.showLoginDialog(context, getSupportFragmentManager(), new OnSyncDone(){
                @Override
                public void done() {
                syncNow();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "'Back' not support yet", Toast.LENGTH_SHORT).show();
    }

    private void syncNow(){
        reportUtil.checkSync();
        dbUtil.syncDB(this, new OnSyncDone() {
            @Override
            public void done() {
                Intent intent = new Intent(getApplicationContext(), Reports.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
        onActiveReport.checkReports();
    }
}

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
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.background.OnActiveReport;
import ua.svasilina.spedition.utils.db.AbstractReportUtil;
import ua.svasilina.spedition.utils.db.DBUtil;
import ua.svasilina.spedition.utils.db.OnDone;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "Start Activity";
    private DBUtil dbUtil;
    private AbstractReportUtil reportUtil;
    private OnActiveReport onActiveReport;
    LoginUtil loginUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();

        BackgroundWorkerUtil.getInstance().stopWorker(context);

        loginUtil = new LoginUtil(context);

        reportUtil = AbstractReportUtil.getReportUtil(context);

        onActiveReport = new OnActiveReport(context);
        dbUtil = new DBUtil(context);

        setContentView(R.layout.start_activity);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setTitle(R.string.sync_title);
        }

        checkToken(context);

    }

    void checkToken(final Context context){
        if (loginUtil.getToken() != null) {
            syncNow();
        } else {
            LoginDialog.showLoginDialog(context, getSupportFragmentManager(), new OnDone(){
                @Override
                public void done() {
                checkToken(context);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "'Back' not support yet", Toast.LENGTH_SHORT).show();
    }

    private void syncNow(){
        reportUtil.syncReports();
        dbUtil.syncDB(this, new OnDone() {
            @Override
            public void done() {
                Intent intent = new Intent(getApplicationContext(), Reports.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
        onActiveReport.checkReports();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

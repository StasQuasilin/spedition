package ua.svasilina.spedition.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.util.Arrays;

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
    private static final int UPDATE_REQUEST_CODE = 1805;
    private static final int GEO_PERMISSION_REQ_CODE = 1806;
    private DBUtil dbUtil;
    private AbstractReportUtil reportUtil;
    private OnActiveReport onActiveReport;
    LoginUtil loginUtil;
    private Handler freshnessHandler;
    private Handler permissionHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();
        BackgroundWorkerUtil.getInstance().stopWorker(context);
        freshnessHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                checkPermissions(context);
            }
        };
        permissionHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                nextStep(context);
            }
        };

        checkFreshness(context);
    }

    private void nextStep(Context context) {
        loginUtil = new LoginUtil(context);

        reportUtil = AbstractReportUtil.getReportUtil(context);

        onActiveReport = new OnActiveReport(context);
        dbUtil = new DBUtil(context);

        setContentView(R.layout.start_activity);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.sync_title);
        }

        checkToken(context);
    }

    private void checkPermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionRequest();
        } else {
            System.out.println("GEO PERMISSION GRANTED");
            permissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println(Arrays.toString(permissions) + ":" + Arrays.toString(grantResults));
        if (requestCode == GEO_PERMISSION_REQ_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//            }
            permissionGranted();
        }
    }

    private void permissionGranted() {
        permissionHandler.sendMessage(new Message());
    }

    private void permissionRequest() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            System.out.println(">>>>>>>");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GEO_PERMISSION_REQ_CODE);
        }
    }

    private void checkFreshness(Context context) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                System.out.println("NEED UPDATE IT");
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("NO NEED UPDATES");
                freshnessHandler.sendMessage(new Message());
            }
        });
        appUpdateInfoTask.addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UPDATE_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                System.out.println("UPDATE SUCCESSFULLY");
            } else if (resultCode == RESULT_CANCELED){
                System.out.println("USER CANCEL IT");
            } else {
                System.out.println("UPDATE ERROR: " + resultCode);
            }
        }
        freshnessHandler.sendMessage(new Message());
        super.onActivityResult(requestCode, resultCode, data);
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
        dbUtil.syncDB(this, () -> {
            //after sync
            dbUtil.getReports(() -> {
                Intent intent = new Intent(getApplicationContext(), Reports.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            });
        });
        onActiveReport.checkReports();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

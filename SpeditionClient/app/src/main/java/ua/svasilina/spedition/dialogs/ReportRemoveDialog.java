package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.activity.Reports;
import ua.svasilina.spedition.utils.background.OnActiveReport;
import ua.svasilina.spedition.utils.db.ReportUtil;

public class ReportRemoveDialog extends DialogFragment {

    private final String uuid;
    private final ReportUtil reportsUtil;
    private final Context context;
    private final OnActiveReport onActiveReport;

    public ReportRemoveDialog(Context context, String uuid) {
        this.uuid = uuid;
        reportsUtil = new ReportUtil(context);
        this.context = context;
        onActiveReport = new OnActiveReport(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.remove_title);
        builder.setMessage(R.string.remove_report_message);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeReport();
            }
        });
        return builder.create();
    }

    private void removeReport() {
        if(reportsUtil.removeReport(uuid)) {
            Intent intent = new Intent(context, Reports.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast.makeText(context, R.string.report_remove_success, Toast.LENGTH_LONG).show();
            onActiveReport.checkReports();
        }
    }
}

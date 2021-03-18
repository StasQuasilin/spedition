package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.utils.Opener;

public class AlertActiveReportDialog extends DialogFragment {

    private final Context context;
    private final LayoutInflater inflater;
    private final String activeReport;
    public AlertActiveReportDialog(Context context, String activeReport){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activeReport = activeReport;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.attention);
        final View view = inflater.inflate(R.layout.alert_active_report, null);
        builder.setView(view);
        builder.setNegativeButton(R.string.open_report, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openReport();
            }
        });
        builder.setPositiveButton(R.string.ok, null);

        return builder.create();
    }

    private void openReport() {
        Opener.openReport(context, false, activeReport);
    }
}

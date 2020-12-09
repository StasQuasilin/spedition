package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.OldReport;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.db.ReportUtil;

public class CopyOldReportsDialog extends DialogFragment {
    private final List<OldReport> oldReports;
    private final ReportUtil reportUtil;
    private final LayoutInflater inflater;

    public CopyOldReportsDialog(Context context, List<OldReport> oldReports, List<SimpleReport> reports) {
        this.oldReports = oldReports;
        reportUtil = new ReportUtil(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.copy_old_reports_dialog, null);

        builder.setView(view);
        return builder.create();
    }
}

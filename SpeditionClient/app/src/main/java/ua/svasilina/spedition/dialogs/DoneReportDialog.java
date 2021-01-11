package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.CustomListener;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;

import static ua.svasilina.spedition.constants.Patterns.DATE_PATTERN;
import static ua.svasilina.spedition.constants.Patterns.TIME_PATTERN;

public class DoneReportDialog extends DialogFragment {

    private final LayoutInflater inflater;
    private final Report report;
    private final CustomListener listener;
    private Button dateButton;
    private Button timeButton;
    private final Calendar calendar;
    private final DateTimeBuilder dateBuilder;
    private final DateTimeBuilder timeBuilder;

    public DoneReportDialog(LayoutInflater inflater, Report report, CustomListener listener) {
        this.inflater = inflater;
        this.report = report;
        this.listener = listener;
        calendar = Calendar.getInstance();
        dateBuilder = new DateTimeBuilder(DATE_PATTERN);
        timeBuilder = new DateTimeBuilder(TIME_PATTERN);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.done_report_title);
        final View view = inflater.inflate(R.layout.done_report, null);
        dateButton = view.findViewById(R.id.dateButton);
        timeButton = view.findViewById(R.id.timeButton);
        initDateTimeButtons();
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        return builder.create();
    }

    private void initDateTimeButtons() {
        final CustomListener listener = new CustomListener() {
            @Override
            public void onChange() {
                updateDateTimeButton();
            }
        };
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog = new DateDialog(calendar, inflater, DateDialogState.date, listener);
                dateDialog.show(getParentFragmentManager(), "Date dialog");
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog timeDialog = new DateDialog(calendar, inflater, DateDialogState.time, listener);
                timeDialog.show(getParentFragmentManager(), "Time dialog");
            }
        });
        updateDateTimeButton();
    }

    private void updateDateTimeButton() {
        dateButton.setText(dateBuilder.build(calendar));
        timeButton.setText(timeBuilder.build(calendar));
    }

    private void save() {
        report.setDoneDate(calendar);
        listener.onChange();
    }
}

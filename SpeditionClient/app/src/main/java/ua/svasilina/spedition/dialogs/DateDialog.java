package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.utils.CustomListener;

public class DateDialog extends DialogFragment {

    private final LayoutInflater inflater;
    private final Calendar calendar;
    private final Calendar innerCalendar;
    private final DateDialogState state;
    private final CustomListener onClickListener;

    public DateDialog(Calendar calendar, LayoutInflater inflater, DateDialogState state, CustomListener onClickListener) {
        this.calendar = calendar;
        this.inflater = inflater;
        this.state = state;
        this.onClickListener = onClickListener;
        innerCalendar = Calendar.getInstance();
        innerCalendar.setTime(calendar.getTime());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);

        View view;

        if (state == DateDialogState.time) {
            view = inflater.inflate(R.layout.time_picker_dialog, null);
            final TimePicker timePicker = view.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    innerCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    innerCalendar.set(Calendar.MINUTE, minute);
                }
            });
            final int hour = innerCalendar.get(Calendar.HOUR_OF_DAY);
            final int minute = innerCalendar.get(Calendar.MINUTE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            } else {
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
            }
        } else {
            view = inflater.inflate(R.layout.date_picker_dialog, null);
            final CalendarView calendarView = view.findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    innerCalendar.set(year, month, dayOfMonth);
                }
            });
            calendarView.setDate(innerCalendar.getTime().getTime());
        }

        builder.setView(view);
        return builder.create();
    }

    private void save() {
        calendar.setTime(innerCalendar.getTime());
        onClickListener.onChange();
    }
}

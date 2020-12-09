package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;

public class ReportNotCompletedDialog extends DialogFragment {

    final boolean e1, e2, e3, e4;

    public ReportNotCompletedDialog(boolean driver, boolean route, boolean leave, boolean fields) {
        this.e1 = driver;
        this.e2 = route;
        this.e3 = leave;
        this.e4 = fields;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.no_title);
        final Context context = getContext();
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setPadding(24, 0, 4, 0);
        if (e1){
            TextView textView = new TextView(context);
            textView.setText(R.string.drivers);
            view.addView(textView);
        }
        if (e2){
            TextView textView = new TextView(context);
            textView.setText(R.string.route);
            view.addView(textView);
        }
        if (e3){
            TextView textView = new TextView(context);
            textView.setText(R.string.leaveTime);
            view.addView(textView);
        }
        if (e4){
            TextView textView = new TextView(context);
            textView.setText(R.string.fields);
            view.addView(textView);
        }

        builder.setView(view);

        builder.setPositiveButton(R.string.ok, null);

        return builder.create();
    }
}

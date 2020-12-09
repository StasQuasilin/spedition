package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.services.SyncService;

public class SyncDialog extends DialogFragment {

    private final Context context;
    private final LayoutInflater inflater;

    public SyncDialog(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.sync_dialog, null);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(50);

        builder.setTitle(R.string.sync);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        System.out.println("Dismiss sync dialog");
        context.startService(new Intent(context, SyncService.class));

    }
}

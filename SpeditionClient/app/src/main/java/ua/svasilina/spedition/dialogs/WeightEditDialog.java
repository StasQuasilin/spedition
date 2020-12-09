package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Weight;

public class WeightEditDialog extends DialogFragment {

    private final Weight weight;
    private final Driver driver;
    private final LayoutInflater inflater;
    private EditText editGross;
    private EditText editTare;
    private final SaveWaiter<Weight> saveWaiter;

    public WeightEditDialog(Weight weight, Driver driver, LayoutInflater inflater, SaveWaiter<Weight> saveWaiter) {
        if (weight != null){
            this.weight = weight;
        } else {
            this.weight = new Weight();
        }

        this.driver = driver;
        this.inflater = inflater;
        this.saveWaiter = saveWaiter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.edit_weight, null);
        final TextView driverLabel = view.findViewById(R.id.driverLabel);

        if (driver != null){
            driverLabel.setText(driver.toString());
        }

        editGross = view.findViewById(R.id.editGross);
        editTare = view.findViewById(R.id.editTare);
        final EditText editNet = view.findViewById(R.id.editNet);

        editGross.setText(String.valueOf(weight.getGross()));
        editTare.setText(String.valueOf(weight.getTare()));

        new NetWatcher(editGross, editTare, editNet);
        editGross.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editGross.selectAll();
                }
            }
        });

        editTare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTare.selectAll();
            }
        });

        builder.setTitle(R.string.weightTitle);
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

    private void save() {
        int gross = 0;
        final Editable grossText = editGross.getText();
        if (grossText.length() > 0){
            gross = Integer.parseInt(grossText.toString());
        }
        weight.setGross(gross);
        int tare = 0;
        final Editable tareText = editTare.getText();
        if (tareText.length() > 0){
            tare = Integer.parseInt(tareText.toString());
        }
        weight.setTare(tare);
        saveWaiter.onSave(weight);
    }
}

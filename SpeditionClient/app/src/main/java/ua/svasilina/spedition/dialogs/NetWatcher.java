package ua.svasilina.spedition.dialogs;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NetWatcher implements TextWatcher {

    private final EditText editGross;
    private final EditText editTare;
    private final EditText editNet;

    public NetWatcher(EditText editGross, EditText editTare, EditText editNet) {
        this.editGross = editGross;
        this.editTare = editTare;
        this.editNet = editNet;
        editGross.addTextChangedListener(this);
        editTare.addTextChangedListener(this);
        computeNet();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        computeNet();
    }

    private void computeNet(){
        final String grossString = editGross.getText().toString();
        final String tareString = editTare.getText().toString();
        int net = 0;
        if (!grossString.isEmpty() && !tareString.isEmpty()){
            int gross = Integer.parseInt(grossString);
            int tare = Integer.parseInt(tareString);
            if (gross > 0 && tare > 0){
                net = gross - tare;
            }
        }
        editNet.setText(String.valueOf(net));
    }
}

package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.CustomListener;

public class EditPhonesDialog extends DialogFragment {

    private final Context context;
    private final LayoutInflater inflater;
    private ListView phonesList;
    private EditText phoneEdit;
    private Button addButton;
    private int currentIndex = -1;
    private final ArrayList<String> phones;
    private SimpleListAdapter<String> adapter;
    private final CustomListener listener;

    public EditPhonesDialog(Context context, ArrayList<String> phones, CustomListener listener) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.phones = phones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.phones_edit_title);
        final View view = inflater.inflate(R.layout.route_edit, null);

        phonesList = view.findViewById(R.id.routePoints);
        phoneEdit = view.findViewById(R.id.editRoute);
        phoneEdit.setInputType(InputType.TYPE_CLASS_PHONE);
        addButton = view.findViewById(R.id.addButton);
        phoneEdit.getText().clear();

        initPhoneList();
        initAddButton();

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);

        builder.setView(view);
        return builder.create();
    }

    private void save() {
        phones.clear();
        for (int i = 0; i < adapter.getCount(); i++){
            phones.add(adapter.getItem(i));
        }
        listener.onChange();
    }

    private void initAddButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNumber();
            }
        });
    }

    private void saveNumber() {
        final String number = phoneEdit.getText().toString();
        if (!number.isEmpty()){

            if (currentIndex == -1){
                adapter.add(number);
            } else {
                adapter.insert(number, currentIndex);
                currentIndex = -1;
            }
            phoneEdit.getText().clear();
        }
    }

    private void initPhoneList() {
        final AdapterItemEditInterface<String> listener = new AdapterItemEditInterface<String>() {
            @Override
            public void click(String item, int index) {
                phoneEdit.setText(item);
                currentIndex = index;
            }
        };
        adapter = new SimpleListAdapter<>(context, R.layout.simple_list_item_d, listener);
        adapter.addAll(phones);

        phonesList.setAdapter(adapter);
    }
}

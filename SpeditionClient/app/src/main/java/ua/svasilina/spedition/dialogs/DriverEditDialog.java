package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.utils.CustomListener;

public class DriverEditDialog extends DialogFragment {
    
    private final Context context;
    private final Driver driver;
    private final LayoutInflater inflater;

    private EditText surnameEdit;
    private EditText forenameEdit;
    private EditText patronymicEdit;
    private ImageButton addPhoneButton;
    private final CustomListener customListener;
    private ListView phoneList;
    private SimpleListAdapter<String> adapter;
    private final boolean editNames;

    public DriverEditDialog(Driver driver, Context context, CustomListener customListener, boolean editNames) {
        this.context = context;
        this.driver = driver;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.customListener = customListener;
        this.editNames = editNames;
    }

    public DriverEditDialog(Driver driver, Context context, CustomListener customListener) {
        this(driver, context, customListener, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.driver_edit_dialog, null);

        final Person person = driver.getPerson();
        surnameEdit = view.findViewById(R.id.surnameEdit);
        initInput(surnameEdit, person.getSurname());
        forenameEdit = view.findViewById(R.id.forenameEdit);
        initInput(forenameEdit, person.getForename());
        patronymicEdit = view.findViewById(R.id.patronymicEdit);
        initInput(patronymicEdit, person.getPatronymic());

        addPhoneButton = view.findViewById(R.id.addPhoneButton);
        phoneList= view.findViewById(R.id.phonesList);
        initAddPhoneButton();
        initPhonesList();
        builder.setTitle(R.string.driver_edit);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(view);
        return builder.create();
    }

    private void initInput(EditText input, String value) {
        if (value != null && !value.isEmpty()){
            input.setText(value);
            input.setEnabled(editNames);
        }
    }

    private void initAddPhoneButton() {
        addPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPhonesDialog epd = new EditPhonesDialog(context, driver.getPerson().getPhones(), new CustomListener() {
                    @Override
                    public void onChange() {
                        updatePhoneList();
                    }
                });
                epd.show(getParentFragmentManager(), "Phone edit dialog");
            }
        });
    }

    private void initPhonesList() {
        adapter = new SimpleListAdapter<>(context, android.R.layout.simple_list_item_1, null);
        phoneList.setAdapter(adapter);
        updatePhoneList();
    }

    private void updatePhoneList() {
        adapter.clear();
        adapter.addAll(driver.getPerson().getPhones());
    }

    private void save() {
        final Person person = driver.getPerson();
        person.setSurname(surnameEdit.getText().toString());
        person.setForename(forenameEdit.getText().toString());
        person.setPatronymic(patronymicEdit.getText().toString());
        customListener.onChange();
    }
}

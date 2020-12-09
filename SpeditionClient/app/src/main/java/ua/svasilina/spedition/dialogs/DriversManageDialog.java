package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.LinkedList;
import java.util.UUID;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.CustomListener;
import ua.svasilina.spedition.utils.search.DriverSearchUtil;

public class DriversManageDialog extends DialogFragment {

    private final LayoutInflater inflater;
    DriverSearchUtil dsu;
    private EditText driverInput;
    private final SimpleListAdapter<Driver> driverVariantsAdapter;
    private final CustomAdapter<ReportDetail> driveListAdapter;
    private final CustomListener listener;
    private final LinkedList<ReportDetail> details;

    public DriversManageDialog(final Context context, final LinkedList<ReportDetail> details, CustomListener listener) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dsu = new DriverSearchUtil(context);
        driveListAdapter = new CustomAdapter<>(context, R.layout.report_detail_view, new CustomAdapterBuilder<ReportDetail>() {
            @Override
            public void build(final ReportDetail item, View view, final int position) {

                final View removeItem = view.findViewById(R.id.removeItem);
                removeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driveListAdapter.remove(driveListAdapter.getItem(position));
                        details.remove(position);
                    }
                });
                final TextView driverName = view.findViewById(R.id.driverName);
                final Driver driver = item.getDriver();
                if(driver != null) {
                    driverName.setText(driver.getValue());
                }
                final TextView driverDetails = view.findViewById(R.id.driverDetails);
                driverDetails.setVisibility(View.GONE);
                final ImageButton driverEditButton = view.findViewById(R.id.driverEditButton);
                driverEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Driver driver = item.getDriver();
                        if (driver == null){
                            driver = new Driver();
                            item.setDriver(driver);
                        }
                        new DriverEditDialog(driver, inflater, new CustomListener() {
                            @Override
                            public void onChange() {

                            }
                        }).show(getParentFragmentManager(), "DED");
                    }
                });
            }
        });
        driveListAdapter.addAll(details);
        driverVariantsAdapter = new SimpleListAdapter<>(context, android.R.layout.simple_list_item_1, new AdapterItemEditInterface<Driver>() {
            @Override
            public void click(Driver item, int index) {
                addDriver(item);
            }
        });
        this.listener = listener;
        this.details = details;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.drivers_manage_dialog, null);
        driverInput = view.findViewById(R.id.driverInput);
        final ImageButton addNewDriver = view.findViewById(R.id.addNewDriver);
        addNewDriver.setVisibility(View.GONE);
        driverInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                driverVariantsAdapter.clear();
                if (s.length() > 1){
                    dsu.search(driverVariantsAdapter, s.toString());
                    if(driverVariantsAdapter.getCount() > 0) {
                        addNewDriver.setVisibility(View.GONE);
                    } else {
                        addNewDriver.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        addNewDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseDriver();
                addNewDriver.setVisibility(View.GONE);
            }
        });
        final ListView driver = view.findViewById(R.id.driverVariants);
        driver.setAdapter(driverVariantsAdapter);
        final ListView driverList = view.findViewById(R.id.driverList);
        driverList.setAdapter(driveListAdapter);

        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Editable text = driverInput.getText();
                if (text.length() > 0){
                    parseDriver();
                }
                listener.onChange();
            }
        });
        return builder.create();
    }

    private void parseDriver(){
        final String[] split = driverInput.getText().toString().trim().split(Keys.SPACE);
        Driver driver = new Driver();
        driver.setUuid(UUID.randomUUID().toString());
        final Person person = driver.getPerson();
        if(split.length > 0){
            person.setSurname(split[0]);
        }
        if (split.length > 1){
            person.setForename(split[1]);
        }
        if (split.length > 2){
            person.setPatronymic(split[2]);
        }
        addDriver(driver);
    }

    private void addDriver(Driver driver) {
        final ReportDetail detail = new ReportDetail();
        detail.setDriver(driver);
        details.add(detail);
        driveListAdapter.add(detail);
        driverVariantsAdapter.clear();
        driverInput.getEditableText().clear();
    }
}

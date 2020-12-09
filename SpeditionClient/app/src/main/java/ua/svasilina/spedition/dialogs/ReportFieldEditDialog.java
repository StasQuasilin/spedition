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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Counterparty;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.CustomListener;
import ua.svasilina.spedition.utils.ProductsUtil;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;
import ua.svasilina.spedition.utils.builders.WeightStringBuilder;
import ua.svasilina.spedition.utils.search.CounterpartySearchUtil;

import static ua.svasilina.spedition.constants.Patterns.DATE_PATTERN;
import static ua.svasilina.spedition.constants.Patterns.TIME_PATTERN;

public class ReportFieldEditDialog extends DialogFragment {

    private final Report report;
    private final ReportField reportField;
    private final Context context;
    private final LayoutInflater inflater;
    private final CustomListener saveListener;

    private EditText counterpartyInput;
    private Spinner product;
    private EditText moneyEdit;
    private Button fixArrive;
    private View arriveContainer;
    private Button arriveDate;
    private Button arriveTime;
    private Button fixLeave;
    private View leaveContainer;
    private Button leaveDate;
    private Button leaveTime;
    private TextView counterpartyWeight;
    private Button addWeight;
    private Switch paymentSwitch;
    private final List<Product> products;
    private final DateTimeBuilder dateBuilder;
    private final DateTimeBuilder timeBuilder;
    private final WeightStringBuilder wsb;

    public ReportFieldEditDialog(ReportField reportField, Context context, Report report, CustomListener saveListener) {
        this.report = report;
        this.reportField = reportField;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.saveListener = saveListener;

        ProductsUtil productsUtil = new ProductsUtil(context);
        if (report != null) {
            products = productsUtil.getChildren(report.getProduct());
        } else {
            products = productsUtil.getProducts();
        }
        dateBuilder = new DateTimeBuilder(DATE_PATTERN);
        timeBuilder = new DateTimeBuilder(TIME_PATTERN);
        wsb= new WeightStringBuilder(context.getResources());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.field_edit_dialog, null);
        counterpartyInput = view.findViewById(R.id.counterparty);
        final Counterparty counterparty = reportField.getCounterparty();
        if (counterparty !=null){
            counterpartyInput.setText(counterparty.getName());
        }
        final ListView counterpartyList = view.findViewById(R.id.counterpartyList);
        final TextView counterpartyLabel = view.findViewById(R.id.counterpartyLabel);
        initCounterpartyInput(counterpartyList, counterpartyLabel);

        fixArrive = view.findViewById(R.id.fixArrive);
        arriveContainer = view.findViewById(R.id.arriveContainer);
        arriveDate = view.findViewById(R.id.arriveDate);
        arriveTime = view.findViewById(R.id.arriveTime);
        fixLeave = view.findViewById(R.id.fixLeave);
        leaveContainer = view.findViewById(R.id.leaveContainer);
        leaveDate = view.findViewById(R.id.leaveDate);
        leaveTime = view.findViewById(R.id.leaveTime);
        initArrive();


        product = view.findViewById(R.id.details);
        initProductSpinner();

        paymentSwitch = view.findViewById(R.id.paymentSwitch);
        final int money = reportField.getMoney();

        paymentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentSwitch.setText(R.string.give);
                } else {
                    paymentSwitch.setText(R.string.discarded);
                }
            }
        });
        paymentSwitch.setChecked(money < 0);

        moneyEdit = view.findViewById(R.id.money);
        moneyEdit.setText(String.valueOf(Math.abs(money)));


        counterpartyWeight = view.findViewById(R.id.counterpartyWeight);
        updateCounterpartyWeight();
        addWeight = view.findViewById(R.id.addWeight);
        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinkedList<ReportDetail> details = report.getDetails();
                new CounterpartyWeightDialog(details, reportField, context, new CustomListener() {
                    @Override
                    public void onChange() {
                        updateCounterpartyWeight();
                    }
                }).show(getParentFragmentManager(), "CWD");
            }
        });

        builder.setView(view);

        builder.setNegativeButton(R.string.cancel, null);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        return builder.create();
    }

    private void updateCounterpartyWeight() {
        final LinkedList<ReportDetail> details = report.getDetails();
        StringBuilder builder = new StringBuilder();
        for (ReportDetail d : details){
            final Weight weight = d.getCounterpartyWeight(reportField.getUuid());
            if (weight != null){
                if (builder.length() > 0){
                    builder.append(Keys.NEW_ROW);
                }
                final Driver driver = d.getDriver();
                if (driver != null){
                    builder.append(driver.toString());
                    builder.append(Keys.SPACE);
                }
                builder.append(wsb.buildShort(weight));
            }
        }
        if (builder.length() > 0){
            counterpartyWeight.setVisibility(View.VISIBLE);
            counterpartyWeight.setText(builder.toString());
        } else {
            counterpartyWeight.setVisibility(View.GONE);
        }
    }

    private void initCounterpartyInput(final ListView counterpartyList, final TextView counterpartyLabel) {
        final Context context = getContext();
        final CounterpartySearchUtil counterpartySearchUtil = new CounterpartySearchUtil(context);
        final CustomAdapter<Counterparty> adapter = new CustomAdapter<>(context, android.R.layout.simple_list_item_1, new CustomAdapterBuilder<Counterparty>() {
            @Override
            public void build(final Counterparty item, View view, int position) {
                final TextView text1 = view.findViewById(android.R.id.text1);
                text1.setText(item.getName());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportField.setCounterparty(item);
                        counterpartyInput.setText(item.getName());
                        counterpartyList.setVisibility(View.GONE);
                    }
                });
            }
        });
        counterpartyList.setVisibility(View.GONE);
        counterpartyList.setAdapter(adapter);
        counterpartyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                counterpartyList.setVisibility(View.GONE);
                if (s.length() > 1){
                    reportField.setCounterparty(null);
                    counterpartySearchUtil.search(adapter, s.toString());
                    if (adapter.getCount() == 0){
                        newCounterparty();
                    } else {
                        oldCounterparty();
                        counterpartyList.setVisibility(View.VISIBLE);
                    }
                } else {
                    oldCounterparty();
                }
            }

            private void oldCounterparty() {
                counterpartyLabel.setText(context.getResources().getString(R.string.counterparty));
                counterpartyLabel.setTextColor(context.getResources().getColor(R.color.textColor));
            }

            private void newCounterparty() {
                counterpartyLabel.setText(context.getResources().getString(R.string.newCounterparty));
                counterpartyLabel.setTextColor(context.getResources().getColor(R.color.someNew));
            }
        });
    }

    private void initLeave() {
        fixLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportField.setLeaveTime(Calendar.getInstance());
                switchLeaveDateTime();
            }
        });
        final CustomListener cl = new CustomListener() {
            @Override
            public void onChange() {
                updateLeaveDateTime();
            }
        };
        leaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateDialog dateDialog = new DateDialog(reportField.getLeaveTime(), inflater, DateDialogState.date, cl);
                dateDialog.show(getChildFragmentManager(), "LeaveDateDialog");
            }
        });
        leaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateDialog dateDialog = new DateDialog(reportField.getLeaveTime(), inflater, DateDialogState.time, cl);
                dateDialog.show(getChildFragmentManager(), "LeaveTimeDialog");
            }
        });
        switchLeaveDateTime();
    }

    private void switchLeaveDateTime() {
        final Calendar leaveTime = reportField.getLeaveTime();
        if (leaveTime == null){
            fixLeave.setVisibility(View.VISIBLE);
            leaveContainer.setVisibility(View.GONE);
        } else {
            fixLeave.setVisibility(View.GONE);
            leaveContainer.setVisibility(View.VISIBLE);
            updateLeaveDateTime();
        }
    }

    private void updateLeaveDateTime() {
        final Calendar ldt = reportField.getLeaveTime();
        leaveDate.setText(dateBuilder.build(ldt));
        leaveTime.setText(timeBuilder.build(ldt));
    }

    private void initArrive() {
        fixArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportField.setArriveTime(Calendar.getInstance());
                switchArriveDateTime();
            }
        });
        final CustomListener cl = new CustomListener() {
            @Override
            public void onChange() {
                updateArriveDateTime();
            }
        };
        arriveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateDialog dateDialog = new DateDialog(reportField.getArriveTime(), inflater, DateDialogState.date, cl);
                dateDialog.show(getChildFragmentManager(), "ArriveDateDialog");
            }
        });
        arriveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateDialog dateDialog = new DateDialog(reportField.getArriveTime(), inflater, DateDialogState.time, cl);
                dateDialog.show(getChildFragmentManager(), "ArriveTimeDialog");
            }
        });
        switchArriveDateTime();
    }

    private void switchArriveDateTime(){
        final Calendar arriveTime = reportField.getArriveTime();
        if (arriveTime == null){
            fixArrive.setVisibility(View.VISIBLE);
            arriveContainer.setVisibility(View.GONE);
            fixLeave.setVisibility(View.GONE);
            leaveContainer.setVisibility(View.GONE);
        } else {
            fixArrive.setVisibility(View.GONE);
            arriveContainer.setVisibility(View.VISIBLE);
            initLeave();
            updateArriveDateTime();
        }
    }

    private void updateArriveDateTime(){
        final Calendar adt = reportField.getArriveTime();
        arriveDate.setText(dateBuilder.build(adt));
        arriveTime.setText(timeBuilder.build(adt));
    }

    ArrayAdapter<Product> adapter;
    private void initProductSpinner() {
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(products);
        product.setAdapter(adapter);
        if (reportField.getProduct() != null){
            product.setSelection(adapter.getPosition(reportField.getProduct()));
        }
    }

    private void save(){
        Counterparty counterparty = reportField.getCounterparty();
        if(counterparty == null){
            final String counterpartyValue = this.counterpartyInput.getText().toString();
            if (!counterpartyValue.isEmpty()){
                counterparty = new Counterparty();
                counterparty.setName(counterpartyValue);
                reportField.setCounterparty(counterparty);
            }

        }

        reportField.setProduct(adapter.getItem(product.getSelectedItemPosition()));

        final String moneyText = moneyEdit.getText().toString();
        if(!moneyText.isEmpty()){
            int money = Integer.parseInt(moneyText);
            if(paymentSwitch.isChecked()){
                money = -money;
            }
            reportField.setMoney(money);
        }
        saveListener.onChange();
//        reportField.setIndex(Integer.parseInt(numberText.getText().toString()));
    }
}

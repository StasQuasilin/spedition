package ua.svasilina.spedition.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.dialogs.ReportFieldEditDialog;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.CustomListener;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;

import static ua.svasilina.spedition.constants.Keys.CURRENCY_SIGN;
import static ua.svasilina.spedition.constants.Keys.HYPHEN;
import static ua.svasilina.spedition.constants.Keys.QUESTION;
import static ua.svasilina.spedition.constants.Keys.SPACE;
import static ua.svasilina.spedition.constants.Patterns.DATE_PATTERN;
import static ua.svasilina.spedition.constants.Patterns.TIME_PATTERN;

public class ReportFieldAdapter extends ArrayAdapter<ReportField> {

    private final int resource;
    private final Context context;
    private final LayoutInflater inflater;
    private final FragmentManager fragmentManager;
    private final CustomListener customListener;
    private final Report report;
    private final DateTimeBuilder dateBuilder;
    private final DateTimeBuilder timeBuilder;

    public ReportFieldAdapter(@NonNull Context context, int resource, FragmentManager fragmentManager, Report report, CustomListener customListener) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.report = report;
        this.customListener = customListener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dateBuilder = new DateTimeBuilder(DATE_PATTERN);
        timeBuilder = new DateTimeBuilder(TIME_PATTERN);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ReportField item = getItem(position);
        final View view = convertView != null ? convertView : inflater.inflate(resource, parent,false);

        final View editLayout = view.findViewById(R.id.editLayout);
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(item);
            }
        });

        final Resources resources = getContext().getResources();

        final TextView indexView = editLayout.findViewById(R.id.index);
        indexView.setText(resources.getString(R.string.pointN) + (position + 1));

        final TextView timeView = editLayout.findViewById(R.id.timeView);

        assert item != null;
        final Calendar arriveTime = item.getArriveTime();
        final Calendar leaveTime = item.getLeaveTime();

        if (arriveTime != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(dateBuilder.build(arriveTime)).append(SPACE);
            builder.append(timeBuilder.build(arriveTime));
            if (leaveTime != null){
                builder.append(HYPHEN);
                if (arriveTime.get(Calendar.DATE) != leaveTime.get(Calendar.DATE)){
                    builder.append(dateBuilder.build(leaveTime)).append(SPACE);
                }
                builder.append(timeBuilder.build(leaveTime));
            }
            timeView.setText(builder.toString());
        } else {
            timeView.setVisibility(View.GONE);
        }

        final TextView counterpartyView = editLayout.findViewById(R.id.counterparty);
        if (item.getCounterparty() != null) {
            counterpartyView.setText(item.getCounterparty().getName().toUpperCase());
        } else {
            counterpartyView.setText(QUESTION);
        }

        StringBuilder builder = new StringBuilder();
        final Weight weight = item.getWeight();
        if (weight != null) {
            builder.append(resources.getString(R.string.B));
            builder.append(weight.getGross());
            builder.append(SPACE);
            builder.append(resources.getString(R.string.T));
            builder.append(weight.getTare());
            builder.append(SPACE);
            final float net = weight.getNet();
            if (net > 0) {
                builder.append(resources.getString(R.string.N));
                builder.append(net);
            }
        }

        final int money = item.getMoney();
        if (money != 0) {
            builder.append(SPACE);
            builder.append(money).append(CURRENCY_SIGN);
        }

        final TextView detailView = editLayout.findViewById(R.id.details);
        final String details = builder.toString();
        if (details.isEmpty()) {
            detailView.setVisibility(View.GONE);
        } else {
            detailView.setText(details);
        }

        final View remove = view.findViewById(R.id.removeButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Report Field Adapter", " Remove field " + position);
                report.getFields().remove(item);
                remove(item);
            }
        });

        return view;
    }

    private void edit(ReportField item){
        final ReportFieldEditDialog editDialog = new ReportFieldEditDialog(item, context, report, new CustomListener() {
            @Override
            public void onChange() {
                customListener.onChange();
            }
        });
        editDialog.show(fragmentManager, "Edit Field");
    }
}

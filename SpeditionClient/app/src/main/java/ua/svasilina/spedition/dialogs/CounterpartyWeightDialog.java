package ua.svasilina.spedition.dialogs;

import android.content.Context;

import java.util.LinkedList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.ReportField;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.utils.CustomListener;

public class CounterpartyWeightDialog extends DriverWeightDialog {

    private final ReportField reportField;

    public CounterpartyWeightDialog(LinkedList<ReportDetail> details, ReportField reportField, Context context, CustomListener customListener) {
        super(details, context, customListener);
        this.reportField = reportField;
    }

    @Override
    public int getTitle() {
        return R.string.counterpartyWeight;
    }

    @Override
    public Weight getWeight(ReportDetail item) {
        return item.getCounterpartyWeight(reportField.getUuid());
    }

    @Override
    public void insertWeight(ReportDetail item, Weight weight) {
        item.setCounterpartyWeight(reportField.getUuid(), weight);
    }
}

package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.LinkedList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.CustomAdapter;
import ua.svasilina.spedition.entity.ReportDetail;
import ua.svasilina.spedition.entity.Weight;
import ua.svasilina.spedition.utils.CustomAdapterBuilder;
import ua.svasilina.spedition.utils.CustomListener;
import ua.svasilina.spedition.utils.builders.WeightStringBuilder;

public class DriverWeightDialog extends DialogFragment {
    private final LinkedList<ReportDetail> details;
    private final LayoutInflater inflater;
    private final CustomListener listener;
    private final CustomAdapter<ReportDetail> weightAdapter;
    private final WeightStringBuilder weightStringBuilder;

    public DriverWeightDialog(LinkedList<ReportDetail> details, final Context context, CustomListener listener) {
        this.details = details;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
        weightStringBuilder = new WeightStringBuilder(context.getResources());
        weightAdapter = new CustomAdapter<>(context, R.layout.driver_weight_item, new CustomAdapterBuilder<ReportDetail>() {
            @Override
            public void build(final ReportDetail item, View view, int position) {
                final TextView driverLabel = view.findViewById(R.id.driverLabel);
                if (item.getDriver() != null){
                    driverLabel.setText(item.getDriver().toString());
                }
                final TextView weightLabel = view.findViewById(R.id.weightLabel);
                final Weight weight = getWeight(item);
                if (weight != null){
                    weightLabel.setText(weightStringBuilder.build(weight));
                    weightLabel.setTextColor(getResources().getColor(R.color.textColor));
                } else {
                    weightLabel.setText(R.string.press_for_weight);
                    weightLabel.setTextColor(getResources().getColor(R.color.gray));
                }
                view.setOnClickListener(getClickListener(item));
            }
        });
        weightAdapter.addAll(details);
    }

    public Weight getWeight(ReportDetail item) {
        return item.getOwnWeight();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(R.layout.driver_weight, null);
        final ListView wList = view.findViewById(R.id.wList);
        wList.setAdapter(weightAdapter);

        builder.setView(view);
        builder.setTitle(getTitle());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        return builder.create();
    }

    public View.OnClickListener getClickListener(final ReportDetail item){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WeightEditDialog(getWeight(item), item.getDriver(), inflater, new SaveWaiter<Weight>() {
                    @Override
                    public void onSave(Weight weight) {
                        insertWeight(item, weight);
                    }
                }).show(getParentFragmentManager(),"WED");
            }
        };
    }

    public void insertWeight(ReportDetail item, Weight weight) {
        item.setOwnWeight(weight);
    }

    public int getTitle(){
        return R.string.ownWeight;
    }

    private void save() {
        if (listener != null) {
            listener.onChange();
        }
    }
}

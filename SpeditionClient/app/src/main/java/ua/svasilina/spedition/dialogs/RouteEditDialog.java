package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.LinkedList;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.CustomListener;

public class RouteEditDialog extends DialogFragment {


    private final LinkedList<String> route;
    private final LayoutInflater inflater;
    private final CustomListener customListener;

    public RouteEditDialog(LinkedList<String> route, LayoutInflater inflater, CustomListener customListener) {
        this.route = route;
        this.inflater = inflater;
        this.customListener = customListener;
    }

    private int currentItem = -1;
    private SimpleListAdapter<String> adapter;

    EditText pointEdit;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.route_edit, null);
        pointEdit = view.findViewById(R.id.editRoute);

        adapter = new SimpleListAdapter<>(getContext(), R.layout.simple_list_item_d, new AdapterItemEditInterface<String>() {
            @Override
            public void click(String item, int index) {
                currentItem = index;
                pointEdit.setText(item);
            }
        });

        ListView listView = view.findViewById(R.id.routePoints);
        listView.setAdapter(adapter);

        adapter.addAll(route);
        if (adapter.getCount() > 0){
            pointEdit.getText().clear();
        }

        final Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        builder.setTitle(R.string.routeEdit);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                route.clear();
                for (int i = 0; i < adapter.getCount(); i++){
                    final String item = adapter.getItem(i);
                    route.add(item);
                }
                customListener.onChange();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void save() {
        final String s = pointEdit.getText().toString();
        if (!s.isEmpty()){
            if (currentItem != -1){
                adapter.remove(adapter.getItem(currentItem));
                adapter.insert(s, currentItem);
            } else {
                adapter.add(s);
            }
            currentItem = -1;
            pointEdit.getText().clear();
        }
    }
}

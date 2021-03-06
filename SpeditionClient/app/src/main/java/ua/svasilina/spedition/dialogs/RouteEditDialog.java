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
    private final Context context;

    public RouteEditDialog(Context context, LinkedList<String> route, LayoutInflater inflater, CustomListener customListener) {
        this.context = context;
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
        pointEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before < count) {
                    final int  index = start + before;
                    final char charAt = s.charAt(index);
                    if (!Character.isAlphabetic(charAt) && !Character.isSpaceChar(charAt)){
                        final CharSequence sequence = s.subSequence(0, index);
                        final CharSequence other = (index < s.length() ? s.subSequence(index + 1, s.length()) :null);
                        pointEdit.setText(sequence);
                        save();
                        if(other != null) {
                            pointEdit.setText(other);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new SimpleListAdapter<>(context, R.layout.simple_list_item_d, new AdapterItemEditInterface<String>() {
            @Override
            public void click(String item, int index) {
                currentItem = index;
                pointEdit.setText(item);
            }
        });

        ListView listView = view.findViewById(R.id.routePoints);
        listView.setAdapter(adapter);

        adapter.addAll(route);
        if (adapter.getCount() == 0){
            adapter.add(context.getResources().getString(R.string.sweet_home));
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
        builder.setNegativeButton(R.string.cancel, null);

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

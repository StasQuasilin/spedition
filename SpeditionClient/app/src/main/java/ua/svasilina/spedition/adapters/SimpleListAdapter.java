package ua.svasilina.spedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;

public class SimpleListAdapter<T> extends ArrayAdapter<T> {

    private final LayoutInflater inflater;
    private final int resource;
    private final AdapterItemEditInterface<T> listener;

    public SimpleListAdapter(@NonNull Context context, int resource, AdapterItemEditInterface<T> listener) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = (convertView != null ? convertView : inflater.inflate(resource, parent, false));
        final T item = getItem(position);
        if (resource == android.R.layout.simple_list_item_1){
            if (item != null) {
                final TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(item.toString());
            }
        } else {
            final TextView indexView = view.findViewById(R.id.index);
            if (indexView != null){
                indexView.setText(String.valueOf(position + 1));
            }
            if (item != null) {
                final TextView textView = view.findViewById(R.id.text);
                if (textView != null) {
                    textView.setText(item.toString().toUpperCase());
                }
            }
            final ImageButton deleteButton = view.findViewById(R.id.delete);
            if(deleteButton != null){
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(item);
                    }
                });
            }
        }

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(item, position);
                }
            });
        }
        return view;
    }
}

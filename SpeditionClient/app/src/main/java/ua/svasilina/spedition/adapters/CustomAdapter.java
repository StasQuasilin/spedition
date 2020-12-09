package ua.svasilina.spedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ua.svasilina.spedition.utils.CustomAdapterBuilder;

public class CustomAdapter<T> extends ArrayAdapter<T> {

    private final CustomAdapterBuilder<T> builder;
    private final LayoutInflater inflater;
    private final int resource;

    public CustomAdapter(@NonNull Context context, int resource, CustomAdapterBuilder<T> builder) {
        super(context, resource);
        this.builder = builder;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final T item = getItem(position);
        final View view = (convertView != null ? convertView : inflater.inflate(resource, null));
        builder.build(item, view, position);
        return view;
    }
}

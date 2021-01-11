package ua.svasilina.spedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.svasilina.spedition.R;

public class FlexAdapter <T> extends RecyclerView.Adapter<FlexHolder<T>> {

    private final ArrayList<T> fields = new ArrayList<>();

    private final Context context;
    public FlexAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public FlexHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.simple_list_item_d, parent, false);
        return new FlexHolder<T>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlexHolder holder, int position) {
        holder.bind(fields.get(position));
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public void add(T item){
        fields.add(item);
    }
}

package ua.svasilina.spedition.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ua.svasilina.spedition.R;

public class FlexHolder <T> extends RecyclerView.ViewHolder {

    private View view;
    public FlexHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }


    public void bind(T t) {
        final TextView view = this.view.findViewById(R.id.text);
        view.setText(t.toString());
    }
}

package ua.svasilina.spedition.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.ReportNote;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;

import static ua.svasilina.spedition.constants.Keys.COLON;
import static ua.svasilina.spedition.constants.Keys.DOT;
import static ua.svasilina.spedition.constants.Keys.SPACE;

public class NoteAdapter extends ArrayAdapter<ReportNote> {

    private final int resource;
    private LayoutInflater inflater;
    private AdapterItemEditInterface<ReportNote> onClick;

    public NoteAdapter(@NonNull Context context, int resource, LayoutInflater inflater,
                       AdapterItemEditInterface<ReportNote> onClick) {
        super(context, resource);
        this.resource = resource;
        this.inflater = inflater;
        this.onClick = onClick;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = convertView != null ? convertView : inflater.inflate(resource, parent, false);

        final ReportNote item = getItem(position);
        if (item != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.click(item, position);
                }
            });
            final Calendar time = item.getTime();
            if (time != null){
                final TextView timeView = view.findViewById(R.id.noteTime);
                timeView.setText(
                        time.get(Calendar.HOUR_OF_DAY) +
                                COLON +
                                time.get(Calendar.MINUTE) +
                                SPACE +
                                time.get(Calendar.DAY_OF_MONTH) +
                                DOT +
                                time.get(Calendar.MONTH) +
                                DOT +
                                time.get(Calendar.YEAR)
                );
            }

            final TextView text = view.findViewById(R.id.noteText);
            text.setText(item.getNote());
        }

        final ImageButton deleteButton = view.findViewById(R.id.deleteNote);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(item);
            }
        });

        return view;
    }
}

package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.NoteAdapter;
import ua.svasilina.spedition.entity.ReportNote;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.CustomListener;

public class NoteEditDialog extends DialogFragment {
    private Context context;
    private ArrayList<ReportNote> notes;
    private LayoutInflater inflater;
    private CustomListener listener;
    private EditText noteEdit;
    private int currentItem = -1;
    ArrayAdapter<ReportNote> adapter;
    AdapterItemEditInterface<ReportNote> onClick;

    public NoteEditDialog() {}

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public NoteEditDialog(Context context,
                          ArrayList<ReportNote> notes,
                          LayoutInflater inflater,
                          CustomListener listener) {
        this.context = context;
        this.notes = notes;
        this.inflater = inflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String text = "";
        if (savedInstanceState != null) {
            final Serializable array = savedInstanceState.getSerializable("array");
            if (array != null){
                notes = (ArrayList<ReportNote>) array;
            }
            currentItem = savedInstanceState.getInt("current");
            text = savedInstanceState.getString("text");
            listener = (CustomListener) savedInstanceState.getSerializable("listener");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.notes);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(createView(text));
        return builder.create();
    }

    public View createView(String text) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (inflater != null) {
            final View view = inflater.inflate(R.layout.note_edit_dialog, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            noteEdit = view.findViewById(R.id.noteEdit);
            noteEdit.setText(text);

            final ImageButton saveNote = view.findViewById(R.id.saveNote);
            saveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNote();
                }
            });

            adapter = new NoteAdapter(context, R.layout.note_view, inflater, new AdapterItemEditInterface<ReportNote>() {
                @Override
                public void click(ReportNote item, int index) {
                    noteEdit.setText(item.getNote());
                    currentItem = index;
                }
            });
            adapter.addAll(notes);
//
            final ListView noteList = view.findViewById(R.id.noteList);
            noteList.setAdapter(adapter);
            return view;
        }
        return null;
    }

    private void saveNote() {
        final String noteText = noteEdit.getText().toString();
        if (!noteText.isEmpty()){
            ReportNote note = null;
            if (currentItem != -1){
                note = adapter.getItem(currentItem);
            }

            if (note == null){
                note = new ReportNote();
                note.setUuid(UUID.randomUUID().toString());
                note.setNote(noteText);
                note.setTime(Calendar.getInstance());
            }

            note.setNote(noteText);
            if (currentItem != -1){
                adapter.remove(note);
                adapter.insert(note, currentItem);
                currentItem = -1;
            } else {
                adapter.add(note);
            }
            noteEdit.getText().clear();
        }
    }

    private void save() {
        saveNote();
        notes.clear();
        for (int i = 0; i < adapter.getCount(); i++){
            notes.add(adapter.getItem(i));
        }
        if (listener != null) {
            listener.onChange();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        save();
        outState.putSerializable("array", notes);
        outState.putString("text", noteEdit.getText().toString());
        outState.putInt("current", currentItem);
        outState.putSerializable("listener", listener);
        super.onSaveInstanceState(outState);
    }
}

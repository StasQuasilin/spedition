package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.search.SearchUtil;

public class InputDialog <T> extends DialogFragment {

    private final Context context;
    private final LayoutInflater inflater;
    private EditText textInput;
    private View clearButton;
    private ListView itemsList;
    private SimpleListAdapter<T> adapter;
    private final SearchUtil<T> searchUtil;


    public InputDialog(Context context, SearchUtil<T> searchUtil) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.searchUtil = searchUtil;
        adapter = new SimpleListAdapter<>(context, android.R.layout.simple_list_item_1, new AdapterItemEditInterface<T>() {
            @Override
            public void click(T item, int index) {

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(R.layout.input_dialog, null);
        textInput = view.findViewById(R.id.textInput);
        initTextInput();
        clearButton = view.findViewById(R.id.inputCancel);
        initClearButton();
        itemsList = view.findViewById(R.id.itemsList);
        itemsList.setVisibility(View.GONE);
        itemsList.setAdapter(adapter);

        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });

        return builder.create();
    }

    private void initClearButton() {
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInput.setText(Keys.EMPTY);
            }
        });
    }

    private void initTextInput() {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >1){
                    searchUtil.search(adapter, s.toString());
                }
            }
        });
        textInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println(event);
                return false;
            }
        });
    }

    private void save() {


    }
}

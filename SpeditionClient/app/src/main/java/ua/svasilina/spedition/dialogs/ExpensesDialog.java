package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.UUID;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.adapters.SimpleListAdapter;
import ua.svasilina.spedition.entity.Expense;
import ua.svasilina.spedition.utils.AdapterItemEditInterface;
import ua.svasilina.spedition.utils.CustomListener;

public class ExpensesDialog extends DialogFragment {

    private final ArrayList<Expense> expenses;
    private final LayoutInflater inflater;
    private final CustomListener customListener;
    private EditText descriptionEdit;
    private EditText amountEdit;
    private SimpleListAdapter<Expense> adapter;
    private final String title;

    public ExpensesDialog(ArrayList<Expense> expenses, LayoutInflater inflater, CustomListener customListener, String title) {
        this.expenses = expenses;
        this.inflater = inflater;
        this.customListener = customListener;
        this.title = title;
    }
    int currentIndex = -1;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Context context = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        final View view = inflater.inflate(R.layout.expenses_dialog, null);

        adapter = new SimpleListAdapter<>(context, R.layout.simple_list_item_d, new AdapterItemEditInterface<Expense>() {
            @Override
            public void click(Expense item, int index) {
                editExpenses(item, index);
            }
        });
        adapter.addAll(expenses);

        final ListView expensesList = view.findViewById(R.id.expensesList);
        expensesList.setAdapter(adapter);

        descriptionEdit = view.findViewById(R.id.descriptionEdit);
        amountEdit = view.findViewById(R.id.amountEdit);
        final ImageButton addButton = view.findViewById(R.id.addExpenses);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpenses();
            }
        });

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

        builder.setView(view);
        return builder.create();
    }

    private void save() {
        addExpenses();
        expenses.clear();
        for (int i = 0; i < adapter.getCount(); i++){
            expenses.add(adapter.getItem(i));
        }
        customListener.onChange();
    }

    private void editExpenses(Expense item, int index) {
        currentIndex = index;
        descriptionEdit.setText(item.getDescription());
        amountEdit.setText(String.valueOf(item.getAmount()));
    }

    private void addExpenses() {
        final String description = descriptionEdit.getText().toString();
        final String amountString = amountEdit.getText().toString();
        if (!description.isEmpty() || !amountString.isEmpty()) {
            Expense expense = null;
            if (currentIndex != -1) {
                expense = adapter.getItem(currentIndex);
                adapter.remove(expense);
            }
            if (expense == null) {
                currentIndex = -1;
                expense = new Expense();
                expense.setUuid(UUID.randomUUID().toString());
            }

            expense.setDescription(description);
            int amount = 0;

            if (!amountString.isEmpty()) {
                amount = Integer.parseInt(amountString);
            }
            expense.setAmount(amount);

            if (currentIndex == -1) {
                adapter.add(expense);
            } else {
                adapter.insert(expense, currentIndex);
                currentIndex = -1;
            }
        }
        descriptionEdit.getText().clear();
        amountEdit.getText().clear();
    }
}

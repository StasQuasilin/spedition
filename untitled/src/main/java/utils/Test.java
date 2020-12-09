package utils;

import entity.Expense;
import entity.ExpenseType;
import org.hibernate.jdbc.Expectation;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Test {

    static Hibernator hibernator = Hibernator.getInstance();
    public static void main(String[] args) {
        for (Expense expense : hibernator.query(Expense.class, null)){
            expense.setType(ExpenseType.expense);
            hibernator.save(expense);
        }
    }
}

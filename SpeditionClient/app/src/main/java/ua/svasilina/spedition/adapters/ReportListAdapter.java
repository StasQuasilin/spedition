package ua.svasilina.spedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.Driver;
import ua.svasilina.spedition.entity.Person;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.Opener;
import ua.svasilina.spedition.utils.builders.DateTimeBuilder;

import static ua.svasilina.spedition.constants.Keys.ARROW;
import static ua.svasilina.spedition.constants.Keys.COMA;
import static ua.svasilina.spedition.constants.Keys.DOT_3;
import static ua.svasilina.spedition.constants.Keys.EMPTY;
import static ua.svasilina.spedition.constants.Keys.PLUS;
import static ua.svasilina.spedition.constants.Keys.SPACE;
import static ua.svasilina.spedition.constants.Patterns.DATE_PATTERN;

public class ReportListAdapter extends ArrayAdapter<SimpleReport> {

    private final Context context;
    private final int resource;
    private final List<SimpleReport> reports;
    private final LayoutInflater inflater;
    final DateTimeBuilder dateTimeBuilder;

    public ReportListAdapter(@NonNull Context context, int resource, @NonNull List<SimpleReport> reports) {
        super(context, resource, reports);
        this.context = context;
        this.resource = resource;
        this.reports = reports;
        dateTimeBuilder = new DateTimeBuilder(DATE_PATTERN);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view = convertView != null ? convertView : inflater.inflate(resource, parent, false);

        final SimpleReport report = reports.get(position);
        if (report.isActive()){
            view.setBackgroundColor(context.getResources().getColor(R.color.activeReport));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opener.openReport(context, report.isDone(), report.getUuid());
            }
        });

        TextView dateView = view.findViewById(R.id.date);

        if (report.getLeaveTime() != null) {
            dateView.setText(dateTimeBuilder.build(report.getLeaveTime()));
        } else {
            dateView.setText(EMPTY);
        }

        final TextView check = view.findViewById(R.id.check);

        if (report.getDoneDate() != null){
            check.setVisibility(View.VISIBLE);
        } else {
            check.setVisibility(View.GONE);
        }

        final LinkedList<Driver> drivers = report.getDrivers();

        final TextView driverView = view.findViewById(R.id.driver);
        if (drivers.size() > 0){
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < Math.min(drivers.size(), 3); i++){
                final Driver driver = drivers.get(i);
                if(driver != null) {
                    final Person person = driver.getPerson();
                    builder.append(person.getSurname());
                    if (drivers.size() < 3){
                        builder.append(SPACE).append(person.getForename());
                    }
                    if (i < drivers.size() - 1) {
                        builder.append(COMA).append(SPACE);
                    }
                }
            }
            if (drivers.size() > 3){
                builder.append(DOT_3).append(PLUS).append(drivers.size() - 3);
            }
            driverView.setText(builder.toString().toUpperCase());
        } else {
            driverView.setText(EMPTY);
        }

        final LinkedList<String> route = report.getRoute();
        final TextView routeView = view.findViewById(R.id.route);
        if (route.size() > 0){
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < route.size(); i++){
                builder.append(route.get(i));
                if (i < route.size() - 1){
                    builder.append(ARROW);
                }
            }
            routeView.setText(builder.toString().toUpperCase());
        } else {
            routeView.setText(EMPTY);
        }

        final LinkedList<Product> products = report.getProducts();
        final TextView productView = view.findViewById(R.id.details);
        if (products.size() > 0){
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < products.size(); i++){
                builder.append(products.get(i).getName());
                if (i < products.size() - 1){
                    builder.append(COMA).append(SPACE);
                }
            }
            productView.setText(builder.toString());
        } else {
            productView.setText(EMPTY);
        }
        return view;
    }


}

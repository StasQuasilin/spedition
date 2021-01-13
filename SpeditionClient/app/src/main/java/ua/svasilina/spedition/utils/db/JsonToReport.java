package ua.svasilina.spedition.utils.db;

import android.content.Context;

import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.JsonParser;

public class JsonToReport {

    private final JsonParser parser;

    JsonToReport(){
        parser = new JsonParser();
    }

    public SimpleReport parseSimpleReport(String fileContent, Context context) {
        SimpleReport report = null;

        final JsonObject parse = parser.parse(fileContent);
        if (parse != null){
            report = new SimpleReport(parse, context);
        }
        return report;
    }

    public Report parseReport(String fileContent, Context context) {
        Report report = null;
        final JsonObject parse = parser.parse(fileContent);
        if (parse != null){
            report = new Report(parse, context);
        }
        return report;
    }
}

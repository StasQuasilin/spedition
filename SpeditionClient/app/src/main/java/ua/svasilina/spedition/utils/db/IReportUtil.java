package ua.svasilina.spedition.utils.db;

import java.util.List;

import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;

public interface IReportUtil {
    List<SimpleReport> getReports();
    Report getReport(String uuid);
    void saveReport(Report report);
    boolean removeReport(String id);
}

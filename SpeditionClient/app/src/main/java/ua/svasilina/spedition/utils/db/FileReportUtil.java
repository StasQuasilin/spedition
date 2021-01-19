package ua.svasilina.spedition.utils.db;

import android.content.Context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.FileFilter;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.StorageUtil;

public class FileReportUtil extends AbstractReportUtil {

    private final StorageUtil storageUtil;
    private final JsonToReport jsonToReport;
    private static final String FILE_MASK = "report";
    private static final String REMOVE_MASK = "remove_";
    private final LoginUtil loginUtil;
    private final Context context;

    public FileReportUtil(Context context) {
        super(context);
        this.context = context;
        storageUtil = new StorageUtil(context);
        jsonToReport = new JsonToReport();
        loginUtil = new LoginUtil(context);
    }

    @Override
    Report[] getNotSyncReports() {
        return new Report[0];
    }

    @Override
    String[] getRemovedReports() {
        return new String[0];
    }

    @Override
    public void markSync(String uuid) {

    }

    @Override
    AbstractReportUtil getDataSource(Context context) {
        return new SqLiteReportUtil(context);
    }

    @Override
    public void forgotAbout(String uuid) {

    }

    @Override
    public List<SimpleReport> getReports() {
        final String token = loginUtil.getToken();
        FileFilter fileFilter = new FileFilter(FILE_MASK + Keys.HYPHEN + token);
        final File[] files = storageUtil.getFiles(fileFilter);
        LinkedList<SimpleReport> reports = new LinkedList<>();

        if (files != null){
            for (File f : files){
                final String fileContent = storageUtil.readFile(f.getName());
                final SimpleReport report = jsonToReport.parseSimpleReport(fileContent, context);

                if (report != null){
                    final String uuid = report.getUuid();
                    if (!storageUtil.fileExist(REMOVE_MASK + uuid)) {
                        reports.add(report);
                    }
                }
            }

        }
        return reports;
    }

    @Override
    public Report getReport(String uuid) {
        final String fileContent = storageUtil.readFile(FILE_MASK + uuid);
        return jsonToReport.parseReport(fileContent, context);
    }

    @Override
    public void saveReport(Report report) {
        String uuid = report.getUuid();
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
        }
        final String token = loginUtil.getToken();
        storageUtil.saveData(FILE_MASK + Keys.HYPHEN + token + Keys.HYPHEN + uuid, report.toJson().toJSONString());
    }

    @Override
    public boolean removeReport(String id, boolean b) {
        return false;
    }
}

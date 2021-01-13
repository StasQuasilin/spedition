package ua.svasilina.spedition.utils.db.parsers;

import android.content.Context;
import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.entity.reports.Report;
import ua.svasilina.spedition.utils.ProductsUtil;
import ua.svasilina.spedition.utils.db.ExpenseType;
import ua.svasilina.spedition.utils.db.ExpensesUtil;
import ua.svasilina.spedition.utils.db.NoteUtil;
import ua.svasilina.spedition.utils.db.ReportDetailUtil;
import ua.svasilina.spedition.utils.db.ReportFieldUtil;
import ua.svasilina.spedition.utils.search.ItemParser;

public class SqlReportParser extends ItemParser<Report> {
    private static final String ID_COLUMN = "id";
    private static final String LEAVE_COLUMN = "leave_time";
    private static final String DONE_COLUMN = "done_time";
    private static final String ROUTE_COLUMN = "route";
    private static final String PRODUCT_COLUMN = "product";

    private int idColumn;
    private int serverIdColumn;
    private int uuidColumn;
    private int leaveColumn;
    private int doneColumn;
    private int routeColumn;
    private int productColumn;

    private final ProductsUtil productsUtil;
    private final ReportDetailUtil detailUtil;
    private final ReportFieldUtil reportFieldUtil;
    private final ExpensesUtil expensesUtil;
    private final NoteUtil noteUtil;

    public SqlReportParser(Context context) {
        productsUtil = new ProductsUtil(context);
        detailUtil = new ReportDetailUtil(context);
        reportFieldUtil = new ReportFieldUtil(context);
        expensesUtil = new ExpensesUtil(context);
        noteUtil = new NoteUtil(context);
    }

    @Override
    public void init(Cursor query) {
        idColumn = query.getColumnIndex(ID_COLUMN);
        serverIdColumn = query.getColumnIndex(Keys.SERVER_ID);
        uuidColumn = query.getColumnIndex(Keys.UUID);
        leaveColumn = query.getColumnIndex(LEAVE_COLUMN);
        doneColumn = query.getColumnIndex(DONE_COLUMN);
        routeColumn = query.getColumnIndex(ROUTE_COLUMN);
        productColumn = query.getColumnIndex(PRODUCT_COLUMN);
    }

    @Override
    public Report parse(Cursor query) {
        Report report = new Report();

        report.setId(query.getInt(idColumn));
        report.setServerId(query.getInt(serverIdColumn));
        final String uuid = query.getString(uuidColumn);
        report.setUuid(uuid);
        final long leaveTime = query.getLong(leaveColumn);
        if (leaveTime > 0){
            report.setLeaveTime(leaveTime);
        }
        final long doneTime = query.getLong(doneColumn);
        if (doneTime > 0){
            report.setDoneTime(doneTime);
        }

        report.setRoute(query.getString(routeColumn));
        final int productId = query.getInt(productColumn);
        final Product product = productsUtil.getProduct(productId);
        report.setProduct(product);

        detailUtil.getDetails(report);
        reportFieldUtil.getFields(report);
        expensesUtil.getExpenses(report.getExpenses(), uuid, ExpenseType.expense);
        expensesUtil.getExpenses(report.getFares(), uuid, ExpenseType.fare);
        noteUtil.getNotes(report.getNotes(), uuid);
        return report;
    }
}

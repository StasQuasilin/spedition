package ua.svasilina.spedition.utils.db;

import android.content.Context;
import android.database.Cursor;

import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.entity.reports.SimpleReport;
import ua.svasilina.spedition.utils.ProductsUtil;
import ua.svasilina.spedition.utils.search.ItemParser;

public class SimpleParser extends ItemParser<SimpleReport> {

    private static final String ID_COLUMN = "id";
    private static final String LEAVE_COLUMN = "leave_time";
    private static final String DONE_COLUMN = "done_time";
    private static final String ROUTE_COLUMN = "route";
    private static final String PRODUCT_COLUMN = "product";
    int idColumn;
    int uuidColumn;
    int leaveColumn;
    int doneColumn;
    int routeColumn;
    int productColumn;

    private final ProductsUtil productsUtil;

    public SimpleParser(Context context) {
        productsUtil = new ProductsUtil(context);
    }

    @Override
    public void init(Cursor query) {
        idColumn = query.getColumnIndex(ID_COLUMN);
        uuidColumn = query.getColumnIndex(Keys.UUID);
        leaveColumn = query.getColumnIndex(LEAVE_COLUMN);
        doneColumn = query.getColumnIndex(DONE_COLUMN);
        routeColumn = query.getColumnIndex(ROUTE_COLUMN);
        productColumn = query.getColumnIndex(PRODUCT_COLUMN);
    }

    @Override
    public SimpleReport parse(Cursor query) {
        SimpleReport simpleReport = new SimpleReport();
        simpleReport.setId(query.getInt(idColumn));
        simpleReport.setUuid(query.getString(uuidColumn));
        final long leaveTime = query.getLong(leaveColumn);
        if (leaveTime > 0) {
            simpleReport.setLeaveTime(leaveTime);
        }
        final long doneTime = query.getLong(doneColumn);
        if(doneTime > 0){
            simpleReport.setDoneTime(doneTime);
        }

        simpleReport.setRoute(query.getString(routeColumn));
        final int productId = query.getInt(productColumn);
        if (productId > 0) {
            simpleReport.addProduct(productsUtil.getProduct(productId));
        }
        return simpleReport;
    }
}

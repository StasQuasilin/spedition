package api.socket.handlers;

import api.socket.SubscribeType;
import entity.reports.Report;
import entity.User;
import entity.reports.ReportHeader;
import org.json.simple.JSONArray;
import utils.hibernate.dao.ReportDAO;

public class ReportHandler extends Handler {
    private final ReportDAO reportDAO = new ReportDAO();

    public ReportHandler(SubscribeType subscribeType) {
        super(subscribeType);
    }

    @Override
    public Object getData(User user) {
        JSONArray array = new JSONArray();
        for (ReportHeader report : reportDAO.getReportsHeaders(user)){
            array.add(report.toJson());
        }
        return array;
    }
}

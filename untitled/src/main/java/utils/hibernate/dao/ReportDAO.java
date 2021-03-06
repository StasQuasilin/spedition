package utils.hibernate.dao;

import api.socket.SubscribeType;
import api.socket.UpdateUtil;
import constants.Keys;
import entity.*;
import entity.reports.CounterpartyWeight;
import entity.reports.Report;
import entity.reports.ReportDetails;
import entity.reports.ReportHeader;
import org.apache.log4j.Logger;
import utils.hibernate.DateContainers.BETWEEN;
import utils.hibernate.DateContainers.GE;
import utils.hibernate.Hibernator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static constants.Keys.REPORT;

public class ReportDAO {
    private final Logger log =Logger.getLogger(ReferencesDAO.class);
    private final Hibernator hibernator = Hibernator.getInstance();
    private final UpdateUtil updateUtil = new UpdateUtil();

    public Report getReportByUUID(Object id){
        return hibernator.get(Report.class, Keys.UUID, id);
    }

    public void save(Report report) {
        hibernator.save(report);
    }

    public void afterSave(Report report){
        final User owner = report.getOwner();
        updateUtil.update(SubscribeType.reports, report.toJson(), owner);
        if (owner.getSupervisor() != null) {
            updateUtil.update(SubscribeType.reports, report.toJson(), owner.getSupervisor());
        }
    }

    public List<ReportHeader> getReportsHeaders(User user) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("leaveTime", new GE(Date.valueOf(LocalDate.now().minusMonths(1))));
        final Role role = user.getRole();
        if (role == Role.supervisor){
            params.put("owner/supervisor", user);
        } else if(role == Role.user){
            params.put("owner", user);
        }

        return hibernator.query(ReportHeader.class, params);
    }

    public List<ReportField> getFields(Object report){
        return hibernator.query(ReportField.class, REPORT, report);
    }

    public Report getReport(Object id) {
        return hibernator.get(Report.class, Keys.ID, id);
    }

    public void save(ReportField reportField) {
        hibernator.save(reportField);
    }

    public void remove(Object item) {
        hibernator.remove(item);
    }

    public void save(Expense expense) {
        hibernator.save(expense);
    }

    public List<ReportNote> getNotes(Object report) {
        return hibernator.query(ReportNote.class, REPORT, report);
    }

    public void save(ReportNote reportNote) {
        hibernator.save(reportNote);
    }

    public void save(Weight weight) {
        hibernator.save(weight);
    }

    public List<Report> getNotClosetReports(User user) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("done", null);
        params.put("owner", user);
        return hibernator.query(Report.class, params);
    }

    public List<Report> getReportsHeaders(Date from, Date to) {
        log.info("Get reports from " + from.toString() +" to " + to.toString());
        return hibernator.query(Report.class, "leaveTime", new BETWEEN(from, to));
    }

    public void save(ReportDetails reportDetails) {
        hibernator.save(reportDetails);
    }

    public List<CounterpartyWeight> getCounterpartyWeight(String detailUuid) {
        return hibernator.query(CounterpartyWeight.class, "details", detailUuid);
    }

    public void save(CounterpartyWeight counterpartyWeight) {
        hibernator.save(counterpartyWeight);
    }
}

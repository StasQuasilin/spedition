package constants;

public interface ApiLinks {
    String API = "/api/v1";
    String REPORT_SAVE = API + "/report/save";
    String REPORT_REMOVE = API + "/report/remove";
    String LOGIN = "/sign/in";
    String SOCKET = "/socket";
    String REGISTRATION = API + "/sign/up";
    String REPORTS = API + "/reports";
    String REFERENCES = API + "/references";
    String GET_REPORTS = API + "/reports/get";
    String SYNC_REFERENCES = API + "/sync/references";
    String GET_PRODUCT = API + "/get/product";
    String GET_DRIVER = API + "/get/driver";
    String GET_COUNTERPARTY = API + "/get/counterparty";
    String ACTIVE_REPORTS = API + "/active/reports";
    String PING = "/ping";
}

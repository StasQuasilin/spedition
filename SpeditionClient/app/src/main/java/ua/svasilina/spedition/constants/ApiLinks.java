package ua.svasilina.spedition.constants;

public interface ApiLinks {
//    String ADDRESS = "134.249.155.33:32332";
    String ADDRESS = "10.10.10.45:3322";
    String HOME = "http://" + ADDRESS + "/spedition";
    String LOGIN = HOME + "/sign/in";

    String API = "/api/v1";
    String REPORT_SAVE = HOME + API+ "/report/save";
    String SYNC_REFERENCES = HOME + API + "/sync/references";
    String GET_PRODUCT = HOME + API + "/get/product";
    String GET_DRIVER = HOME + API + "/get/driver";
    String GET_COUNTERPARTY = HOME + API + "/get/counterparty";
    String REPORT_REMOVE = HOME + API + "/report/remove";
    String PING = HOME + "/ping";
}

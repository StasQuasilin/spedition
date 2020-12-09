package api;

import entity.JsonAble;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static constants.Keys.ENCODING;

public abstract class ServletAPI extends HttpServlet {

    public static final String SUCCESS_ANSWER = "{\"answer\":\"success\"}";

    private final Logger log = Logger.getLogger(ServletAPI.class);

    public void write(HttpServletResponse resp, JsonAble json) throws IOException {
        write(resp, json.toJson());
    }

    public void write(HttpServletResponse resp, JSONObject json) throws IOException {

        write(resp, json.toJSONString());
    }

    public void write(HttpServletResponse resp, String msg) throws IOException {
        log.info("Write '" + msg + "'");
        resp.setCharacterEncoding(ENCODING);
        resp.getWriter().write(msg);
    }

    JSONParser parser = new JSONParser();
    public final JSONObject parseBody(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        try {
            return (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

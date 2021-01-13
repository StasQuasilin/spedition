package controllers;

import entity.Role;
import entity.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static constants.Keys.ROLE;
import static constants.Keys.USER;

public abstract class AbstractController extends HttpServlet {

    private final JSONParser parser = new JSONParser();

    public JSONObject parseBody(HttpServletRequest req){
        try {
            return (JSONObject) parser.parse(req.getReader());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(HttpServletRequest req){
        return (User) req.getSession().getAttribute(USER);
    }
    public Role getRole(HttpServletRequest req){
        return (Role) req.getSession().getAttribute(ROLE);
    }
}

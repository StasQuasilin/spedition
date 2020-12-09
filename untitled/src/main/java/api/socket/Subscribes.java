package api.socket;

import api.socket.handlers.Handler;
import api.socket.handlers.ReportHandler;
import entity.User;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subscribes {

    private static final Subscribes instance = new Subscribes();
    private final HashMap<SubscribeType, HashMap<User, Session>> sessions = new HashMap<>();
    private static final HashMap<SubscribeType, Handler> handlers = new HashMap<>();
    static {
        addHandler(new ReportHandler(SubscribeType.reports));
    }

    private Subscribes() {
    }

    public static Subscribes getInstance() {
        return instance;
    }

    static void addHandler(Handler handler){
        handlers.put(handler.getSubscribeType(), handler);
    }

    public void add(Session session, SubscribeType subscribeType, User user) {
        if (!sessions.containsKey(subscribeType)){
            sessions.put(subscribeType, new HashMap<>());
        }
        sessions.get(subscribeType).put(user, session);
        final Handler handler = handlers.get(subscribeType);
        try {
            handler.onSubscribe(session, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session getSession(SubscribeType type, User user){
        final HashMap<User, Session> userSessionHashMap = sessions.get(type);
        if (userSessionHashMap != null){
            return userSessionHashMap.get(user);
        }
        return null;
    }

    public Handler getHandler(SubscribeType type){
        return handlers.get(type);
    }

    public void remove(User user) {
        for (Map.Entry<SubscribeType, HashMap<User, Session>> entry : sessions.entrySet()){
            entry.getValue().remove(user);
        }
    }
}

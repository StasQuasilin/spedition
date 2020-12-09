package api.socket.handlers;

import api.socket.DataType;
import api.socket.SubscribeType;
import entity.User;
import org.json.simple.JSONObject;

import javax.websocket.Session;

import java.io.IOException;

import static constants.Keys.*;

public abstract class Handler {
    private final SubscribeType subscribeType;

    protected Handler(SubscribeType subscribeType) {
        this.subscribeType = subscribeType;
    }

    public SubscribeType getSubscribeType() {
        return subscribeType;
    }

    public void onSubscribe(Session session, User user) throws IOException {
        send(session, DataType.add, getData(user));
    }

    public void send(Session session, DataType dataType, Object data){
        JSONObject json = new JSONObject();
        json.put(TYPE, subscribeType.toString());
        JSONObject dataJson = new JSONObject();
        dataJson.put(dataType.toString(), data);
        json.put(DATA, dataJson);
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(json.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract Object getData(User user);
}

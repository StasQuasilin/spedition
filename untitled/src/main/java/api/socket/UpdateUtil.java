package api.socket;

import api.socket.handlers.Handler;
import entity.User;

import javax.websocket.Session;
import java.util.ArrayList;

public class UpdateUtil {

    private final Subscribes subscribes = Subscribes.getInstance();

    public void update(SubscribeType type, Object o, User owner){
        final Handler handler = subscribes.getHandler(type);
        final Session session = subscribes.getSession(type, owner);
        if (session != null) {
            handler.send(session, DataType.update, o);
        }
    }
}

package api.socket;

import api.socket.handlers.Handler;
import entity.Report;
import entity.User;

import javax.websocket.Session;
import java.util.ArrayList;

public class UpdateUtil {

    private final Subscribes subscribes = Subscribes.getInstance();

    public void update(SubscribeType type, Object o, User owner){
        sendAction(type, DataType.update,o,owner);
    }

    private void sendAction(SubscribeType subscribeType, DataType type, Object o, User owner){
        final Handler handler = subscribes.getHandler(subscribeType);
        final Session session = subscribes.getSession(subscribeType, owner);
        if (session != null) {
            handler.send(session, type, o);
        }
    }

    public void remove(SubscribeType reports, Object report, User recipient) {
        sendAction(reports, DataType.remove, report, recipient);
    }
}

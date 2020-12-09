package api.socket;

import constants.ApiLinks;
import entity.User;
import org.json.simple.JSONObject;
import utils.JsonParser;
import utils.hibernate.dao.UserDAO;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.util.HashMap;

import static constants.Keys.*;

@ServerEndpoint(ApiLinks.SOCKET)
public class SocketEndpoint {

    private final Subscribes subscribes = Subscribes.getInstance();
    private final JsonParser parser = new JsonParser();
    private final UserDAO userDAO = new UserDAO();
    private final HashMap<Session, User> userSessions = new HashMap<>();

    @OnOpen
    public void OnOpen(Session session){
        System.out.println("Connect " + session.getId());
    }

    @OnMessage
    public void OnMessage(Session session, String message){
        final JSONObject parse = parser.parse(message);
        if (parse != null){
            if (parse.containsKey(ACTION)){
                SocketAction action = SocketAction.valueOf(String.valueOf(parse.get(ACTION)));
                SubscribeType subscribeType = SubscribeType.valueOf(String.valueOf(parse.get(SUBSCRIBE)));
                switch (action){
                    case subscribe:
                        final User user = userDAO.getUserById(parse.get(USER));
                        if (!userSessions.containsKey(session)){
                            userSessions.put(session, user);
                        }
                        subscribes.add(session, subscribeType, user);
                        break;
                    case unsubscribe:
//                        subscribes.remove(session, subscribeType);
                }
            }
        }
        System.out.println("Message: " + message);
    }

    @OnClose
    public void OnClose(Session session){
        System.out.println("Close session #" + session.getId());
        subscribes.remove(userSessions.remove(session));
    }

    @OnError
    public void OnError(Session session, Throwable cause){
        System.err.println("Error in session: " + session.getId() + ", cause: " + session.isOpen());
        cause.printStackTrace();
    }
}

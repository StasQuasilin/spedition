package utils.hibernate.dao;

import entity.Phone;
import entity.Role;
import entity.User;
import entity.UserAccess;
import utils.hibernate.Hibernator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static constants.Keys.*;

public class UserDAO {
    private final Hibernator hibernator = new Hibernator();

    public UserAccess getUserAccessByPhone(String number) {
        final Phone phone = hibernator.get(Phone.class, NUMBER, number);
        if (phone != null){
            return hibernator.get(UserAccess.class, USER_PERSON, phone.getPerson());
        }

        return null;
    }

    public User getUserByToken(Object token) {
        return hibernator.get(UserAccess.class, TOKEN, token).getUser();
    }

    public void save(Phone phone) {
        hibernator.save(phone);
    }

    public void remove(Phone phone) {
        hibernator.remove(phone);
    }

    public List<User> getUsersByRole(Role role) {
        return hibernator.query(User.class, ROLE, role);
    }

    public User getUserById(Object id) {
        return hibernator.get(User.class, ID, id);
    }

    public List<User> getUsersBySupervisor(User supervisor) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(USER_SUPERVISOR, supervisor);
        LinkedList<User> users = new LinkedList<>();
        for (UserAccess access : hibernator.query(UserAccess.class, param)){
            users.add(access.getUser());
        }
        return users;
    }

    public void removeUser(Object id) {
        final UserAccess access = hibernator.get(UserAccess.class, "user", id);
        if (access != null){
            hibernator.remove(access);
        }
    }

    public void setVersionCode(String token, String version) {
        final User user = getUserByToken(token);
        if (user != null){
            user.setAppVersion(version);
            hibernator.save(user);
        }
    }
}

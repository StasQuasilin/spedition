package utils.hibernate.dao;

import entity.Person;
import entity.Phone;
import entity.User;
import entity.UserAccess;
import utils.hibernate.Hibernator;

import static constants.Keys.*;

public class RegistrationDAO {

    private final Hibernator hibernator = Hibernator.getInstance();

    public User getUserByPhone(String number) {
        final Phone phone = hibernator.get(Phone.class, NUMBER, number);
        if (phone != null){
            final Person person = phone.getPerson();
            return hibernator.get(User.class, PERSON, person);
        }
        return null;
    }

    public void registration(Person person, User user, Phone phone, UserAccess access) {
        hibernator.save(person);
        hibernator.save(user);
        hibernator.save(phone);
        hibernator.save(access);
    }
}

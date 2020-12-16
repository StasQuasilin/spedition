package utils;

import entity.UserAccess;
import utils.hibernate.HibernateSessionFactory;
import utils.hibernate.Hibernator;

import java.util.Base64;
import java.util.List;

public class Run {
    public static void main(String[] args) {
        Hibernator hibernator = Hibernator.getInstance();

        final List<UserAccess> query = hibernator.query(UserAccess.class, null);
        for (UserAccess ua : query){
            final String password = ua.getPassword();
            final byte[] decode = Base64.getDecoder().decode(password);
            final int hashCode = new String(decode).hashCode();
            ua.setPasswordHash(hashCode);
            hibernator.save(ua);
        }
        HibernateSessionFactory.shutdown();
    }
}

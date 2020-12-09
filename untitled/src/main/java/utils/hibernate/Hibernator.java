package utils.hibernate;

import constants.Keys;
import org.hibernate.Session;
import utils.hibernate.DateContainers.*;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Quasilin on 09.09.2018.
 */
public class Hibernator {
    private static final Hibernator instance = new Hibernator();

    public static synchronized Hibernator getInstance() {
        return instance;
    }

    public <T>List<T> limitQuery(Class<T> tClass, HashMap<String, Object> parameters, int limit) {
        Session session = HibernateSessionFactory.getSession();
        CriteriaQuery<T> query = getCriteriaQuery(session, tClass, parameters);

        List<T> resultList = session.createQuery(query)
                .setMaxResults(limit)
                .getResultList();

        HibernateSessionFactory.putSession(session);

        return resultList;
    }
    public <T>List<T> limitQuery(Class<T> tClass, String parameter, Object value, int limit) {
        final HashMap<String, Object> param = new HashMap<>();
        param.put(parameter, value);
        return limitQuery(tClass, param, limit);
    }

    public static final String SLASH = Keys.SLASH;

    <K, T> Predicate[] buildPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> query, Root<K> root, HashMap<String, Object> param){
        if (param != null){
            Predicate[] predicates = new Predicate[param.size()];
            int i = 0;

            for (Map.Entry<String, Object> entry : param.entrySet()){

                Path<Date> path = parsePath(root, entry.getKey());

                if (entry.getValue() == null || entry.getValue().equals(State.isNull)){
                    predicates[i] = criteriaBuilder.isNull(path);
                } else if(entry.getValue().equals(State.notNull)) {
                    predicates[i] = criteriaBuilder.isNotNull(path);
                } else if (entry.getValue() instanceof EQ){
                    EQ eq = (EQ) entry.getValue();
                    predicates[i] = criteriaBuilder.greaterThanOrEqualTo(path, eq.getDate());
                }else if (entry.getValue() instanceof NOT) {
                    NOT not = (NOT) entry.getValue();
                    predicates[i] = criteriaBuilder.notEqual(path, not.getObject());
                } else if (entry.getValue() instanceof BETWEEN){
                    BETWEEN between = (BETWEEN) entry.getValue();
                    predicates[i] = criteriaBuilder.between(path, between.getFrom(), between.getTo());
                }else if (entry.getValue() instanceof GE) {
                    GE ge = (GE) entry.getValue();
                    predicates[i] = criteriaBuilder.greaterThanOrEqualTo(path, ge.getDate());
                    query.orderBy(criteriaBuilder.asc(path));
                }else if (entry.getValue() instanceof GT) {
                    GT gt = (GT) entry.getValue();
                    predicates[i] = criteriaBuilder.greaterThan(path, gt.getDate());
                    query.orderBy(criteriaBuilder.asc(path));
                } else if (entry.getValue() instanceof LE) {
                    LE le = (LE) entry.getValue();
                    predicates[i] = criteriaBuilder.lessThanOrEqualTo(path, le.getDate());
                    query.orderBy(criteriaBuilder.desc(path));
                } else if (entry.getValue() instanceof LT) {
                    LT lt = (LT) entry.getValue();
                    predicates[i] = criteriaBuilder.lessThan(path, lt.getDate());
                    query.orderBy(criteriaBuilder.desc(path));
                } else {
                    predicates[i] = criteriaBuilder.equal(path, entry.getValue());
                }
                i++;
            }
            return predicates;
        }
        return new Predicate[0];
    }

    private <T, K> Root<K> buildQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> query, Class<K> tClass, HashMap<String, Object> param){
        Root<K> from = query.from(tClass);
        query.where(buildPredicates(criteriaBuilder, query, from, param));
        return from;
    }

    private <T> CriteriaQuery<T> getCriteriaQuery(Session session, Class<T> tClass, HashMap<String, Object> parameters) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        return getCriteriaQuery(criteriaBuilder, tClass, parameters);
    }

    private <T> CriteriaQuery<T> getCriteriaQuery(CriteriaBuilder builder, Class<T> tClass, HashMap<String, Object> parameters) {
        CriteriaQuery<T> query = builder.createQuery(tClass);

        buildQuery(builder, query, tClass, parameters);

        return query;
    }

    private <T> Path<Date> parsePath(Root<T> root, String value){
        Path<Date> objectPath = null;
        String[] split = value.split(SLASH);

        for (String s : split){
            if (objectPath == null) {
                objectPath = root.get(s);
            } else {
                objectPath = objectPath.get(s);
            }
        }

        return objectPath;
    }

    public synchronized <T> float sum(Class<T> tClass, HashMap<String, Object> param, String... columns){
        Session session = HibernateSessionFactory.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Float> query = criteriaBuilder.createQuery(Float.class);
        Root<T> root = buildQuery(criteriaBuilder, query, tClass, param);

        for (String column : columns){

            Path<Float> path = null;
            for (String s : column.split(SLASH)){
                if (path == null){
                    path = root.get(s);
                }else {
                    path = path.get(s);
                }
            }
            query.select(criteriaBuilder.sum(path));
        }

        float sum = 0;
        try {
            sum = session.createQuery(query).uniqueResult();
        } catch (Exception ignored) {}

        HibernateSessionFactory.putSession(session);

        return sum;
    }

    public synchronized <T>List<T> query(Class<T> tClass, HashMap<String, Object> params){
        Session session = HibernateSessionFactory.getSession();
        CriteriaQuery<T> query = getCriteriaQuery(session, tClass, params);

        List<T> resultList = session.createQuery(query).getResultList();

        HibernateSessionFactory.putSession(session);
        putParams(params);
        return resultList;
    }

    private void putParams(HashMap<String, Object> params) {
        if (params != null) {
            params = new HashMap<>();
            pool.add(params);
        }
    }

    public void  clear(){
        HibernateSessionFactory.getSession().clear();
    }

    public <T> List<T> query(Class<T> tClass, String key, Object value) {
        HashMap<String, Object> params = getParams();
        params.put(key, value);
        return query(tClass, params);
    }

    final ArrayList<HashMap<String, Object>> pool = new ArrayList<>();
    synchronized HashMap<String, Object> getParams(){
        if (pool.size() > 0){
            return pool.remove(0);
        } else {
            return new HashMap<>();
        }
    }

    public <T>T get(Class<T> tClass, String key, Object value){
        HashMap<String, Object> params = getParams();
        params.put(key, value);
        return get(tClass, params);
    }

    public <T> T get(Class<T> tClass, HashMap<String, Object> parameters) {

        List<T> query = query(tClass, parameters);
        if (query == null || query.isEmpty()) {
            return null;
        } else {
            return query.get(0);
        }
    }

    public <T> void Clear(Class<T> tClass) {
        Session session = HibernateSessionFactory.getSession();

        List<T> query = query(tClass, null);

        query.forEach(session::delete);

        session.beginTransaction().commit();

        System.out.println("\tdelete " + query.size() + " " + tClass.getSimpleName() + " items");
        query.clear();
        HibernateSessionFactory.putSession(session);
    }

    public <T> void removeList(List<T> list) {
        Session session = HibernateSessionFactory.getSession();
        list.forEach(session::delete);
        session.beginTransaction().commit();
        HibernateSessionFactory.putSession(session);
    }
    public <T> List<T> find (Class<T> tClass, HashMap<String, String> params){
        return find(tClass, params, null);
    }

    public <T> List<T> find (Class<T> tClass, HashMap<String, String> findData, HashMap<String, Object> params){

        Session session = HibernateSessionFactory.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(tClass);
        Root<T> from = query.from(tClass);

        ArrayList<Predicate> predicates = new ArrayList<>();
        Collections.addAll(predicates, buildPredicates(criteriaBuilder, query, from, params));
        if (findData != null) {

            for (Map.Entry<String, String> entry : findData.entrySet()){
                String[] split = entry.getKey().split("/");
                Path<String> objectPath = null;

                for (String s : split){
                    if (objectPath == null) {
                        objectPath = from.get(s);
                    } else {
                        objectPath = objectPath.get(s);
                    }
                }
                Predicate predicate;
                if (entry.getValue() != null) {
                    predicate = criteriaBuilder.like(criteriaBuilder.upper(objectPath), "%" + entry.getValue().toUpperCase() + "%");
                } else {
                    predicate = criteriaBuilder.isNull(objectPath);
                }
                predicates.add(predicate);
            }
        }
        Predicate[] p = new Predicate[predicates.size()];
        predicates.toArray(p);
        query.where(p);
        List<T> resultList = session.createQuery(query).getResultList();

        HibernateSessionFactory.putSession(session);

        return resultList;
    }

    public <T> List<T> find (Class<T> tClass, String key, String value) {
        HashMap<String, String> par = new HashMap<>();
        par.put(key, value);
        return find(tClass, par);
    }

    public void save(Object ... objects) {
        Session session = HibernateSessionFactory.getSession();
        for (Object o : objects){
            session.saveOrUpdate(o);
        }
        session.beginTransaction().commit();
        HibernateSessionFactory.putSession(session);
    }

    public void remove(Object ... values) {
        Session session = HibernateSessionFactory.getSession();
        for (Object o : values){
            session.remove(o);
        }
        session.beginTransaction().commit();
        HibernateSessionFactory.putSession(session);
    }

    public void flush() {
        Session session = HibernateSessionFactory.getSession();
        session.flush();
    }

    public <T> List<T> find(Class<T> tClass, HashMap<String, Object> params, String value, String key) {
        HashMap<String, String> find = new HashMap<>();
        find.put(value, key);
        return find(tClass, find, params);
    }
}

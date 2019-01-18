package ru.otus.l14.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.l14.cache.CacheEngine;
import ru.otus.l14.cache.CacheEntry;
import ru.otus.l14.dao.UserDataSetDAO;
import ru.otus.l14.dataset.UserDataSet;
import ru.otus.l14.helper.HibernateHelper;

import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {

    private final SessionFactory sessionFactory;

    private CacheEngine<Long, CacheEntry<UserDataSet>> cache;

    public DBServiceHibernateImpl(CacheEngine cache){
        this.cache = cache;
        sessionFactory = HibernateHelper.getSessionFactory();
    }

    public String getLocalStatus() {
        return runInSession(session -> {
            return session.getTransaction().getStatus().name();
        });
    }

    public void save(UserDataSet user){
        try (Session session = sessionFactory.openSession()){
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(user);
        }
    }

    public UserDataSet read(long id) {
        CacheEntry<UserDataSet> element = cache.get(id);
        if (element != null) {
            return element.getValue();
        }

        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            UserDataSet user = dao.read(id);
            cache.put(id, new CacheEntry<>(user));
            return user;
        });
    }

    public UserDataSet readByName(String name) {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readByName(name);
        });
    }

    public List<UserDataSet> readAll() {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll();
        });
    }

    public Long count() {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.count();
        });
    }

    public void shutdown() {
        sessionFactory.close();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}

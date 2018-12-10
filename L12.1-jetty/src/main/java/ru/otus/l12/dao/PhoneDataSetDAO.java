package ru.otus.l12.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.l12.dataset.PhoneDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PhoneDataSetDAO {
    private Session session;

    public PhoneDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(PhoneDataSet dataSet) {
        session.save(dataSet);
    }

    public PhoneDataSet read(long id) {
        return session.load(PhoneDataSet.class, id);
    }

    public PhoneDataSet readByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        Root<PhoneDataSet> from = criteria.from(PhoneDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<PhoneDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<PhoneDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        criteria.from(PhoneDataSet.class);
        return session.createQuery(criteria).list();
    }
}

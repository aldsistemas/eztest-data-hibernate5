package br.com.eztest.dcv.hibernate;

import br.com.eztest.dcv.ContextLoader;
import br.com.eztest.dcv.DataUnit;
import org.hibernate.Session;

import java.util.*;
import java.util.Map.Entry;

public abstract class ContextHibernateLoader implements ContextLoader<Object> {

    public abstract Collection<ContextEntityMapping> getEntities();

    public abstract Session getSession();

    @Override
    public List<DataUnit<Object>> loadData() {

        final List<DataUnit<Object>> ret = new ArrayList<DataUnit<Object>>();
        final Collection<ContextEntityMapping> ents = getEntities();
        final Session session = getSession();
        session.getTransaction().begin();
        final Map<Object, ContextEntityMapping> fullList = new HashMap<Object, ContextEntityMapping>();
        for (final ContextEntityMapping map : ents) {
            final Class<?> entityClass = map.getEntityClass();
            final List<?> list = session.createCriteria(entityClass).list();
            for (final Object o : list) {
                fullList.put(o, map);
            }
        }

        for (final Entry<Object, ContextEntityMapping> o : fullList.entrySet()) {
            final ContextEntityMapping map = o.getValue();
            final Object key = o.getKey();
            ret.add(new DataUnit<Object>(key.getClass().getName(), map.getMapper().getId(key), key, map.getMapper()));
        }
        session.flush();
        session.getTransaction().commit();
        if (session.isOpen()) {
            session.close();
        }
        return ret;
    }
}

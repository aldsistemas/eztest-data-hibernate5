package br.com.eztest.dcv.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

/**
 * Loader de dados default que usa mapeamentos hibernate. Os dados carregados consistem no resultado de uma obtencao das listas de entidades.
 * <p>
 * Para cada entidade e usado o {@link HibernateEntityDataMapper}.
 *
 * @see HibernateEntityDataMapper
 */
public class DefaultContextHibernateLoader extends ContextHibernateLoader {

    private final HibernateProperties properties;
    private final List<Class<?>> entityClasses;
    private SessionFactory sf;
    private List<ContextEntityMapping> classes;
//    private final ContextConfiguration configuration;
//    private Configuration              cfg;

//    public DefaultContextHibernateLoader(final String file) {
//        this(file, new ContextConfiguration());
//    }

    //    public DefaultContextHibernateLoader(final Configuration config, final ContextConfiguration configuration) {
//        this.cfg = config;
//        this.configuration = configuration;
//        init();
//    }
    public DefaultContextHibernateLoader(final HibernateProperties properties, List<Class<?>> entityClasses) {
        this.properties = properties;
        this.entityClasses = entityClasses;
        init();
    }

//    public DefaultContextHibernateLoader(final String file, final ContextConfiguration configuration) {
//        this.cfg = new Configuration().configure(file);
//        this.configuration = configuration;
//        init();
//    }

    @Override
    public Collection<ContextEntityMapping> getEntities() {
        return this.classes;
    }

    @Override
    public Session getSession() {
        return this.sf.openSession();
    }

    private void init() {

        Configuration config = new Configuration();
        this.properties.getProperties().forEach(config::setProperty);
        this.entityClasses.forEach(config::addAnnotatedClass);

        this.classes = new ArrayList<>();
        this.entityClasses.forEach(c -> this.classes.add(new ContextEntityMapping(c.getName(), c, new HibernateEntityDataMapper())));

        this.sf = config.buildSessionFactory();
        this.classes = Collections.unmodifiableList(this.classes);
    }
}

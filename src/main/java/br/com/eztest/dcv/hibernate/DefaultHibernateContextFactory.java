package br.com.eztest.dcv.hibernate;

import br.com.eztest.dcv.Context;
import br.com.eztest.dcv.ContextFactory;
import br.com.eztest.dcv.ContextLoader;
import br.com.eztest.dcv.DataContext;

import java.util.List;

/**
 * {@link br.com.eztest.dcv.ContextFactory} padrao para dados mapeados por hibernate.
 */
public final class DefaultHibernateContextFactory implements ContextFactory<Object> {

    private final ContextLoader<Object> loader;

    private DefaultHibernateContextFactory(final ContextLoader<Object> loader) {
        this.loader = loader;
    }

    @Override
    public Context<Object> createContext() {
        return new DefaultHibernateContext(this.loader);
    }

    public static ContextFactory<Object> instance(final HibernateProperties properties, List<Class<?>> entityClasses) {
        return new DefaultHibernateContextFactory(new DefaultContextHibernateLoader(properties, entityClasses));
    }

    public static class DefaultHibernateContext extends DataContext<Object> {

        public DefaultHibernateContext(final ContextLoader<Object> loader) {
            super(loader);
        }
    }
}

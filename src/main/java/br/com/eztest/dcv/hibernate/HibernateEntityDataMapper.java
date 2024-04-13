package br.com.eztest.dcv.hibernate;

import br.com.eztest.dcv.ContextIdGenerator;
import br.com.eztest.dcv.FieldDataMapper;
import br.com.eztest.dcv.FieldIdGenerator;
import br.com.eztest.dcv.MethodIdGenerator;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper de entidades hibernate. Traduz os dados contidos em entidades para um formato que possa ser processado pelos
 * comparadores de contexto.
 * 
 * 
 * 
 *TODO: levantar o mapeamento de componentes da entidade mapeados nos getters/setters e os atributos que sao mapeados
 * por default, ou seja, quando nao contem nenhuma anotacao @Column
 * 
 * TODO: considerar colecoes ordenadas. Atualmente as comparacoes em colecoes sao feitas apenas por conteudo, e nao por
 * ordem.
 * 
 * TODO: tratar mapeamentos unidirecionais do tipo Set sem uma contrapartida ManyToOne (no caso do OneToMany) e/ou que
 * envolve uma tabela de ligacao (no caso do ManyToMany).
 */
public class HibernateEntityDataMapper extends FieldDataMapper {

    private final Map<Class<?>, ContextIdGenerator<Object>> idGenerator = new HashMap<>();

    @Override
    public Object getId(final Object o) {

        final Class<? extends Object> c = o.getClass();
        ContextIdGenerator<Object> f = this.idGenerator.get(c);
        if (f == null) {
            calculaId(o);
            f = this.idGenerator.get(c);
            if (f == null) {
                throw new RuntimeException("Classe sem atributos/metodos anotados com @Id");
            }
        }
        return f.generateId(o);
    }

    // @Override
    // protected List<Field> getFields(final Class<? extends Object> class1) {
    //
    // final List<Field> pre = super.getFields(class1);
    // final List<Field> ret = new ArrayList<Field>();
    // for (final Field f : pre) {
    // final int mod = f.getModifiers();
    // if (!Modifier.isStatic(mod)) {
    // if (f.isAnnotationPresent(Transient.class)) {
    // // Nao considera atributos nao persistidos
    // continue;
    // } else if (f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class)) {
    // // Considera o mapeamento direcionado a uma unica entidade.
    // addEntity(f);
    // } else if (!f.isAnnotationPresent(OneToMany.class) && !f.isAnnotationPresent(ManyToAny.class)) {
    // // Atributo de colecao
    // addCollection(f);
    // } else if (f.isAnnotationPresent(Column.class)) {
    // ret.add(f);
    // }
    // }
    // }
    // return ret;
    // }
    @Override
    protected List<Field> getFields(final Class<? extends Object> class1) {

        final List<Field> pre = super.getFields(class1);
        final List<Field> ret = new ArrayList<Field>();
        for (final Field f : pre) {
            final int mod = f.getModifiers();
            if (!Modifier.isStatic(mod)) {
                if (!f.isAnnotationPresent(OneToMany.class) && !f.isAnnotationPresent(ManyToAny.class)
                        && !f.isAnnotationPresent(Transient.class)) {
                    if (f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(Column.class)
                            || f.isAnnotationPresent(OneToOne.class)) {
                        ret.add(f);
                    }
                }
            }
        }
        return ret;
    }

    private void calculaId(final Object o) {

        Class<?> c = o.getClass();
        while (!c.equals(Object.class)) {

            final Field[] fds = c.getDeclaredFields();
            for (final Field f : fds) {
                if (f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(EmbeddedId.class)) {
                    f.setAccessible(true);
                    this.idGenerator.put(o.getClass(), new FieldIdGenerator<>(f));
                    return;
                }
            }
            for (final Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Id.class) || m.isAnnotationPresent(EmbeddedId.class)) {
                    m.setAccessible(true);
                    this.idGenerator.put(o.getClass(), new MethodIdGenerator<>(m));
                    return;
                }
            }
            c = c.getSuperclass();
        }
    }
}

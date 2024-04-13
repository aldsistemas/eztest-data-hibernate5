package br.com.eztest.dcv.hibernate;

import br.com.eztest.dcv.DataValuesMapper;

public class ContextEntityMapping {

    private final Class<?>         entityClass;
    private final String           dataType;

    private final DataValuesMapper mapper;

    public ContextEntityMapping(final String dataType, final Class<?> entityClass, final DataValuesMapper mapper) {
        this.dataType = dataType;
        this.entityClass = entityClass;
        this.mapper = mapper;
    }

    public String getDataType() {
        return this.dataType;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public DataValuesMapper getMapper() {
        return this.mapper;
    }
}

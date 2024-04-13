package br.com.eztest.dcv.hibernate;

import java.util.HashMap;
import java.util.Map;

public class HibernateProperties {

    private String url;
    private String username;
    private String password;
    private String dialect;
    private String defaultSchema;
    private boolean flushBeforeCompletion = true;
    private String currentSessionContextClass = "thread";

    private Map<String, String> properties = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public boolean isFlushBeforeCompletion() {
        return flushBeforeCompletion;
    }

    public void setFlushBeforeCompletion(boolean flushBeforeCompletion) {
        this.flushBeforeCompletion = flushBeforeCompletion;
    }

    public String getCurrentSessionContextClass() {
        return currentSessionContextClass;
    }

    public void setCurrentSessionContextClass(String currentSessionContextClass) {
        this.currentSessionContextClass = currentSessionContextClass;
    }

    public void setProperty(String key, String value) {
        if ("hibernate.connection.username".equals(key)) {
            setUsername(value);
        } else if ("hibernate.connection.password".equals(key)) {
            setPassword(value);
        } else if ("hibernate.connection.url".equals(key)) {
            setUrl(value);
        } else if ("hibernate.dialect".equals(key)) {
            setDialect(value);
        } else if ("hibernate.default_schema".equals(key)) {
            setDefaultSchema(value);
        } else if ("hibernate.transaction.flush_before_completion".equals(key)) {
            setFlushBeforeCompletion(Boolean.parseBoolean(value));
        } else if ("hibernate.current_session_context_class".equals(key)) {
            setCurrentSessionContextClass(value);
        } else {
            properties.put(key, value);
        }
    }

    public Map<String, String> getProperties() {
        Map<String, String> ret = new HashMap<>();
        ret.putAll(this.properties);
        ret.put("hibernate.connection.url", this.url);
        ret.put("hibernate.connection.username", this.username);
        ret.put("hibernate.connection.password", this.password);
        ret.put("hibernate.dialect", this.dialect);
        ret.put("hibernate.default_schema", this.defaultSchema);
        ret.put("hibernate.transaction.flush_before_completion", String.valueOf(this.flushBeforeCompletion));
        ret.put("hibernate.current_session_context_class", this.currentSessionContextClass);
        return ret;
    }
}
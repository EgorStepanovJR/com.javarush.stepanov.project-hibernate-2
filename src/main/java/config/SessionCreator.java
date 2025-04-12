package config;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum SessionCreator implements AutoCloseable {
    INSTANCE;
    private final SessionFactory sessionFactory;

    SessionCreator() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    public void close() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            System.err.println(new StringBuilder().append("Ошибка закрытия сессии ").append(e));
        }
    }

    public static SessionCreator getInstance() {
        return INSTANCE;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

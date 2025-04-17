package dao;

import entity.Store;
import org.hibernate.SessionFactory;

public class StoreDAO extends GenericDAO<Store> {
    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }

    public Store getById(byte id) {
        return getCurrentSession().get(Store.class, id);
    }

    public void deleteById(byte entityId) {
        final Store entity = getById(entityId);
        getCurrentSession().delete(entity);
    }
}

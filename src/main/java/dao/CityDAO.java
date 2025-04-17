package dao;

import entity.City;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CityDAO extends GenericDAO<City> {
    public CityDAO(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }

    public City getByName(String name) {
        Query<City> query = getCurrentSession().createQuery("from City c where c.city = :name", City.class);
        query.setParameter("name", name);
        query.setMaxResults(1);

        return query.getSingleResult();
    }
}

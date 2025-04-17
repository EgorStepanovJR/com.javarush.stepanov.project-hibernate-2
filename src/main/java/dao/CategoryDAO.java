package dao;

import entity.Category;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CategoryDAO extends GenericDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }

    public Category getByName(String name) {
        Query<Category> categoryQuery = getCurrentSession().createQuery("from Category c where c.name = :name", Category.class);
        categoryQuery.setParameter("name", name);
        Category category = categoryQuery.getSingleResult();

        return categoryQuery.getSingleResult();
    }
}

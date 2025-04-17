package dao;

import entity.Film;
import entity.Inventory;
import entity.Store;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class InventoryDAO extends GenericDAO<Inventory> {
    private final String SELECT_INVENTORY_BY_FILM_AND_STORE = "from Inventory i where i.film = :film and i.store = :store";
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }

    public Inventory getInventoryByFilmAndByStore(Film film, Store store) {
        Query<Inventory> inventoryQuery = getCurrentSession()
                .createQuery(SELECT_INVENTORY_BY_FILM_AND_STORE, Inventory.class);

        inventoryQuery.setParameter("film",film);
        inventoryQuery.setParameter("store", store);
        inventoryQuery.setMaxResults(1);
        Inventory result = inventoryQuery.getSingleResult();

        return result;
    }
}

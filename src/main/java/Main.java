import config.MySessionFactory;
import dao.*;
import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class Main {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        this.sessionFactory = new MySessionFactory().getSessionFactory();

        this.actorDAO = new ActorDAO(sessionFactory);
        this.addressDAO = new AddressDAO(sessionFactory);
        this.categoryDAO = new CategoryDAO(sessionFactory);
        this.cityDAO = new CityDAO(sessionFactory);
        this.countryDAO = new CountryDAO(sessionFactory);
        this.customerDAO = new CustomerDAO(sessionFactory);
        this.filmDAO = new FilmDAO(sessionFactory);
        this.filmTextDAO = new FilmTextDAO(sessionFactory);
        this.inventoryDAO = new InventoryDAO(sessionFactory);
        this.languageDAO = new LanguageDAO(sessionFactory);
        this.paymentDAO = new PaymentDAO(sessionFactory);
        this.rentalDAO = new RentalDAO(sessionFactory);
        this.staffDAO = new StaffDAO(sessionFactory);
        this.storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Customer customer = main.createCustomer();
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).get(0);
            City city = cityDAO.getByName("Okinawa");

            Address address = Address.builder()
                    .city(city)
                    .district("Guamri, Jeomgok-myeon, 298-4")
                    .address("Uiseong-gun, Gyeongsangbuk-do")
                    .postalCode("769-831")
                    .phone("+82-4-275-1624")
                    .build();
            addressDAO.save(address);

//            Address address = new Address();
//            address.setCity(city);
//            address.setDistrict("Guamri, Jeomgok-myeon, 298-4");
//            address.setAddress("Uiseong-gun, Gyeongsangbuk-do");
//            address.setPostalCode("769-831");
//            address.setPhone("+82-4-275-1624");
//            addressDAO.save(address);

            Customer newCustomer = Customer.builder()
                    .address(address)
                    .store(store)
                    .firstName("Sung-hoon")
                    .lastName("Don")
                    .email("sunghoondon@live.com")
                    .isActive(true)
                    .build();
            customerDAO.save(newCustomer);

//            Customer customer = new Customer();
//            customer.setAddress(address);
//            customer.setStore(store);
//            customer.setFirstName("Sung-hoon");
//            customer.setLastName("Don");
//            customer.setEmail("sunghoondon@live.com");
//            customer.setIsActive(true);
//            customerDAO.save(customer);

            transaction.commit();
            return newCustomer;
        }
    }
}

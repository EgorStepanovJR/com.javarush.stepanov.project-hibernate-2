import config.MySessionFactory;
import dao.*;
import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
        System.out.println("Создан покупатель: " + customer.getFirstName());
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).getFirst();
            City city = cityDAO.getByName("Okinawa");

            Address address = Address.builder()
                    .city(city)
                    .district("Fukuchiyama-shi")
                    .address("133-1180, Kitahiranocho")
                    .postalCode("620-0811")
                    .phone("+8159-655-5523")
                    .build();
            addressDAO.save(address);

            Customer newCustomer = Customer.builder()
                    .address(address)
                    .store(store)
                    .firstName("Choshi")
                    .lastName("Hirata")
                    .email("hiratachoshi@project.jp")
                    .isActive(true)
                    .build();
            customerDAO.save(newCustomer);

            transaction.commit();
            return newCustomer;
        }
    }

    private Film createNewFilm() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Language language = languageDAO.getByNameIfExist("English");

            Category category = categoryDAO.getByName("Action");
            Set<Category> categorySet = new HashSet<>();
            categorySet.add(category);

            Actor actor1 = actorDAO.getByFirstNameAndLastNameIfExist("RYAN", "GOSLING");
            Actor actor2 = actorDAO.getByFirstNameAndLastNameIfExist("EMILY", "BLUNT");
            Actor actor3 = actorDAO.getByFirstNameAndLastNameIfExist("Drew", "Pearce");
            Actor actor4 = actorDAO.getByFirstNameAndLastNameIfExist("THE", "DOG");
            Set<Actor> actorSet = new HashSet<>();
            actorSet.add(actor1);
            actorSet.add(actor2);
            actorSet.add(actor3);
            actorSet.add(actor4);

            Film newFilmToDB = Film.builder()
                    .title("THE FALL GUY")
                    .releaseYear(Year.now())
                    .language(language)
                    .rentalDuration(Byte.valueOf( "3"))
                    .rentalRate(BigDecimal.valueOf(3.99))
                    .replacementCost(BigDecimal.valueOf(21.19))
                    .rating(Rating.PG_13)
                    .specialFeatures("Trailers,Commentaries")
                    .categories(categorySet)
                    .actors(actorSet)
                    .build();
            filmDAO.save(newFilmToDB);

            Store store1 = storeDAO.getById(Byte.parseByte("1"));
            Store store2 = storeDAO.getById(Byte.parseByte("2"));

            Inventory inventory1 = Inventory.builder()
                    .film(newFilmToDB)
                    .store(store1)
                    .build();
            Inventory inventory2 = Inventory.builder()
                    .film(newFilmToDB)
                    .store(store2)
                    .build();
            inventoryDAO.save(inventory1);
            inventoryDAO.save(inventory2);

            transaction.commit();
            return newFilmToDB;
        }
    }

    private List<Film> getAllAvailableFilmsForRent(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Store store = customer.getStore();
            List<Film> availableFilms = filmDAO.getAvailableFilms(store);

            transaction.commit();

            return availableFilms;
        }
    }

    private void returnFilmToStore(Rental rental) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Rental tempRental = rentalDAO.getById(rental.getId());
            tempRental.setReturnDate(LocalDateTime.now());
            rentalDAO.update(tempRental);

            transaction.commit();
        }
    }

    private Rental rentFilm(Customer customer, Film film) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Store store = customer.getStore();
            Inventory inventory = inventoryDAO.getInventoryByFilmAndByStore(film, store);

            Rental newRental = Rental.builder()
                    .rentalDate(LocalDateTime.now())
                    .inventory(inventory)
                    .customer(customer)
                    .staff(store.getManagerStaff())
                    .build();
            rentalDAO.save(newRental);

            Payment paymentForNewRental = Payment.builder()
                    .customer(customer)
                    .staff(store.getManagerStaff())
                    .rental(newRental)
                    .amount(inventory.getFilm().getRentalRate())
                    .paymentDate(LocalDateTime.now())
                    .build();
            paymentDAO.save(paymentForNewRental);

            transaction.commit();

            return newRental;
        }
    }
}

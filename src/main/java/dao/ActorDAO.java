package dao;

import entity.Actor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import static java.util.Objects.isNull;

public class ActorDAO extends GenericDAO<Actor> {


    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }

    public Actor getByFirstNameAndLastNameIfExist(String firstName, String lastName) {
        String GET_BY_FULLNAME = "from Actor a where a.firstName = :first_name and a.lastName = : last_name";

        Query<Actor> actorQuery = getCurrentSession().createQuery(GET_BY_FULLNAME, Actor.class);
        actorQuery.setParameter("first_name", firstName);
        actorQuery.setParameter("last_name", lastName);
        Actor actor = actorQuery.getSingleResult();
        if (isNull(actor)) {
            Actor newActorToDB = Actor.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();

            return save(newActorToDB);
        }
        return actor;
    }
}

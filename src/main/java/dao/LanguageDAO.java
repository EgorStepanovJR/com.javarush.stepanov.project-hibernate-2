package dao;

import entity.Language;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import static java.util.Objects.isNull;

public class LanguageDAO extends GenericDAO<Language> {
    private final String GET_LANGUAGE_BY_NAME_IF_EXIST = "from Language l where l.name = :name";

    public LanguageDAO(SessionFactory sessionFactory) {
        super(Language.class, sessionFactory);
    }

    public Language getByNameIfExist(String name) {
        Query<Language> languageQuery = getCurrentSession().createQuery(GET_LANGUAGE_BY_NAME_IF_EXIST, Language.class);
        languageQuery.setParameter("name", name);
        Language language = languageQuery.getSingleResult();
        if (isNull(language)) {

            Language newLanguageToDB = Language.builder()
                    .name(name)
                    .build();

            return save(newLanguageToDB);
        }

        return language;
    }
}

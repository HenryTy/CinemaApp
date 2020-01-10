package ty.henry.cinemaapp.persistence;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Session getHibernateSession() {
        return entityManager.unwrap(Session.class);
    }
}

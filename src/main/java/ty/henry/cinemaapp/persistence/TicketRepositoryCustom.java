package ty.henry.cinemaapp.persistence;

import org.hibernate.Session;

public interface TicketRepositoryCustom {

    Session getHibernateSession();
}

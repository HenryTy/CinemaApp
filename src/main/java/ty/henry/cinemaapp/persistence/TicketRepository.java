package ty.henry.cinemaapp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.model.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String>, TicketRepositoryCustom {

    List<Ticket> findAllByShowing(Showing showing);
}

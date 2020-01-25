package ty.henry.cinemaapp.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.model.Ticket;
import ty.henry.cinemaapp.model.User;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String>, TicketRepositoryCustom {

    List<Ticket> findAllByShowing(Showing showing);

    List<Ticket> findAllByShowingAndTicketNumberStartsWith(Showing showing, String numberBegin, Sort sort);

    List<Ticket> findAllByUser(User user, Sort sort);

    List<Ticket> findByUserAndShowing_Movie(User user, Movie movie);
}

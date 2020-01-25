package ty.henry.cinemaapp.service;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDatabaseException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.TicketForm;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.*;
import ty.henry.cinemaapp.persistence.ShowingRepository;
import ty.henry.cinemaapp.persistence.TicketRepository;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    public List<Showing> findShowingsForMovieAndDate(Movie movie, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59);
        return showingRepository.findAllByMovieAndShowingDateBetween(movie, from, to, Sort.by("showingDate"));
    }

    public List<Showing> findFutureShowingsForMovieAndDate(Movie movie, LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59);

        if(now.isAfter(from)) {
            from = now;
        }
        return showingRepository.findAllByMovieAndShowingDateBetween(movie, from, to, Sort.by("showingDate"));
    }

    public Showing findShowingById(Long id) {
        return showingRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }

    public List<Reservation> findReservationsForShowing(Showing showing) {
        List<Ticket> showingTickets = ticketRepository.findAllByShowing(showing);
        List<Reservation> showingReservations = new ArrayList<>();

        for(Ticket t : showingTickets) {
            showingReservations.addAll(t.getReservations());
        }

        return showingReservations;
    }

    public List<Ticket> findTicketsForShowing(Showing showing, String search) {
        return ticketRepository.findAllByShowingAndTicketNumberStartsWith(showing, search, Sort.by("ticketNumber"));
    }

    public List<Ticket> findTicketsForUser(User user) {
        return ticketRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "purchaseDate"));
    }

    public boolean hasUserTicketForMovie(User user, Movie movie) {
        return ticketRepository.findByUserAndShowing_Movie(user, movie).size() > 0;
    }

    public Ticket findTicketByNumber(String ticketNumber) {
        return ticketRepository.findById(ticketNumber).orElseThrow(EntityNotExistException::new);
    }

    public void markReservedSeats(TicketForm ticketForm) {
        List<Reservation> showingReservations = findReservationsForShowing(ticketForm.getShowing());
        for(Reservation r : showingReservations) {
            ticketForm.setReserved(r.getRowNumber(), r.getSeatInRow());
        }
    }

    public void buyTicket(TicketForm ticketForm) {
        List<int[]> clickedSeats = ticketForm.getClickedSeats();

        if(clickedSeats.size() == 0) {
            ticketForm.setSuccessfulPurchase(false);
            ticketForm.setErrorMessage("Wybierz co najmniej jedno miejsce");
            return;
        }

        int[] rows = new int[clickedSeats.size()];
        int[] seats = new int[clickedSeats.size()];

        for(int i = 0; i < clickedSeats.size(); i++) {
            rows[i] = clickedSeats.get(i)[0];
            seats[i] = clickedSeats.get(i)[1];
        }

        Session session = ticketRepository.getHibernateSession();

        session.doWork( connection -> {
            try (CallableStatement function = connection
                    .prepareCall(
                            "{ ? = call add_ticket(?, ?, ?, ?) }"
                    )
            ) {
                function.registerOutParameter(1, Types.VARCHAR);
                function.setLong( 2, ticketForm.getShowingId());
                function.setInt(3, ticketForm.getUserId());

                OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
                Array rowsArray = oracleConnection.createOracleArray("NUMBER_ARRAY", rows);
                Array seatsArray = oracleConnection.createOracleArray("NUMBER_ARRAY", seats);

                function.setArray(4, rowsArray);
                function.setArray(5, seatsArray);

                function.execute();
                ticketForm.setGeneratedTicketNumber(function.getString(1));
                ticketForm.setSuccessfulPurchase(true);
            } catch (SQLException ex) {
                if(ex.getCause() != null && ex.getCause() instanceof OracleDatabaseException &&
                        ((OracleDatabaseException) ex.getCause()).getOracleErrorNumber() == 20000) {
                    ticketForm.setSuccessfulPurchase(false);
                    ticketForm.setErrorMessage("Niektóre z wybranych przez Ciebie" +
                            " miejsc zostały w międzyczasie zarezerwowane. Spróbuj ponownie.");
                    return;
                }
                else {
                    throw ex;
                }
            }
        } );

        if(ticketForm.isSuccessfulPurchase()) {
            userService.addPointToUser(ticketForm.getUser());
        }
    }

    public void validateTicket(Ticket ticket) {
        ticket.setStatus(TicketStatus.USED);
        ticketRepository.save(ticket);
    }
}

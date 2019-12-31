package ty.henry.cinemaapp.model;

import javax.persistence.*;

@Entity
public class Reservation {

    @EmbeddedId
    private ReservationId id;

    @ManyToOne
    @MapsId("ticketNumber")
    private Ticket ticket;

    public ReservationId getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getRowNumber() {
        return id.getRowNumber();
    }

    public int getSeatInRow() {
        return id.getSeatInRow();
    }
}

package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ticket {

    @Id
    private String ticketNumber;

    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "showingId", nullable = false)
    private Showing showing;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Showing getShowing() {
        return showing;
    }

    public User getUser() {
        return user;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}

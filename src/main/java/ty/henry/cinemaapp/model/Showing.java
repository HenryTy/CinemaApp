package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Showing implements Comparable<Showing> {

    @SequenceGenerator(name = "SEQ_SHOWING", sequenceName = "SEQ_SHOWING")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SHOWING")
    private Long id;

    private LocalDateTime showingDate;

    @ManyToOne
    @JoinColumn(name = "movieId", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hallId", nullable = false)
    private Hall hall;

    private Showing() {

    }

    public Showing(Movie movie, Hall hall, LocalDateTime showingDate) {
        this.showingDate = showingDate;
        this.movie = movie;
        this.hall = hall;
    }

    public LocalDateTime getShowingDate() {
        return showingDate;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setShowingDate(LocalDateTime showingDate) {
        this.showingDate = showingDate;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(other == null || other.getClass() != getClass()) {
            return false;
        }
        Showing otherShowing = (Showing) other;
        return Objects.equals(movie, otherShowing.movie)
                && Objects.equals(hall, otherShowing.hall)
                && Objects.equals(showingDate, otherShowing.showingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, hall, showingDate);
    }

    @Override
    public int compareTo(Showing other) {
        return showingDate.compareTo(other.showingDate);
    }
}

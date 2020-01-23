package ty.henry.cinemaapp.model;

import javax.persistence.*;

@Entity
public class Rating {

    @EmbeddedId
    private RatingId id;

    private int value;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movieId", nullable = false)
    private Movie movie;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public RatingId getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

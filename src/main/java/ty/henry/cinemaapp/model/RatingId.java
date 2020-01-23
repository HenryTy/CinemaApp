package ty.henry.cinemaapp.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingId implements Serializable {

    private int movieId;
    private int userId;

    public RatingId() {

    }

    public RatingId(int movieId, int userId) {
        this.movieId = movieId;
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(this == other) return true;
        if(other.getClass() != getClass()) return false;
        RatingId otherRatingId = (RatingId) other;
        return movieId == otherRatingId.movieId
                && userId == otherRatingId.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, userId);
    }
}

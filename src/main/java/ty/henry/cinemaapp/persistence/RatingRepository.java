package ty.henry.cinemaapp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Rating;
import ty.henry.cinemaapp.model.RatingId;
import ty.henry.cinemaapp.model.User;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    Rating findByUserAndMovie(User user, Movie movie);

    @Query("SELECT COUNT(r.value) FROM Rating r WHERE r.movie = ?1")
    int getRatingCountForMovie(Movie movie);

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.movie = ?1")
    float getAvgRatingForMovie(Movie movie);
}

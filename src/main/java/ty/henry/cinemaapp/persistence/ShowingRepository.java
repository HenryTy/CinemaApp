package ty.henry.cinemaapp.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Showing;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Long> {

    List<Showing> findAllByHallAndShowingDateBetween(Hall hall, LocalDateTime from, LocalDateTime to, Sort sort);

    List<Showing> findAllByMovie(Movie movie, Sort sort);

    List<Showing> findAllByMovieInAndShowingDateAfter(List<Movie> movies, LocalDateTime beforeShowings, Sort sort);

    List<Showing> findAllByMovieAndShowingDateAfter(Movie movie, LocalDateTime beforeShowings, Sort sort);

    List<Showing> findAllByHallAndShowingDateAfter(Hall hall, LocalDateTime beforeShowings);

    List<Showing> findAllByShowingDateAfter(LocalDateTime beforeShowings, Sort sort);

    List<Showing> findAllByMovieAndShowingDateBetween(Movie movie, LocalDateTime from, LocalDateTime to, Sort sort);

    @Query(value = "SELECT COUNT(sh.showing_id) FROM showing sh JOIN movie m USING(movie_id)" +
            " WHERE ?1 < sh.showing_date + m.length_minutes/1440" +
            " AND ?2 > sh.showing_date" +
            " AND sh.hall_id = ?3", nativeQuery = true)
    int countShowingCollisions(LocalDateTime newShowingStartDate, LocalDateTime newShowingEndDate, int hallId);
}

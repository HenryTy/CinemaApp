package ty.henry.cinemaapp.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Showing> findAllByMovieAndShowingDateAfter(Movie movie, LocalDateTime beforeShowings, Sort sort);

    List<Showing> findAllByHallAndShowingDateAfter(Hall hall, LocalDateTime beforeShowings);

    List<Showing> findAllByShowingDateAfter(LocalDateTime beforeShowings, Sort sort);

    List<Showing> findAllByMovieAndShowingDateBetween(Movie movie, LocalDateTime from, LocalDateTime to, Sort sort);
}

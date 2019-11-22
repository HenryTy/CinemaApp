package ty.henry.cinemaapp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    boolean existsByTitleAndProductionYear(String title, Integer productionYear);

    boolean existsByTitleAndProductionYearAndIdNot(String title, Integer productionYear, Integer id);

    Iterable<Movie> findAllByTitleContainsIgnoreCase(String title);
}

package ty.henry.cinemaapp.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Movie;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    boolean existsByTitleAndProductionYear(String title, Integer productionYear);

    boolean existsByTitleAndProductionYearAndIdNot(String title, Integer productionYear, Integer id);
}

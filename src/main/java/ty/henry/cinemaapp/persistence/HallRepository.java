package ty.henry.cinemaapp.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Hall;

@Repository
public interface HallRepository extends PagingAndSortingRepository<Hall, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}

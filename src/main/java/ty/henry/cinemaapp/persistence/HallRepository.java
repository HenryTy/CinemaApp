package ty.henry.cinemaapp.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Hall;

@Repository
public interface HallRepository extends CrudRepository<Hall, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}

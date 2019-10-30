package ty.henry.cinemaapp.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.User;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}

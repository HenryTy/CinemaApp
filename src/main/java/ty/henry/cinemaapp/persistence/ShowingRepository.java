package ty.henry.cinemaapp.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Showing;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Long> {

    List<Showing> findAllByHallAndShowingDateBetween(Hall hall, LocalDateTime from, LocalDateTime to, Sort sort);
}

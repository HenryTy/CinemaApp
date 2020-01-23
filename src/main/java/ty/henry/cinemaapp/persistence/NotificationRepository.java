package ty.henry.cinemaapp.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ty.henry.cinemaapp.model.Notification;
import ty.henry.cinemaapp.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findAllByUsers(User user, Sort sort);
}

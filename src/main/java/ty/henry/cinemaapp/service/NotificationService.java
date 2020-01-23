package ty.henry.cinemaapp.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.CancelShowingForm;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Notification;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.persistence.NotificationRepository;
import ty.henry.cinemaapp.persistence.TicketRepository;

import java.sql.CallableStatement;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    NotificationRepository notificationRepository;

    public void cancelShowing(CancelShowingForm cancelShowingForm) {
        Session session = ticketRepository.getHibernateSession();
        session.doWork(connection -> {
            try (CallableStatement procedure = connection
                    .prepareCall(
                            "{ call cancel_showing(?, ?) }"
                    )
            ) {
                procedure.setLong(1, cancelShowingForm.getShowingId());
                procedure.setString(2, cancelShowingForm.getMessage());
                procedure.execute();
            }
        });
    }

    public List<Notification> findNotificationsForUser(User user) {
        return notificationRepository.findAllByUsers(user, Sort.by(Sort.Direction.DESC, "id"));
    }

    public Notification findNotificationById(int id) {
        return notificationRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }

    public void markNotificationAsRead(int userId, int notificationId) {
        Session session = ticketRepository.getHibernateSession();
        session.doWork(connection -> {
            try (CallableStatement procedure = connection
                    .prepareCall(
                            "{ call mark_notification_as_read(?, ?) }"
                    )
            ) {
                procedure.setInt(1, userId);
                procedure.setInt(2, notificationId);
                procedure.execute();
            }
        });
    }
}

package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Notification {

    @SequenceGenerator(name = "SEQ_NOTIFICATION", sequenceName = "SEQ_NOTIFICATION", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTIFICATION")
    private Integer id;

    private String title;
    private String message;

    @ManyToMany(mappedBy = "notifications")
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getUsers() {
        return users;
    }
}

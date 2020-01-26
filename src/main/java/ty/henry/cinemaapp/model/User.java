package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    @Column(name = "user_id")
    private Integer id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @ManyToMany
    @JoinTable(
            name = "Us_Not_Rel",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "notification_id")}
    )
    private List<Notification> notifications;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public Integer getPoints() {
        return points;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public int age() {
        return (int) dateOfBirth.until(LocalDate.now(), ChronoUnit.YEARS);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}

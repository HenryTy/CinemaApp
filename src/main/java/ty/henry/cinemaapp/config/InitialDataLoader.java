package ty.henry.cinemaapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ty.henry.cinemaapp.model.Role;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.persistence.UserRepository;

import java.time.LocalDate;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        createUserIfNotFound("Admin", "Admin", LocalDate.of(1970, 1, 1),
                "admin@admin", "admin", Role.ROLE_ADMIN, 0);

        alreadySetup = true;
    }

    @Transactional
    void createUserIfNotFound(String name, String surname, LocalDate dateOfBirth, String email,
                              String password, Role role, int points) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setDateOfBirth(dateOfBirth);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setPoints(points);
            userRepository.save(user);
        }
    }
}

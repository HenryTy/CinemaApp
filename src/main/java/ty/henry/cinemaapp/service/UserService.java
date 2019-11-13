package ty.henry.cinemaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ty.henry.cinemaapp.dto.UserForm;
import ty.henry.cinemaapp.error.UserAlreadyExistsException;
import ty.henry.cinemaapp.error.UserDoesntExistException;
import ty.henry.cinemaapp.model.Role;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.persistence.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserDoesntExistException("There's no user with email " + email);
        }
        return user;
    }

    @Transactional
    public User registerNewUserAccount(UserForm userForm)
            throws UserAlreadyExistsException {
        if(emailExists(userForm.getEmail())) {
            throw new UserAlreadyExistsException("Account with email "
                    + userForm.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setSurname(userForm.getSurname());
        user.setDateOfBirth(userForm.getDateOfBirth());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setPoints(0);
        return userRepository.save(user);
    }

    public User editUser(User user, UserForm userForm) {
        user.setName(userForm.getName());
        user.setSurname(userForm.getSurname());
        user.setDateOfBirth(userForm.getDateOfBirth());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }
}

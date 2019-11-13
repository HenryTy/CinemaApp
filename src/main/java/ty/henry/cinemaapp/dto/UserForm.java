package ty.henry.cinemaapp.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ty.henry.cinemaapp.validation.PasswordMatches;

import javax.validation.constraints.*;
import java.time.LocalDate;

@PasswordMatches(message = "Błąd w powtórzeniu hasła", groups = {UserForm.UneditableDataValidation.class})
public class UserForm {

    public interface EditableDataValidation {

    }

    public interface UneditableDataValidation {

    }

    @Size(min = 2, max = 20, message = "Imię musi zawierać od 2 do 20 znaków", groups = {EditableDataValidation.class})
    private String name;

    @Size(min = 2, max = 30, message = "Nazwisko musi zawierać od 2 do 30 znaków", groups = {EditableDataValidation.class})
    private String surname;

    @NotBlank(message = "Email jest niepoprawny", groups = {UneditableDataValidation.class})
    @Email(message = "Email jest niepoprawny", groups = {UneditableDataValidation.class})
    private String email;

    @Size(min = 7, max = 30, message = "Hasło musi zawierać od 7 do 30 znaków",
            groups = {UneditableDataValidation.class})
    @Pattern(regexp = ".*[A-Z].*", message = "Hasło musi zawierać co najmniej jedną wielką literę",
            groups = {UneditableDataValidation.class})
    @Pattern(regexp = ".*[a-z].*", message = "Hasło musi zawierać co najmniej jedną małą literę",
            groups = {UneditableDataValidation.class})
    @Pattern(regexp = ".*[0-9].*", message = "Hasło musi zawierać co najmniej jedną cyfrę",
            groups = {UneditableDataValidation.class})
    private String password;
    private String matchingPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Podaj datę z przeszłości", groups = {EditableDataValidation.class})
    @NotNull(message = "Niepoprawna data", groups = {EditableDataValidation.class})
    private LocalDate dateOfBirth;

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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

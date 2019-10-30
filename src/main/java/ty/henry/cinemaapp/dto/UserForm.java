package ty.henry.cinemaapp.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ty.henry.cinemaapp.validation.PasswordMatches;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@PasswordMatches(message = "Błąd w powtórzeniu hasła")
public class UserForm {

    @Size(min = 2, max = 20, message = "Imię musi zawierać od 2 do 20 znaków")
    private String name;

    @Size(min = 2, max = 30, message = "Nazwisko musi zawierać od 2 do 30 znaków")
    private String surname;

    @NotBlank(message = "Email jest niepoprawny")
    @Email(message = "Email jest niepoprawny")
    private String email;

    @Size(min = 7, max = 30, message = "Hasło musi zawierać od 7 do 30 znaków")
    @Pattern(regexp = ".*[A-Z].*", message = "Hasło musi zawierać co najmniej jedną wielką literę")
    @Pattern(regexp = ".*[a-z].*", message = "Hasło musi zawierać co najmniej jedną małą literę")
    @Pattern(regexp = ".*[0-9].*", message = "Hasło musi zawierać co najmniej jedną cyfrę")
    private String password;
    private String matchingPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

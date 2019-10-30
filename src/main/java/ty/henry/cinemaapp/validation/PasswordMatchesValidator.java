package ty.henry.cinemaapp.validation;

import ty.henry.cinemaapp.dto.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserForm> {

    @Override
    public boolean isValid(UserForm userForm, ConstraintValidatorContext context) {
        return userForm.getPassword().equals(userForm.getMatchingPassword());
    }
}

package pl.sokn.annotation.validation;

import pl.sokn.dto.PasswordBase;
import pl.sokn.dto.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Do nothing because we validate whole object
    }

    /**
     *
     * @param obj - An object that is send during registration
     * @param context - we don't use functionality of this object
     * @return true if passwords matches, false otherwise
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof UserDTO) {
            final UserDTO user = (UserDTO) obj;
            if (user.getPassword() != null && user.getMatchingPassword() != null)
                return user.getPassword().equals(user.getMatchingPassword());
        }
        if (obj instanceof PasswordBase) {
            final PasswordBase password = (PasswordBase) obj;
            if (password.getPassword() != null && password.getMatchingPassword() != null)
                return password.getPassword().equals(password.getMatchingPassword());
        }

        return false;
    }
}
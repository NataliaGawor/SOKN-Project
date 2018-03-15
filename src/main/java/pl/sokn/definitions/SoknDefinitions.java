package pl.sokn.definitions;

public interface SoknDefinitions {

    class ApiMessages {

        public static final String REGISTRATION_TOKEN_SEND = "Email to {0} has sent.";
        public static final String RESET_TOKEN_SEND = "Reset password email send to {0}";
        public static final String EMAIL_CONFIRMED = "Email confirmed";
        public static final String P_UPDATED_SUCCESSFULLY = "Password updated successfully";
        public static final String TOKEN_IS_VALID = "Token is valid";

        private ApiMessages() {}
    }

    class ErrorMessages {
        public static final String USER_DOES_NOT_EXISTS = "User does not exists";
        public static final String USER_ALREADY_EXISTS = "User already exists";
        public static final String INVALID_DATE_FORMAT = "You have provided invalid date format";
        public static final String INVALID_TOKEN = "You have provided invalid token: ";
        public static final String TOKEN_EXPIRED = "Your token expired";
        public static final String INVALID_OLD_P = "Invalid old password";
        public static final String P_ARE_THE_SAME = "Your new and old password cannot be the same";

        private ErrorMessages() {}
    }

    class Roles {
        public static final String DEFAULT_ROLE = "USER";
        public static final String ADMIN_ROLE = "ADMIN";

        private Roles() {}
    }
}

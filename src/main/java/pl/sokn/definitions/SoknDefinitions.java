package pl.sokn.definitions;

/**
 * Static data that is used in multiple classes is kept here
 */
public interface SoknDefinitions {

    class Api {
        public static final String LOGIN = "/login";
        public static final String REFRESH = "/refresh";
        public static final String MAILING_LIST = "/mailingList";
        public static final String REVIEWER_PATH = "/reviewer";
        public static final String USERS_PATH = "/user";
        public static final String REGISTER = USERS_PATH + "/register";
        public static final String REGISTRATION_CONFIRM = USERS_PATH + "/registrationConfirm";
        public static final String RESEND_REGISTRATION_TOKEN = USERS_PATH + "/resendRegistrationToken";
        public static final String CHANGE_PASSWORD = USERS_PATH + "/changePassword";
        public static final String RESET_PASSWORD = USERS_PATH + "/resetPassword";
        public static final String FORGOT_PASSWORD = USERS_PATH + "/forgotPassword";
        public static final String RESEND_FORGOT_PASSWORD = USERS_PATH + "/resendPasswordToken";
        public static final String CHECK_IF_REGISTERED = USERS_PATH + "/checkIfRegistered";
        public static final String SEND_CONTACT_EMAIL = USERS_PATH + "/sendContactEmail";
        public static final String ARTICLE_PATH = "/article";
        public static final String FIELDS_OF_ARTICLES = "/fieldsOfArticles";
        public static final String GET_ALL_FIELDS_OF_ARTICLES = FIELDS_OF_ARTICLES + "/getAll";

        private Api() {
        }
    }

    class ApiMessages {

        public static final String REGISTRATION_TOKEN_SEND = "Email do {0} został wysłany.";
        public static final String RESET_TOKEN_SEND = "Kliknij w link wysłany na {0}";
        public static final String EMAIL_CONFIRMED = "Email został pomyślnie potwierdzony";
        public static final String P_UPDATED_SUCCESSFULLY = "Hasło zostało pomyślnie zaktualizowane";
        public static final String TOKEN_IS_VALID = "Token jest prawidłowy";

        private ApiMessages() {
        }
    }

    class ErrorMessages {
        public static final String USER_DOES_NOT_EXISTS = "Użytkownik nie istnieje";
        public static final String USER_ALREADY_EXISTS = "Użytkownik już istnieje w bazie danych";
        public static final String USER_ALREADY_HAS_AUTHORITY = "Użytkownik już posiada uprawnienia";
        public static final String INVALID_TOKEN = "Wysłałeś nieprawidłowy token: ";
        public static final String TOKEN_EXPIRED = "Ważność Twojego tokena wygasła";
        public static final String INVALID_OLD_P = "Nieprawidłowe stare hasło";
        public static final String P_ARE_THE_SAME = "Twoje nowe i stare hasła nie mogą być takie same";
        public static final String P_CHANGE_NOT_ALLOWED = "Nie masz uprawnień do zmiany hasła";
        public static final String EMAIL_ALREADY_SUBSCRIBED = "Nie możesz zapisać się na listę, ponieważ już na niej jesteś.";
        public static final String CANNOT_UNSUBSCRIBE = "Nie można usunąć tego adresu, ponieważ nie ma go na liście.";

        private ErrorMessages() {
        }
    }

    class Roles {
        public static final String DEFAULT_ROLE = "AUTHOR";
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String REVIEWER_ROLE = "REVIEWER";
        public static final String PASS_CHANGE_ROLE = "PASS_CHANGE";

        private Roles() {
        }
    }

    class AuthorizationHelper {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";

        private AuthorizationHelper() {
        }
    }
}

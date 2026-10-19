/**
 * Represents one user of the system: a username (an email address) and a password.
 *
 * A User object can only exist if BOTH the username and the password are valid.
 * The constructor checks them, and if something is wrong it throws an
 * IllegalArgumentException whose message says exactly what is wrong.
 *
 * Encapsulation: the fields are private and final, there are no setters,
 * so nobody outside this class can put invalid data into a User.
 */
public class User {

    // ---- Limits taken from the lab requirements ----
    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 12;

    /** Special characters that are allowed inside a password. */
    private static final String PASSWORD_SYMBOLS = "!@#$%^&*()_+";

    /**
     * Email pattern, built from the 3 parts described in the lab:
     *   part 1 (before @) : letters, digits and . _ % + -   (at least one char)
     *   part 2 (after @)  : starts with a letter or digit, then letters, digits, . or -
     *   part 3 (after the last '.') : at least 2 letters
     */
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9][A-Za-z0-9.-]*\\.[A-Za-z]{2,}$";

    // ---- Error messages (copied exactly from the lab's table) ----
    private static final String BAD_EMAIL = "Please enter a valid Email as username";
    private static final String EMAIL_TOO_LONG = "Username is too long, try something shorter";
    private static final String PASS_TOO_SHORT = "Your password is too short, add more characters";
    private static final String PASS_TOO_LONG = "Your password is too long, try a shorter one";
    private static final String BAD_PASSWORD = "Please enter a valid password";

    private final String username;
    private final String password;

    /**
     * Creates a new user only if the username and password are valid.
     *
     * @param username an email address, at most 50 characters
     * @param password 8-12 characters with at least one letter, one digit and one symbol
     * @throws IllegalArgumentException with a message describing the first problem found
     */
    public User(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        this.password = password;
    }

    /** Checks the username; throws an exception if it is not valid. */
    private static void validateUsername(String username) {
        if (username == null || !username.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException(BAD_EMAIL);
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException(EMAIL_TOO_LONG);
        }
    }

    /** Checks the password; throws an exception if it is not valid. */
    private static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(PASS_TOO_SHORT);
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(PASS_TOO_LONG);
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        // Go over every character: it must be a letter, a digit or an allowed symbol.
        for (char c : password.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                hasLetter = true;
            } else if (c >= '0' && c <= '9') {
                hasDigit = true;
            } else if (PASSWORD_SYMBOLS.indexOf(c) >= 0) {
                hasSymbol = true;
            } else {
                throw new IllegalArgumentException(BAD_PASSWORD); // illegal character
            }
        }

        // A valid password needs at least one of each kind.
        if (!hasLetter || !hasDigit || !hasSymbol) {
            throw new IllegalArgumentException(BAD_PASSWORD);
        }
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /** Format used when printing: "username password". */
    @Override
    public String toString() {
        return username + " " + password;
    }
}

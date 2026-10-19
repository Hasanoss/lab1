import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Reads users from a text file, keeps only the valid ones, and prints them sorted.
 *
 * Each line of the input is: username password (separated by one or more spaces).
 *
 * Output:
 *  - Valid users, sorted by username, are printed to System.out (standard output).
 *  - Invalid lines, with the reason, are printed to System.err (standard error).
 *    This way, when running "java UsersApp < users.txt > out.txt",
 *    out.txt contains only the sorted valid users, exactly like the expected output.
 *
 * Where the input comes from:
 *  1. a file name given as a command-line argument, or
 *  2. redirected standard input (java UsersApp < users.txt), or
 *  3. otherwise the file "users.txt" in the current folder.
 */
public class UsersApp {

    private static final String DEFAULT_FILE = "users.txt";

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner reader = openInput(args)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                handleLine(line, users);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + e.getMessage());
            return;
        }

        // Sort the valid users by username (normal String order).
        Collections.sort(users, (u1, u2) -> u1.getName().compareTo(u2.getName()));

        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * Turns one line into a User and adds it to the list.
     * If the line is not valid, prints the line and the reason to System.err.
     */
    private static void handleLine(String line, ArrayList<User> users) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return; // skip empty lines
        }

        // Split on one or more spaces/tabs.
        String[] parts = trimmed.split("\\s+");
        String username = parts[0];
        String password = (parts.length > 1) ? parts[1] : ""; // missing password -> too short

        try {
            if (parts.length > 2) {
                // Extra words on the line: a password cannot contain spaces.
                throw new IllegalArgumentException("Please enter a valid password");
            }
            users.add(new User(username, password)); // constructor validates the data
        } catch (IllegalArgumentException e) {
            System.err.println(line + "  -> " + e.getMessage());
        }
    }

    /** Chooses where to read from (see the class comment). */
    private static Scanner openInput(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            return new Scanner(new File(args[0]));
        }
        try {
            if (System.in.available() > 0) {
                return new Scanner(System.in);
            }
        } catch (IOException e) {
            // ignore and fall back to the default file
        }
        return new Scanner(new File(DEFAULT_FILE));
    }
}

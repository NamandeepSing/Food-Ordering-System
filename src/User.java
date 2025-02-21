import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String USER_FILE = "users.txt";

    // Authenticate user
    public static boolean authenticateUser(String username, String password) throws IOException {
        Map<String, String> users = loadUsersFromFile();
        return users.containsKey(username) && users.get(username).equals(password);
    }

    // Register new user
    public static boolean saveUser(String username, String password) throws IOException {
        Map<String, String> users = loadUsersFromFile();

        if (users.containsKey(username)) {
            return false; // User already exists
        }

        users.put(username, password);
        saveUsersToFile(users);
        return true;
    }

    // Load users from file
    private static Map<String, String> loadUsersFromFile() throws IOException {
        Map<String, String> users = new HashMap<>();
        File userFile = new File(USER_FILE);

        if (!userFile.exists()) {
            userFile.createNewFile();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        }
        return users;
    }

    // Save users to file
    private static void saveUsersToFile(Map<String, String> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        }
    }
}

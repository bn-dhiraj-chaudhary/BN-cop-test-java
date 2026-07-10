package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {

    // Intentional finding: hardcoded credentials (for COP/Polaris merge-key comparison testing)
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "SuperSecret123!";

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", DB_USER, DB_PASSWORD);

        Statement setup = conn.createStatement();
        setup.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))");
        setup.execute("INSERT INTO users VALUES (1, 'alice'), (2, 'bob')");

        String userInput = args.length > 0 ? args[0] : "1";
        findUser(conn, userInput);

        if (args.length > 1) {
            runSystemCommand(args[1]);
        }
    }

    // Intentional finding: SQL injection via string concatenation
    private static void findUser(Connection conn, String id) throws Exception {
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM users WHERE id = " + id;
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println("Found user: " + rs.getString("name"));
        }
    }

    // Intentional finding: OS command injection
    private static void runSystemCommand(String cmd) throws Exception {
        Runtime.getRuntime().exec(cmd);
    }
}

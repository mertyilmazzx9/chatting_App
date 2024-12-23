package technologies.proven.myilmaz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ChatApp";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mert_1331";

    // veritabanı bağlantı kodu
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

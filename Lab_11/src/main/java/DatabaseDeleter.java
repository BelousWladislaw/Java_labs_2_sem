import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDeleter {
    public static void main(String[] args) {
        // Получение подключения к базе данных
        String DB_URL = "jdbc:mysql://localhost:3306/";
        String DB_USER = "root";
        String DB_PASSWORD = "12345";
        String databaseName = "university";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Удаление базы данных
            String dropDatabase = "DROP DATABASE " + databaseName;
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropDatabase);

            System.out.println("Database " + databaseName + " deleted successfully.");

            // Закрытие ресурсов
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Закрытие подключения к базе данных
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Database db = new Database("jdbc:mysql://localhost/university?user=root&password=123456");

        String createTeachersTableQuery = "CREATE TABLE Teachers (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255)," +
                "subject VARCHAR(255)," +
                "weekly_hours INT," +
                "students_count INT" +
                ")";
        try {
            db.executeQuery(createTeachersTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createLessonsTableQuery = "CREATE TABLE Lessons (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "subject VARCHAR(255)," +
                "day_of_week VARCHAR(255)," +
                "classroom VARCHAR(255)" +
                ")";
        try {
            db.executeQuery(createLessonsTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
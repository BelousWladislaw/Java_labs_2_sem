import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
    private static final String DB_URL = "jdbc:mysql://localhost:5678/";
    private static final String DB_NAME = "university";
    private static final String DB_USER = "Vladislav";
    private static final String DB_PASSWORD = "1111";

    public static void main(String[] args) {
        try {
            // Установка соединения с базой данных
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();

            // Создание базы данных
            String createDBQuery = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDBQuery);

            // Использование базы данных
            String useDBQuery = "USE " + DB_NAME;
            statement.executeUpdate(useDBQuery);

            // Создание таблицы "Subjects"
            String createSubjectsTableQuery = "CREATE TABLE Subjects (" +
                    "subject_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "subject_name VARCHAR(100) NOT NULL," +
                    "day_of_week VARCHAR(20) NOT NULL," +
                    "classrooms VARCHAR(100) NOT NULL" +
                    ")";
            statement.executeUpdate(createSubjectsTableQuery);

            // Заполнение таблицы "Subjects" данными
            String insertSubjectsQuery = "INSERT INTO Subjects (subject_name, day_of_week, classrooms) VALUES " +
                    "('Mathematics', 'Monday', '101'), " +
                    "('Physics', 'Tuesday', '201'), " +
                    "('Computer Science', 'Wednesday', '301')";
            statement.executeUpdate(insertSubjectsQuery);

            // Создание таблицы "Teachers"
            String createTeachersTableQuery = "CREATE TABLE Teachers (" +
                    "teacher_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "subject_id INT," +
                    "weekly_lectures INT," +
                    "students_count INT," +
                    "FOREIGN KEY (subject_id) REFERENCES Subjects(subject_id)" +
                    ")";
            statement.executeUpdate(createTeachersTableQuery);

            // Заполнение таблицы "Teachers" данными
            String insertTeachersQuery = "INSERT INTO Teachers (full_name, subject_id, weekly_lectures, students_count) VALUES " +
                    "('Sokolov Sergei', 1, 5, 30), " +
                    "('Grishcenko Vitaly', 2, 4, 25), " +
                    "('Podalov Maxim', 3, 3, 20)";
            statement.executeUpdate(insertTeachersQuery);

            System.out.println("База данных успешно создана и заполнена данными.");

            // Закрытие соединения
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
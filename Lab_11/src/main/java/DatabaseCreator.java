import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "university";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";

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

            // Создание таблицы "Rooms"
            String createRoomsTableQuery = "CREATE TABLE Rooms (" +
                    "room_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "room_number VARCHAR(10) NOT NULL," +
                    "capacity INT NOT NULL" +
                    ")";
            statement.executeUpdate(createRoomsTableQuery);

            // Заполнение таблицы "Rooms" данными
            String insertRoomsQuery = "INSERT INTO Rooms (room_number, capacity) VALUES " +
                    "('101', 30), " +
                    "('201', 40), " +
                    "('301', 20)";
            statement.executeUpdate(insertRoomsQuery);

            // Создание таблицы "Lectures"
            String createLecturesTableQuery = "CREATE TABLE Lectures (" +
                    "lecture_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "teacher_id INT," +
                    "day_of_week VARCHAR(20) NOT NULL," +
                    "start_time TIME NOT NULL," +
                    "end_time TIME NOT NULL," +
                    "FOREIGN KEY (teacher_id) REFERENCES Teachers(teacher_id)" +
                    ")";
            statement.executeUpdate(createLecturesTableQuery);

            // Заполнение таблицы "Lectures" данными
            String insertLecturesQuery = "INSERT INTO Lectures (teacher_id, day_of_week, start_time, end_time) VALUES " +
                    "(1, 'Monday', '09:00:00', '10:30:00'), " +
                    "(2, 'Tuesday', '11:00:00', '12:30:00'), " +
                    "(3, 'Wednesday', '14:00:00', '15:30:00')";
            statement.executeUpdate(insertLecturesQuery);

            // Добавление столбца "room_id" в таблицу "Lectures"
            String addRoomIdColumnQuery = "ALTER TABLE Lectures ADD COLUMN room_id INT";
            statement.executeUpdate(addRoomIdColumnQuery);

            // Добавление внешнего ключа на столбец "room_id"
            String addForeignKeyQuery = "ALTER TABLE Lectures ADD FOREIGN KEY (room_id) REFERENCES Rooms(room_id)";
            statement.executeUpdate(addForeignKeyQuery);

            System.out.println("База данных успешно создана и заполнена данными.");

            // Закрытие соединения
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zapros1 {
    private Connection connection;

    public Zapros1(Connection connection) {
        this.connection = connection;
    }

    public List<String> execute(String dayOfWeek, String roomNumber) {
        List<String> teacherNames = new ArrayList<>();

        String query = "SELECT Teachers.full_name " +
                "FROM Teachers " +
                "JOIN Lectures ON Teachers.teacher_id = Lectures.teacher_id " +
                "JOIN Rooms ON Lectures.room_id = Rooms.room_id " +
                "WHERE DAYOFWEEK(Lectures.day_of_week) = DAYOFWEEK(STR_TO_DATE(?, '%W')) " +
                "AND Rooms.room_number = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dayOfWeek);
            statement.setString(2, roomNumber);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String teacherName = resultSet.getString("full_name");
                teacherNames.add(teacherName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacherNames;
    }

    public static void main(String[] args) {
        // Получение соединения с базой данных
        Connection connection = getConnection();

        // Создание экземпляра класса TeachersByDayAndRoomQueryRunner
        Zapros1 queryRunner = new Zapros1(connection);

        // Создание объекта Scanner для считывания ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // Ввод значения дня недели
        System.out.print("Введите день недели: ");
        String dayOfWeek = scanner.nextLine();

        // Ввод значения аудитории
        System.out.print("Введите номер аудитории: ");
        String roomNumber = scanner.nextLine();

        // Выполнение запроса и получение списка преподавателей
        List<String> teachers = queryRunner.execute(dayOfWeek, roomNumber);

        // Использование результата запроса
        if (teachers.isEmpty()) {
            System.out.println("Нет преподавателей, работающих в указанный день и аудитории.");
        } else {
            System.out.println("Преподаватели, работающие в указанный день и аудитории:");
            for (String teacher : teachers) {
                System.out.println(teacher);
            }
        }

        // Закрытие соединения с базой данных
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        // Конфигурация подключения к базе данных
        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "12345";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
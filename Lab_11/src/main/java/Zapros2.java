import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zapros2 {
    private Connection connection;

    public Zapros2(Connection connection) {
        this.connection = connection;
    }

    public List<String> execute(String dayOfWeek) {
        List<String> teacherNames = new ArrayList<>();

        String query = "SELECT full_name " +
                "FROM Teachers " +
                "WHERE teacher_id NOT IN (" +
                "  SELECT DISTINCT Lectures.teacher_id " +
                "  FROM Lectures " +
                "  WHERE DAYOFWEEK(day_of_week) = DAYOFWEEK(STR_TO_DATE(?, '%W'))" +
                ")";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dayOfWeek);

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

        // Создание экземпляра класса TeachersNotAvailableOnDayQueryRunner
        Zapros2 queryRunner = new Zapros2(connection);

        // Создание объекта Scanner для считывания ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // Ввод значения дня недели
        System.out.print("Введите день недели: ");
        String dayOfWeek = scanner.nextLine();

        // Выполнение запроса и получение списка преподавателей
        List<String> teachers = queryRunner.execute(dayOfWeek);

        // Использование результата запроса
        if (teachers.isEmpty()) {
            System.out.println("Все преподаватели ведут занятия в указанный день недели.");
        } else {
            System.out.println("Преподаватели, которые не ведут занятия в указанный день недели:");
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
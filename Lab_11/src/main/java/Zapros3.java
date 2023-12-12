import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zapros3 {
    private Connection connection;

    public Zapros3(Connection connection) {
        this.connection = connection;
    }

    public List<String> execute(int numLectures) {
        List<String> daysOfWeek = new ArrayList<>();

        String query = "SELECT day_of_week " +
                "FROM Lectures " +
                "GROUP BY day_of_week " +
                "HAVING COUNT(*) = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, numLectures);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("day_of_week");
                daysOfWeek.add(dayOfWeek);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daysOfWeek;
    }

    public static void main(String[] args) {
        // Получение соединения с базой данных
        Connection connection = getConnection();

        // Создание экземпляра класса DaysWithNumLecturesQueryRunner
        Zapros3 queryRunner = new Zapros3(connection);

        // Создание объекта Scanner для считывания ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // Ввод значения количества занятий
        System.out.print("Введите количество занятий: ");
        int numLectures = scanner.nextInt();

        // Очистка буфера чтения
        scanner.nextLine();

        // Выполнение запроса и получение списка дней недели
        List<String> daysOfWeek = queryRunner.execute(numLectures);

        // Использование результата запроса
        if (daysOfWeek.isEmpty()) {
            System.out.println("Нет дней недели, в которых проводится указанное количество занятий.");
        } else {
            System.out.println("Дни недели, в которых проводится " + numLectures + " занятий:");
            for (String dayOfWeek : daysOfWeek) {
                System.out.println(dayOfWeek);
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
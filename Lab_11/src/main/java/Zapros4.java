import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zapros4 {
    private Connection connection;

    public Zapros4(Connection connection) {
        this.connection = connection;
    }

    public List<String> execute(int numRooms) {
        List<String> daysOfWeek = new ArrayList<>();

        String query;
        if (numRooms > 0) {
            query = "SELECT day_of_week " +
                    "FROM Lectures " +
                    "GROUP BY day_of_week " +
                    "HAVING COUNT(DISTINCT lecture_id) = ?";
        } else {
            query = "SELECT DISTINCT day_of_week " +
                    "FROM Lectures " +
                    "WHERE day_of_week NOT IN (SELECT DISTINCT day_of_week FROM Lectures)";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (numRooms > 0) {
                statement.setInt(1, numRooms);
            }

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

        // Создание экземпляра класса DaysWithNumRoomsQueryRunner
        Zapros4 queryRunner = new Zapros4(connection);

        // Создание объекта Scanner для считывания ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // Ввод значения количества аудиторий
        System.out.print("Введите количество аудиторий: ");
        int numRooms = scanner.nextInt();

        // Очистка буфера чтения
        scanner.nextLine();

        // Выполнение запроса и получение списка дней недели
        List<String> daysOfWeek = queryRunner.execute(numRooms);

        // Использование результата запроса
        if (daysOfWeek.isEmpty()) {
            if (numRooms > 0) {
                System.out.println("Нет дней недели, в которых занято " + numRooms + " аудиторий.");
            } else {
                System.out.println("Недоработочка... Прошу прощения))");
            }
        } else {
            System.out.println("Дни недели, в которых занято " + numRooms + " аудиторий:");
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
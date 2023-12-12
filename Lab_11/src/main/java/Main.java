import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Получение подключения к базе данных
        String DB_URL = "jdbc:mysql://localhost:3306/university";
        String DB_USER = "root";
        String DB_PASSWORD = "12345";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Выполнение запросов к базе данных
            if (connection != null) {
                try {
                    // Вывести информацию о преподавателях, работающих в заданный день недели в заданной аудитории
                    String query1 = "SELECT Teachers.teacher_id, Teachers.full_name, Teachers.subject_id " +
                            "FROM Teachers " +
                            "JOIN Lectures ON Teachers.teacher_id = Lectures.teacher_id " +
                            "JOIN Rooms ON Lectures.room_id = Rooms.room_id " +
                            "WHERE Lectures.day_of_week = ? " +
                            "AND Rooms.room_number = ?";
                    PreparedStatement statement1 = connection.prepareStatement(query1);
                    statement1.setString(1, "Понедельник"); // Замените на нужный день недели
                    statement1.setString(2, "Аудитория 101"); // Замените на нужную аудиторию
                    ResultSet resultSet1 = statement1.executeQuery();

                    while (resultSet1.next()) {
                        int teacherId = resultSet1.getInt("teacher_id");
                        String fullName = resultSet1.getString("full_name");
                        int subjectId = resultSet1.getInt("subject_id");

                        System.out.println("Teacher ID: " + teacherId);
                        System.out.println("Full Name: " + fullName);
                        System.out.println("Subject ID: " + subjectId);
                        System.out.println();
                    }

                    // Вывести информацию о преподавателях, которые не ведут занятия в заданный день недели
                    String query2 = "SELECT Teachers.teacher_id, Teachers.full_name, Teachers.subject_id " +
                            "FROM Teachers " +
                            "WHERE Teachers.teacher_id NOT IN (" +
                            "SELECT Lectures.teacher_id " +
                            "FROM Lectures " +
                            "WHERE Lectures.day_of_week = ?)";
                    PreparedStatement statement2 = connection.prepareStatement(query2);
                    statement2.setString(1, "Пятница"); // Замените на нужный день недели
                    ResultSet resultSet2 = statement2.executeQuery();

                    while (resultSet2.next()) {
                        int teacherId = resultSet2.getInt("teacher_id");
                        String fullName = resultSet2.getString("full_name");
                        int subjectId = resultSet2.getInt("subject_id");

                        System.out.println("Teacher ID: " + teacherId);
                        System.out.println("Full Name: " + fullName);
                        System.out.println("Subject ID: " + subjectId);
                        System.out.println();
                    }

                    // Вывести дни недели, в которых проводится заданное количество занятий
                    String query3 = "SELECT day_of_week " +
                            "FROM Lectures " +
                            "GROUP BY day_of_week " +
                            "HAVING COUNT(*) = ?";
                    PreparedStatement statement3 = connection.prepareStatement(query3);
                    statement3.setInt(1, 3); // Замените на нужное количество занятий
                    ResultSet resultSet3 = statement3.executeQuery();

                    while (resultSet3.next()) {
                        String dayOfWeek = resultSet3.getString("day_of_week");

                        System.out.println("Day of Week: " + dayOfWeek);
                        System.out.println();
                    }

                    // Вывести дни недели, в которых занято заданное количество аудиторий
                    String query4 = "SELECT day_of_week " +
                            "FROM Lectures " +
                            "GROUP BY day_of_week " +
                            "HAVING COUNT(DISTINCT room_id) = ?";
                    PreparedStatement statement4 = connection.prepareStatement(query4);
                    statement4.setInt(1, 5); // Замените на нужное количество аудиторий
                    ResultSet resultSet4 = statement4.executeQuery();

                    while (resultSet4.next()) {
                        String dayOfWeek = resultSet4.getString("day_of_week");

                        System.out.println("Day of Week: " + dayOfWeek);
                        System.out.println();
                    }

                    // Закрытие ресурсов
                    resultSet1.close();
                    statement1.close();
                    resultSet2.close();
                    statement2.close();
                    resultSet3.close();
                    statement3.close();
                    resultSet4.close();
                    statement4.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
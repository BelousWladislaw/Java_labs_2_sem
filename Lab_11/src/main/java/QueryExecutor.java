import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecutor {
    private Database db;

    public QueryExecutor(Database db) {
        this.db = db;
    }

    public ResultSet getTeachersByDayAndClassroom(String dayOfWeek, String classroom) throws SQLException {
        String query = "SELECT * FROM Teachers WHERE id IN (SELECT id FROM Lessons WHERE day_of_week = ? AND classroom = ?)";
        return db.executeSelectQuery(query, dayOfWeek, classroom);
    }

    public ResultSet getTeachersWithoutLessonsOnDay(String dayOfWeek) throws SQLException {
        String query = "SELECT * FROM Teachers WHERE id NOT IN (SELECT id FROM Lessons WHERE day_of_week = ?)";
        return db.executeSelectQuery(query, dayOfWeek);
    }

    public ResultSet getDaysWithLessonsCount(int count) throws SQLException {
        String query = "SELECT day_of_week FROM Lessons GROUP BY day_of_week HAVING COUNT(*) = ?";
        return db.executeSelectQuery(query, count);
    }

    public ResultSet getDaysWithClassroomsCount(int count) throws SQLException {
        String query = "SELECT day_of_week FROM Lessons GROUP BY day_of_week HAVING COUNT(DISTINCT classroom) = ?";
        return db.executeSelectQuery(query, count);
    }

    public void moveFirstLessonsToLast(String dayOfWeek) throws SQLException {
        String query = "UPDATE Lessons SET classroom = 'new_classroom' WHERE day_of_week = ? ORDER BY id ASC LIMIT 1";
        db.executeQuery(query, dayOfWeek);
    }
}
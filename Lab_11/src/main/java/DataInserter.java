import java.sql.SQLException;

public class DataInserter {
    private Database db;

    public DataInserter(Database db) {
        this.db = db;
    }

    public void insertTeacher(String name, String subject, int weeklyHours, int studentsCount) throws SQLException {
        String query = "INSERT INTO Teachers (name, subject, weekly_hours, students_count) VALUES (?, ?, ?, ?)";
        db.executeQuery(query, name, subject, weeklyHours, studentsCount);
    }

    public void insertLesson(String subject, String dayOfWeek, String classroom) throws SQLException {
        String query = "INSERT INTO Lessons (subject, day_of_week, classroom) VALUES (?, ?, ?)";
        db.executeQuery(query, subject, dayOfWeek, classroom);
    }
}

import java.sql.ResultSet;
import java.sql.SQLException;

public class HtmlForm {
    private QueryExecutor queryExecutor;

    public HtmlForm(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public ResultSet getTeachersByDayAndClassroom(String dayOfWeek, String classroom) throws SQLException {
        return queryExecutor.getTeachersByDayAndClassroom(dayOfWeek, classroom);
    }

    public ResultSet getTeachersWithoutLessonsOnDay(String dayOfWeek) throws SQLException {
        return queryExecutor.getTeachersWithoutLessonsOnDay(dayOfWeek);
    }

    public ResultSet getDaysWithLessonsCount(int count) throws SQLException {
        return queryExecutor.getDaysWithLessonsCount(count);
    }

    public ResultSet getDaysWithClassroomsCount(int count) throws SQLException {
        return queryExecutor.getDaysWithClassroomsCount(count);
    }

    public void moveFirstLessonsToLast(String dayOfWeek) throws SQLException {
        queryExecutor.moveFirstLessonsToLast(dayOfWeek);
    }
}
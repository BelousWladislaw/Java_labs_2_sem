public class HtmlFormCreator {
    public static void main(String[] args) {
        // Создаем экземпляр класса Database с указанием URL базы данных
        Database db = new Database("jdbc:mysql://localhost/university?user=Vladislav&password=1111");

        // Создаем экземпляр класса QueryExecutor с передачей объекта Database
        QueryExecutor queryExecutor = new QueryExecutor(db);

        // Создаем экземпляр класса HtmlForm с передачей объекта QueryExecutor
        HtmlForm htmlForm = new HtmlForm(queryExecutor);

        // Генерируем HTML-форму для вывода информации о преподавателях в заданный день недели и аудитории
        String teachersByDayAndClassroomForm = generateTeachersByDayAndClassroomForm();
        System.out.println(teachersByDayAndClassroomForm);

        // Генерируем HTML-форму для вывода информации о преподавателях, не ведущих занятия в заданный день недели
        String teachersWithoutLessonsOnDayForm = generateTeachersWithoutLessonsOnDayForm();
        System.out.println(teachersWithoutLessonsOnDayForm);

        // Генерируем HTML-форму для вывода дней недели с заданным количеством занятий
        String daysWithLessonsCountForm = generateDaysWithLessonsCountForm();
        System.out.println(daysWithLessonsCountForm);

        // Генерируем HTML-форму для вывода дней недели с заданным количеством занятых аудиторий
        String daysWithClassroomsCountForm = generateDaysWithClassroomsCountForm();
        System.out.println(daysWithClassroomsCountForm);

        // Генерируем HTML-форму для переноса первых занятий заданных дней недели на последнее место
        String moveFirstLessonsToLastForm = generateMoveFirstLessonsToLastForm();
        System.out.println(moveFirstLessonsToLastForm);
    }

    private static String generateTeachersByDayAndClassroomForm() {
        // Генерация HTML-кода для формы вывода информации о преподавателях по дню недели и аудитории
        // ...
        return "<form>...</form>";
    }

    private static String generateTeachersWithoutLessonsOnDayForm() {
        // Генерация HTML-кода для формы вывода информации о преподавателях без занятий в заданный день недели
        // ...
        return "<form>...</form>";
    }

    private static String generateDaysWithLessonsCountForm() {
        // Генерация HTML-кода для формы вывода дней недели с заданным количеством занятий
        // ...
        return "<form>...</form>";
    }

    private static String generateDaysWithClassroomsCountForm() {
        // Генерация HTML-кода для формы вывода дней недели с заданным количеством занятых аудиторий
        // ...
        return "<form>...</form>";
    }

    private static String generateMoveFirstLessonsToLastForm() {
        // Генерация HTML-кода для формы переноса первых занятий заданных дней недели на последнее место
        // ...
        return "<form>...</form>";
    }
}
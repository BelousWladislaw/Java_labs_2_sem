import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {
    private String dbUrl;

    public Database(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    public void executeQuery(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            setParameters(statement, params);
            statement.executeUpdate();
        }
    }

    public ResultSet executeSelectQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeQuery();
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}


package carsharing.jdbc;
import java.io.Closeable;
import java.sql.*;

public class H2JDBCConnection implements Closeable {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:mem:{path to desired directory for db}";

    private final Connection conn;

    public H2JDBCConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String statement, Object... args) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(statement);
            for(int i = 1; i <= args.length; i++) {
                ps.setObject(i, args[i - 1]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String statement, Object... args) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(statement);
            for(int i = 1; i <= args.length; i++) {
                ps.setObject(i, args[i - 1]);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

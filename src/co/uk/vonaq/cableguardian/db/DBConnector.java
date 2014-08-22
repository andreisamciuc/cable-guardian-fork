package co.uk.vonaq.cableguardian.db;

import com.google.inject.ImplementedBy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@ImplementedBy(DBConnectorMysql.class)
public interface DBConnector {

    public ResultSet executeQuery(String query) throws SQLException;

    public int executeUpdate(String query, String... values) throws SQLException;

    public void close(AutoCloseable ob) throws Exception;

    public Connection getConnection();
}

package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.Inject.Log;
import com.google.inject.name.Named;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import javax.inject.Inject;

public class DBConnectorMysql implements DBConnector {

    @Log
    private Logger logger;
    private Connection conn = null;
    private Statement stmt0;
    private Statement stmt1;

    @Inject
    public DBConnectorMysql(@Named("JDBC_URL") String url, @Named("JDBC_CRED") String creds) throws SQLException {
        conn = DriverManager.getConnection(url + creds);
        stmt0 = conn.createStatement();
        stmt1 = conn.createStatement();
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        return stmt0.executeQuery(query);
    }

    @Override
    public int executeUpdate(String query, String... values) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        for (String s: values){
            stmt.setString( ++i, s );
        }
        stmt.executeUpdate();
        
        int newId = -1;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            newId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return newId;
    }
    
    @Override
    public void close(AutoCloseable ob) throws Exception {
        // it is a good idea to release resources in a finally{} block in reverse-order 
        // of their creation if they are no-longer needed
        if (ob != null) {
            try {
                ob.close();
            } catch (SQLException sqlEx) {
            }
        }
    }

    @Override
    public Connection getConnection() {
        return conn;
    }
}

package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class SMSTable {

    @Inject
    private DBConnector dBConnector;

    public int sendMessage(String number, String text) throws SQLException {
        String query = "insert into cable_guardian.outbox (number,text) values(?,?)";
        return dBConnector.executeUpdate(query, number, text);
    }

    public ResultSet getMessages() throws SQLException {
        String query = "select * from cable_guardian.inbox ";
        return dBConnector.executeQuery(query);
    }

    public void deleteMessage(int id) throws SQLException {
        String query = "delete from cable_guardian.inbox where id = " + id;
        dBConnector.executeUpdate(query);
    }

    public ResultSet getProcessedOutboxMessages() throws SQLException {
        String query = "select id,number,insertdate,text,error from cable_guardian.outbox where processed = '1'";
        return dBConnector.executeQuery(query);
    }

    public void deleteOutboxMessage(int id) throws SQLException {
        String query = "delete from cable_guardian.outbox where id = " + id;
        dBConnector.executeUpdate(query);
    }
}

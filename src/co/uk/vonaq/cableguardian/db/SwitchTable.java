package co.uk.vonaq.cableguardian.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SwitchTable {
    @Inject
    TestsTable testsTable;
    @Inject
    private DBConnector dBConnector;

    public Set<Integer> getNodesForCable(int cable) throws SQLException {
        Set<Integer> res = new HashSet<>();
        //String query = "select cable_number,tolerance,alarm_enabled from cable_guardian.cables where test_enabled = \"Enabled\";";
        String query = "select switch_id,distance,description from cable_guardian.switches where cable_number = "+cable;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                res.add(rs.getInt(1));
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }

        return res;
    }
    
    //should return which node_in is the closest to the foult location.
    public int getNodeForCableByDistance(int cable, int distance) throws SQLException {
        int res = Integer.MAX_VALUE;
        int numberOfClosest = 0;
        //String query = "select cable_number,tolerance,alarm_enabled from cable_guardian.cables where test_enabled = \"Enabled\";";
        String query = "select switch_id,distance from cable_guardian.switches where cable_number = "+cable;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                int switchDistance = rs.getInt(2);
                if (Math.abs(switchDistance-distance)<Math.abs(distance-res)){
                    res = switchDistance;
                    numberOfClosest = rs.getInt(1);
                }
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }

        return numberOfClosest;
    }

    public void deleteAllNodesForCable(int cable_number) throws SQLException {
        String query = "Delete From cable_guardian.switches where cable_number = "+cable_number;
        dBConnector.executeUpdate(query);
    }

    public void addNode(int cable_number,  String description) throws SQLException {
        int length = testsTable.getCableAverage(cable_number);
        String query = "INSERT INTO cable_guardian.switches (cable_number, distance, description) VALUES (?,?,?)";
        dBConnector.executeUpdate(query,""+cable_number,""+length,description);
    }

    public String getNodeDescription(int switchNumber) throws SQLException {
        String query = "select description from switches where switch_id =  " + switchNumber;
        ResultSet rs = null;
        String switchDistance = null;
        try {
            rs = dBConnector.executeQuery(query);
            if (rs.next()) {
                switchDistance = rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return switchDistance;
    }
    
    public boolean doesCableHaveNodes(int cableNumber) throws SQLException{
        Set<Integer> nodes = getNodesForCable(cableNumber);
        return !nodes.isEmpty();
    }
}

package lk.ijse.asms.dao.custom;

import java.sql.SQLException;

public interface TeamDAO {
    boolean saveTeam(String id,String jobId) throws SQLException, ClassNotFoundException;
    String getNextTeamId() throws SQLException, ClassNotFoundException;

}

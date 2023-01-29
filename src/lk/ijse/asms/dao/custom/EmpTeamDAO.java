package lk.ijse.asms.dao.custom;

import lk.ijse.asms.dto.EmpTeamDTO;
import java.sql.SQLException;

public interface EmpTeamDAO {
    boolean saveEmp_Team(EmpTeamDTO empTeam) throws SQLException, ClassNotFoundException;
}

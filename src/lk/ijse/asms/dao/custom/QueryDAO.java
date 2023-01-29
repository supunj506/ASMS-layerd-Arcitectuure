package lk.ijse.asms.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.asms.view.tm.TeamTM;
import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO {
    ObservableList<TeamTM> getEmployeeDetails(String contract_base) throws SQLException, ClassNotFoundException;
    ArrayList<String> getDetailForSubcPayment(String id) throws SQLException, ClassNotFoundException;
    ArrayList<String> getContractBaseFinishJob() throws SQLException, ClassNotFoundException;
    ObservableList<String> getFinishJob(String status) throws SQLException, ClassNotFoundException;
    ArrayList<String> getJobDetails(String status) throws SQLException, ClassNotFoundException;

}

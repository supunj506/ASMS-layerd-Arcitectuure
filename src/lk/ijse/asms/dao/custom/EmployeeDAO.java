package lk.ijse.asms.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.asms.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeDAO {
    String getNextEmployeeId() throws SQLException, ClassNotFoundException;
    boolean save(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;
    boolean update(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;
    boolean delete(String text) throws SQLException, ClassNotFoundException;
    ArrayList<EmployeeDTO> getAllEmployee() throws SQLException, ClassNotFoundException;
    EmployeeDTO getEmployee(String nic) throws SQLException, ClassNotFoundException;

}

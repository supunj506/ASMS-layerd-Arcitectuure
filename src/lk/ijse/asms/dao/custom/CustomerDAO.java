package lk.ijse.asms.dao.custom;
import lk.ijse.asms.dto.CustomerDTO;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO {
    String getNextCustomerId() throws SQLException, ClassNotFoundException;
    boolean save(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;
    boolean update(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;
    ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException;
    CustomerDTO getCustomerById(String id) throws SQLException, ClassNotFoundException;
    CustomerDTO getCustomerByName(String name)throws SQLException, ClassNotFoundException;

}

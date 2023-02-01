package lk.ijse.asms.dao.custom;
import lk.ijse.asms.dao.CrudDAO;
import lk.ijse.asms.dto.CustomerDTO;
import lk.ijse.asms.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer,String> {
    CustomerDTO getCustomerById(String id) throws SQLException, ClassNotFoundException;
    CustomerDTO getCustomerByName(String name)throws SQLException, ClassNotFoundException;

}

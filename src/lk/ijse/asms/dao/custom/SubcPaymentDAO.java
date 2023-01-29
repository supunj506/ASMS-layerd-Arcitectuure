package lk.ijse.asms.dao.custom;

import lk.ijse.asms.dto.SubPaymentDTO;
import java.sql.SQLException;

public interface SubcPaymentDAO {
    boolean saveSubCPayment(SubPaymentDTO subPaymentDTO) throws SQLException, ClassNotFoundException;
    String getNextId () throws SQLException, ClassNotFoundException;
    SubPaymentDTO getSubPaymentById(String s) throws SQLException, ClassNotFoundException;
}

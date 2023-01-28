package lk.ijse.asms.dao.custom;

import lk.ijse.asms.dto.SubPaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SubcPaymentDAO {
    boolean saveSubCPayment(SubPaymentDTO subPaymentDTO) throws SQLException, ClassNotFoundException;
    String getNextId () throws SQLException, ClassNotFoundException;
    ArrayList<Integer> getPointCount(String id) throws SQLException, ClassNotFoundException;
}

package lk.ijse.asms.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.asms.dto.PaymentPlaneDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentPlaneDAO {
    ArrayList<Integer> getPointPrice() throws SQLException, ClassNotFoundException;
    ArrayList<String> getPointDescription(String point) throws SQLException, ClassNotFoundException;
    boolean updatePPPlane (PaymentPlaneDTO paymentPlaneDTO) throws SQLException, ClassNotFoundException;
    ArrayList<PaymentPlaneDTO> getAllPoint() throws SQLException, ClassNotFoundException;
}

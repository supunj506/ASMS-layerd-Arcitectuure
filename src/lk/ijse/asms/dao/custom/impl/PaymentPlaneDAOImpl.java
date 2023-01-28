package lk.ijse.asms.dao.custom.impl;

import lk.ijse.asms.dao.util.SQLUtil;
import lk.ijse.asms.dao.custom.PaymentPlaneDAO;
import lk.ijse.asms.dto.PaymentPlaneDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentPlaneDAOImpl implements PaymentPlaneDAO {
    //////////////////////////////security na array list ekakin ganna nisa//////////////////////////////////////////////////
    @Override
    public ArrayList<Integer> getPointPrice() throws SQLException, ClassNotFoundException {
        ResultSet rst= SQLUtil.execute("select unite_price from pp_plane");
        ArrayList<Integer>list=new ArrayList<>();
        while(rst.next()){
            list.add(rst.getInt(1));

        }
        return list;
    }

    //////////////////////////////////////aulak naaa/////////////////////////////////////////////////////////
    @Override
    public ArrayList<String> getPointDescription(String point) throws SQLException, ClassNotFoundException {
        ResultSet rst= SQLUtil.execute("select description,unite_price from pp_plane where name=?",point);
        ArrayList<String> desc=new ArrayList<>();
        while (rst.next()){
            desc.add(rst.getString(1));
            desc.add(rst.getString(2));

        }
        return desc;
    }
    @Override
    public boolean updatePPPlane(PaymentPlaneDTO paymentPlaneDTO) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("Update pp_plane set description=?,unite_price=? where name=?",
                paymentPlaneDTO.getDescription(),
                paymentPlaneDTO.getUnitPrice(),
                paymentPlaneDTO.getName());
    }
    @Override
    public ArrayList<PaymentPlaneDTO> getAllPoint() throws SQLException, ClassNotFoundException {
        ResultSet rst= SQLUtil.execute("select * from pp_plane");
        ArrayList<PaymentPlaneDTO> plane=new ArrayList<>();
        while (rst.next()){
            plane.add(new PaymentPlaneDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Double.parseDouble(rst.getString(4))));
        }
        return plane;
    }

}

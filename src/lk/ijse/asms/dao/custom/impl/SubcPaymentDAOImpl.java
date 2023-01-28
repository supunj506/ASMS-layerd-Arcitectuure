package lk.ijse.asms.dao.custom.impl;
import lk.ijse.asms.dao.custom.SubcPaymentDAO;
import lk.ijse.asms.dto.SubPaymentDTO;
import lk.ijse.asms.dao.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubcPaymentDAOImpl implements SubcPaymentDAO {
    @Override
    public boolean saveSubCPayment(SubPaymentDTO subPaymentDTO) throws SQLException, ClassNotFoundException {
       return SQLUtil.execute("insert into subc_payment (id,emp_id,job_id,data_point,power_point,camera_point,total_amount,pay_status) values (?,?,?,?,?,?,?,?)",
                subPaymentDTO.getId(),
                subPaymentDTO.getEmp_id(),
                subPaymentDTO.getJob_id(),
                subPaymentDTO.getData_point(),
                subPaymentDTO.getPower_point(),
                subPaymentDTO.getCamera_point(),
                subPaymentDTO.getTotal_amount(),
                subPaymentDTO.getPay_status());

    }
    @Override
        public String getNextId () throws SQLException, ClassNotFoundException {
            ResultSet rst = SQLUtil.execute("SELECT id FROM subc_payment ORDER BY id DESC LIMIT 1");
            if (rst.next()) {
                String payId=(rst.getString(1));
                String []ids= payId.split("Sub");
                int id=Integer.parseInt(ids[1]);
                id+=1;
                if(id>9){
                    return "Sub"+id;
                }
                return "Sub0"+id;

            }
            return "Sub01";
        }

    @Override
    public ArrayList<Integer> getPointCount(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst= SQLUtil.execute("select data_point,power_point,camera_point from subc_payment where id=?",id);
        ArrayList<Integer>point=new ArrayList<>();
        while (rst.next()){
            point.add(rst.getInt(1));
            point.add(rst.getInt(2));
            point.add(rst.getInt(3));
        }
        return  point;
    }

}

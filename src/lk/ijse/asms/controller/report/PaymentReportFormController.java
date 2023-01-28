package lk.ijse.asms.controller.report;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.asms.dao.custom.*;
import lk.ijse.asms.dao.custom.impl.*;
import lk.ijse.asms.dto.EmployeeDTO;
import lk.ijse.asms.dto.JobDTO;
import lk.ijse.asms.util.Navigation;
import lk.ijse.asms.util.Routes;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PaymentReportFormController {

    EmployeeDAO employeeDAO=new EmployeeDAOImpl();
    SubcPaymentDAO paymentDAO=new SubcPaymentDAOImpl();
    JobDAO jobDAO=new JobDAOImpl();
    QueryDAO queryDAO=new QueryDAOImpl();
    PaymentPlaneDAO paymentPlaneDAO=new PaymentPlaneDAOImpl();

    public AnchorPane contractPaymentPane;
    public JFXComboBox <String>cmbJob;
    public JFXButton btnGetPaySlip;

    public  void initialize(){
        btnGetPaySlip.setDisable(true);
        loadAllJob();
    }



    private void loadAllJob() {
        try {

            ArrayList<String> arrayList= queryDAO.getContractBaseFinishJob();
            ObservableList<String> obList= FXCollections.observableArrayList();
            for(String item:arrayList){
                obList.addAll(item);
            }
            cmbJob.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void cmbJobOnAction(ActionEvent actionEvent) {
        btnGetPaySlip.setDisable(false);
    }

    public void salaryPaymentFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.SALARY_PAYMENT,contractPaymentPane);
    }

    public void contractPaymentOnAction(ActionEvent actionEvent) {
        String[]temp=String.valueOf(cmbJob.getValue()).split(" / ");
        try {
            EmployeeDTO employeeDTO = employeeDAO.getEmployee(temp[2]);
            HashMap paramMap=new HashMap();
            paramMap.put("Eid", employeeDTO.getId());
            paramMap.put("Enic", employeeDTO.getNic());
            paramMap.put("Ename", employeeDTO.getName());
            paramMap.put("Eaddress", employeeDTO.getAddress());
            System.out.println(temp[1]);
            JobDTO abc = jobDAO.getJobById(temp[1]);

            Date startDate = java.sql.Date.valueOf(abc.getStartDate());
            Date endDate = java.sql.Date.valueOf(abc.getEndDate());

            paramMap.put("Jid",temp[1]);
            paramMap.put("Jstart",startDate );
            paramMap.put("Jend",endDate );

            ArrayList<Integer>pointCount= paymentDAO.getPointCount(temp[0]);
            paramMap.put("dataC",pointCount.get(0));
            paramMap.put("powerC",pointCount.get(1));
            paramMap.put("cameraC",pointCount.get(2));
            ArrayList<Integer> pointPrice = paymentPlaneDAO.getPointPrice();
            paramMap.put("dataV",Double.parseDouble(String.valueOf(pointPrice.get(0))));
            paramMap.put("powerV",Double.parseDouble(String.valueOf(pointPrice.get(1))));
            paramMap.put("cameraV",Double.parseDouble(String.valueOf(pointPrice.get(2))));

            double dataT=pointPrice.get(0)*pointCount.get(0);
            double powerT=pointPrice.get(1)*pointCount.get(1);
            double cameraT=pointPrice.get(2)*pointCount.get(2);

            paramMap.put("dataT",dataT);
            paramMap.put("powerT",powerT);
            paramMap.put("cameraT",cameraT);

            double total=dataT+powerT+cameraT;
            paramMap.put("total",total);

//            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/lk/ijse/asms/view/report/HellowJasper.jrxml"));
//            JasperReport jasperReport = JasperCompileManager.compileReport(load);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource(1));
//            JasperViewer.viewReport(jasperPrint,false);

            JasperReport js=(JasperReport)JRLoader.loadObject(this.getClass().getResource("/lk/ijse/asms/view/report/ContractPayseet.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(js, paramMap, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint,false);
            cmbJob.setItems(null);
            loadAllJob();
            btnGetPaySlip.setDisable(true);

        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void backManagerFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.MANAGER_DASHBOARD,contractPaymentPane);
    }


    public void jobReportFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.JOB_REPORT,contractPaymentPane);
    }

    public void contractPaymentFormOnAction(ActionEvent actionEvent) {
    }
}

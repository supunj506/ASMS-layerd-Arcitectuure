package lk.ijse.asms.controller.job;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.asms.dao.custom.JobDAO;
import lk.ijse.asms.dao.custom.PaymentPlaneDAO;
import lk.ijse.asms.dao.custom.QueryDAO;
import lk.ijse.asms.dao.custom.SubcPaymentDAO;
import lk.ijse.asms.dao.custom.impl.PaymentPlaneDAOImpl;
import lk.ijse.asms.dao.custom.impl.QueryDAOImpl;
import lk.ijse.asms.dao.util.PaymentPlaneType;
import lk.ijse.asms.db.DBConnection;
import lk.ijse.asms.dao.custom.impl.JobDAOImpl;
import lk.ijse.asms.dao.custom.impl.SubcPaymentDAOImpl;
import lk.ijse.asms.dto.PaymentPlaneDTO;
import lk.ijse.asms.dto.SubPaymentDTO;
import lk.ijse.asms.dto.JobDTO;
import lk.ijse.asms.util.Navigation;
import lk.ijse.asms.util.Routes;
import lk.ijse.asms.util.ValidateUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class FinishJobFormController {

    SubcPaymentDAO paymentDAO=new SubcPaymentDAOImpl();
    JobDAO jobDAO=new JobDAOImpl();
    QueryDAO queryDAO=new QueryDAOImpl();
    PaymentPlaneDAO paymentPlaneDAO=new PaymentPlaneDAOImpl();

    public AnchorPane finishJobPane;
    public JFXComboBox<String> cmbJob;
    public JFXTextField txtPower;
    public JFXTextField txtData;
    public JFXTextField txtCamera;
    public JFXButton btnFinishJob;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        loadJob();
        Pattern power = Pattern.compile("^[0-9]{1,2}$");
        map.put(txtPower, power);
        map.put(txtData, power);
        map.put(txtCamera, power);
        btnFinishJob.setDisable(true);
    }

    private void loadJob() {
        try {
            ObservableList<String> jobList = queryDAO.getFinishJob("DOING");
            cmbJob.setItems(jobList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addJobFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADD_JOB, finishJobPane);
    }

    public void callJobFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.CALL_JOB, finishJobPane);
    }

    public void finishJobOnAction(ActionEvent actionEvent) throws SQLException {
        Connection connection = null;
        boolean isFinish=true;
        String[] id = String.valueOf(cmbJob.getValue()).split(" / ");
        String jobId = id[0];
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PaymentPlaneDTO power = paymentPlaneDAO.getPointDetails(PaymentPlaneType.POWER);
            PaymentPlaneDTO data = paymentPlaneDAO.getPointDetails(PaymentPlaneType.DATA);
            PaymentPlaneDTO camera = paymentPlaneDAO.getPointDetails(PaymentPlaneType.CAMERA);

            double total = (Double.parseDouble(txtPower.getText()) * power.getUnitPrice()) +
                    (Double.parseDouble(txtData.getText()) * data.getUnitPrice()) +
                    (Double.parseDouble(txtCamera.getText()) * camera.getUnitPrice());
            JobDTO jobDTO =new JobDTO(
                    jobId,
                    LocalDate.now(),
                    "DONE",
                    Integer.parseInt(txtPower.getText()),
                    Integer.parseInt(txtData.getText()),
                    Integer.parseInt(txtCamera.getText())
            );
            boolean finish = jobDAO.addJob(jobDTO);
            if (finish) {
                connection.commit();

                JobDTO jobDTO1 = jobDAO.getJobById(jobId);
                String doneBy = jobDTO1.getDoneBy();
                clean();


                if (doneBy.equals("SUB CONTRACT")) {
                    String payId = paymentDAO.getNextId();
                    ArrayList<String> detail = queryDAO.getDetailForSubcPayment(jobId);
//                    new Alert(Alert.AlertType.CONFIRMATION,detail.get(0)+" "+detail.get(1)).show();

                    SubPaymentDTO subPaymentDTO = new SubPaymentDTO(
                            payId,
                            detail.get(0),
                            id[0],
                            Integer.parseInt(detail.get(1)),
                            Integer.parseInt(detail.get(2)),
                            Integer.parseInt(detail.get(3)),
                            total,
                            "REMINNING"
                    );
                    boolean save = paymentDAO.saveSubCPayment(subPaymentDTO);
                    if (save) {
                        connection.commit();
                        clean();

                    } else {
                        connection.rollback();
                        isFinish=false;
                    }
                }

            } else {
                connection.rollback();
                isFinish=false;
            }
            if(isFinish){
                new Alert(Alert.AlertType.CONFIRMATION, "Finish Job Successfully !!!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }


    }

    private void clean() {
        cmbJob.setItems(null);
        loadJob();
        txtCamera.clear();
        txtPower.clear();
        txtData.clear();
        btnFinishJob.setDisable(true);

    }

    public void backManagerFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.MANAGER_DASHBOARD, finishJobPane);
    }

    public void cmbJobOnAction(ActionEvent actionEvent) {
        loadJob();
    }

    public void keyReleasedOnAction(KeyEvent keyEvent) {

        ValidateUtil.validate(map, btnFinishJob);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidateUtil.validate(map, btnFinishJob);
            if (response instanceof JFXTextField) {
                JFXTextField txt = (JFXTextField) response;
                txt.requestFocus();
            }
        }

    }
}
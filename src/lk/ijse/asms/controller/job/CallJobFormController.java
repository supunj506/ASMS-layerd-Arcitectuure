package lk.ijse.asms.controller.job;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.asms.dao.custom.*;
import lk.ijse.asms.dao.custom.impl.*;
import lk.ijse.asms.db.DBConnection;
import lk.ijse.asms.dto.EmpTeamDTO;
import lk.ijse.asms.dto.JobDTO;
import lk.ijse.asms.util.Navigation;
import lk.ijse.asms.util.Routes;
import lk.ijse.asms.view.tm.TeamTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CallJobFormController {

    VehicleDAO vehicleDAO=new VehicleDAOImpl();
    JobDAO jobDAO=new JobDAOImpl();
    TeamDAO teamDAO=new TeamDAOImpl();
    EmpTeamDAO empTeamDAO=new EmpTeamDAOImpl();
    QueryDAO queryDAO=new QueryDAOImpl();

    public AnchorPane callJobPane;
    public JFXComboBox <String>cmbEmployee;
    public JFXRadioButton radioFull;
    public ToggleGroup empType;
    public JFXRadioButton radioContract;
    public JFXComboBox<String> cmbVehicle;
    public TableView<TeamTM> tableView;
    public TableColumn tvEmpId;
    public TableColumn tvEmpName;
    public TableColumn tvEmpDivision;
    public JFXComboBox <String >cmbJob;
    public JFXButton btnCallJob;
    public ImageView deleted;

    public void initialize(){

        loadJobDetails();
        loadVehicleDetails();
        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("division"));

    }

    private void loadVehicleDetails() {
        try {
             cmbVehicle.setItems(vehicleDAO.getAllVehicle());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadJobDetails() {
        ObservableList<String>list=FXCollections.observableArrayList();
        try {
            ArrayList<String> jobList = queryDAO.getJobDetails("TO DO");
            if(!jobList.isEmpty()){
                list.addAll(jobList);
                cmbJob.setItems(list);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    String doneBy=null;
    boolean fullSelected=false;
    boolean contractSelected=false;

    public void fullTimeEmployeeOnAction(ActionEvent actionEvent) {
        fullSelected=true;


        try {
            ObservableList<TeamTM> list = queryDAO.getEmployeeDetails("FULL TIME");
            ObservableList<String> employeeList=FXCollections.observableArrayList();
            for(TeamTM teamTM:list){
                employeeList.add(teamTM.getId()+" / "+teamTM.getName()+" / "+teamTM.getDivision());
            }
            cmbEmployee.setItems(employeeList);
            doneBy="COMPANY";
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void contractEmployeeOnAction(ActionEvent actionEvent) {
        contractSelected=true;
        try {
            ObservableList<TeamTM> list = queryDAO.getEmployeeDetails("CONTRACT BASE");
            ObservableList<String> employeeList=FXCollections.observableArrayList();
            for(TeamTM teamTM:list){
                employeeList.add(teamTM.getId()+" / "+teamTM.getName()+" / "+teamTM.getDivision());
            }
            cmbEmployee.setItems(employeeList);
                doneBy = "SUB CONTRACT";

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    ObservableList<TeamTM>list=FXCollections.observableArrayList();
    public void cmbEmpOnAction(ActionEvent actionEvent) {
        if(fullSelected){
            radioContract.setDisable(true);
        } else if(contractSelected){
            radioFull.setDisable(true);
        }
        if(cmbEmployee.getValue()!=null) {
            String[] temp = String.valueOf(cmbEmployee.getValue()).split(" / ");
            TeamTM teamTM = new TeamTM(
                    temp[0],
                    temp[1],
                    temp[2]
            );

            if (isExists(teamTM)) {
                new Alert(Alert.AlertType.ERROR, "Employee Already in Team !!! ").show();
            } else {
                if (contractSelected) {
                    if (list.size() >= 1) {
                        new Alert(Alert.AlertType.ERROR, "Contract Employee Already Selected !!! Please Remove Exists Employee And Add Other !!!  ").show();
                    } else {
                        list.add(teamTM);
                        tableView.setItems(list);
                    }
                } else {
                    list.add(teamTM);
                    tableView.setItems(list);
                }
            }
        }
    }
    public boolean isExists(TeamTM teamTM){
        for(TeamTM temp:list){
            if(temp.getId().equals(teamTM.getId())){
                return true;
            }
        }
        return false;
    }


    public void callJobOnAction(ActionEvent actionEvent) throws SQLException {

        String []vehicleId=String.valueOf(cmbVehicle.getValue()).split(" / ");
        String []jobId=String.valueOf(cmbJob.getValue()).split(" / ");
        LocalDate startDate=LocalDate.now();
        String jobStatus="DOING";
        boolean isCall=true;
        Connection connection=null;
        try {
            if(cmbJob.getValue()==(null)||cmbVehicle.getValue()==(null)||list.size()==0){
                new Alert(Alert.AlertType.ERROR,"Missing data found Check Again !!!").show();
            }else{
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
                JobDTO jobDTO =new JobDTO(
                        jobId[0],
                        vehicleId[0],
                        startDate,
                        jobStatus,
                        doneBy
                );

            boolean jobCall = jobDAO.callJob(jobDTO);
            if (jobCall) {
                connection.commit();
                String teamId = teamDAO.getNextTeamId();
                boolean teamSave = teamDAO.saveTeam(teamId, jobId[0]);
                if (teamSave) {
                    connection.commit();
                    boolean empTeamSave = false;

                    for (TeamTM temp : list) {
                        empTeamSave = empTeamDAO.saveEmp_Team(new EmpTeamDTO(teamId, temp.getId()));
                    }
                    if (empTeamSave) {
                        connection.commit();
                    } else {
                        isCall = false;
                        connection.rollback();
                    }
                } else {
                    connection.rollback();
                    isCall = false;
                }
            } else {
                connection.rollback();
                isCall = false;
            }
            if (isCall) {
                new Alert(Alert.AlertType.CONFIRMATION, "Call Job Successfully !!!").show();
                clean();
            }
        }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e+"");
        }finally {
            assert connection != null;
            connection.setAutoCommit(true);
        }
    }
    public void clean(){
        list.clear();
        loadJobDetails();
        cmbVehicle.setItems(null);
        loadVehicleDetails();
        cmbEmployee.setItems(null);
        radioFull.setSelected(false);
        radioContract.setSelected(false);
        radioFull.setDisable(false);
    }

    public void addJobFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADD_JOB,callJobPane);
    }

    public void finishJobFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.FINISH_JOB,callJobPane);
    }

    public void backManagerFormOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.MANAGER_DASHBOARD,callJobPane);
    }

    public void deleteTableOnAction(ActionEvent actionEvent) {
        list.clear();
        tableView.setItems(list);
        radioFull.setDisable(false);
        radioContract.setDisable(false);
        contractSelected=false;
        fullSelected=false;
        cmbEmployee.setItems(null);
        radioFull.setSelected(false);
        radioContract.setSelected(false);

    }

}

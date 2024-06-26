package Controller;

import DB.Global;
import VTable.SalesGstReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BuffaloReports
{

    @FXML
    private Button export;
    @FXML
    private Label lblmilkrprt, lblTotalMilk, lblAvgGenFat, lblTotalAmount;
    @FXML
    private TableView<SalesGstReport> tbl;
    @FXML
    private DatePicker sdate, edate;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn<SalesGstReport, Integer> snumber;
    @FXML
    private TableColumn<SalesGstReport, String> cname;
    @FXML
    private TableColumn<SalesGstReport, Double>  totalmilk, avgfat, avgdegree, avgsnf, avgrate, tpayment;

    private ObservableList<SalesGstReport> data = FXCollections.observableArrayList();

    FilteredList<SalesGstReport> filteredData;

    private double ttlmilk = 0.0, ttlavggenfat = 0.0, ttlamt = 0.0;


    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblmilkrprt.setText("Buffalo Milk Report");
            sdate.setPromptText("From");
            edate.setPromptText("Up To");
            txtSearch.setPromptText("Search");
            export.setText("Export");
            snumber.setText("Sr.No.");
            cname.setText("Customer Name");
            totalmilk.setText("Total Milk");
            avgfat.setText("Avg. Fat");
            avgdegree.setText("Avg. Degree");
            avgsnf.setText("Total General FAT");
            avgrate.setText("Avg. Rate");
            tpayment.setText("Total Payment");
        }

        try
        {
            snumber.setCellValueFactory(new PropertyValueFactory<>("snumber"));
            cname.setCellValueFactory(new PropertyValueFactory<>("cname"));
            totalmilk.setCellValueFactory(new PropertyValueFactory<>("totalmilk"));
            avgfat.setCellValueFactory(new PropertyValueFactory<>("avgfat"));
            avgdegree.setCellValueFactory(new PropertyValueFactory<>("avgdegree"));
            avgsnf.setCellValueFactory(new PropertyValueFactory<>("avgsnf"));
            avgrate.setCellValueFactory(new PropertyValueFactory<>("avgrate"));
            tpayment.setCellValueFactory(new PropertyValueFactory<>("tpayment"));

            sdate.setOnAction(event -> tbl.setItems(getSalesData()));

            edate.setOnAction(event -> tbl.setItems(getSalesData()));

            export.setOnAction(event -> {
                try {
                    Workbook workbook = new HSSFWorkbook();
                    Sheet spreadsheet = workbook.createSheet("विक्री अहवाल");
                    org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(0);

                    for (int j = 0; j < tbl.getColumns().size()-1; j++) {
                        row.createCell(j).setCellValue(tbl.getColumns().get(j).getText());
                    }

                    for (int i = 0; i < tbl.getItems().size(); i++) {
                        row = spreadsheet.createRow(i + 1);
                        for (int j = 0; j < tbl.getColumns().size()-1; j++) {
                            if(tbl.getColumns().get(j).getCellData(i) != null) {
                                row.createCell(j).setCellValue(tbl.getColumns().get(j).getCellData(i).toString());
                            }
                            else {
                                row.createCell(j).setCellValue("");
                            }
                        }
                    }
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("सेव्ह करण्यासाठी फाईल निर्दिष्ट करा");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File (*.xls)", "*.xls");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(new Stage());
                    if (file != null) {
                        try
                        {
                            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
                            workbook.write(fileOut);
                            fileOut.close();
                        }
                        catch (IOException ignored)
                        {

                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.print(e);
                }
            });

        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

        filteredData = new FilteredList<>(data, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Double.toString(customer.getTotalmilk()).contains(lowerCaseFilter))
                {
                    return true; // Filter matches first name.
                }
                else if (Double.toString(customer.getAvgfat()).contains(lowerCaseFilter))
                {
                    return true; // Filter matches first name.
                }
                else if (Double.toString(customer.getAvgdegree()).contains(lowerCaseFilter))
                {
                    return true; // Filter matches first name.
                }
                else if (Double.toString(customer.getAvgsnf()).contains(lowerCaseFilter))
                {
                    return true; // Filter matches first name.
                }
                else if  (Double.toString(customer.getAvgrate()).contains(lowerCaseFilter))
                {
                    return true;
                }
                else return (Double.toString(customer.getTpayment()).contains(lowerCaseFilter));

            });
        });
        txtSearch.setOnKeyReleased(event -> setSearchData());

    }

    private void setSearchData()
    {
        SortedList<SalesGstReport> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbl.comparatorProperty());
        tbl.setItems(sortedData);
    }

    private ObservableList<SalesGstReport> getSalesData() {


        ttlmilk = 0.0;
        ttlavggenfat = 0.0;
        ttlamt = 0.0;

        LocalDate sd = sdate.getValue();
        LocalDate ed = edate.getValue();
        if(sd!=null && ed!=null)
        {
            data.clear();
            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                List<Object[]> result = session.createQuery("select s.customer.name, sum(s.totalmilk), avg(s.cfat), avg(s.cdegree), sum(s.gfat), avg(s.rate), sum(s.totalamt) from DB.BuffaloMilk s where s.date>=:id and s.date<=:id1 and s.d=1 group by s.customer.name").setParameter("id",sd).setParameter("id1",ed).list();
                int i = 1;
                for(Object[] obj : result){
                    String cname1 =String.valueOf(obj[0]);
                    double totalmilk1 = Double.parseDouble(String.valueOf(obj[1]));
                    double avgfat1 = Double.parseDouble(String.valueOf(obj[2]));
                    double avgdegree1 = Double.parseDouble(String.valueOf(obj[3]));
                    double avgsnf1 = Double.parseDouble(String.valueOf(obj[4]));
                    double avgrate1 = Double.parseDouble(String.valueOf(obj[5]));
                    double tpayment1 = Double.parseDouble(String.valueOf(obj[6]));
                    SalesGstReport a = new SalesGstReport();
                    a.setSnumber(i++);
                    a.setCname(cname1);
                    a.setTotalmilk(Global.round2(totalmilk1));
                    a.setAvgfat(Global.round2(avgfat1));
                    a.setAvgdegree(Global.round2(avgdegree1));
                    a.setAvgsnf(Global.round2(avgsnf1));
                    a.setAvgrate(Global.round2(avgrate1));
                    a.setTpayment(Global.round2(tpayment1));
                    data.add(a);
                    ttlmilk = ttlmilk + Global.round2(a.getTotalmilk());
                    ttlavggenfat = ttlavggenfat + Global.round2(a.getAvgsnf());
                    ttlamt = ttlamt + Global.round2(a.getTpayment());
                }
                transaction.commit();
                session.close();
                lblTotalMilk.setText("Total Milk : " + Global.round2(ttlmilk));
                lblAvgGenFat.setText("Total General FAT : " + Global.round2(ttlavggenfat));
                lblTotalAmount.setText("Total Amount : " + Global.round2(ttlamt));
                return data;
            }
            catch (Exception e)
            {
                lblTotalMilk.setText("Total Milk : 0.0 " );
                lblAvgGenFat.setText("Total General FAT : 0.0 " );
                lblTotalAmount.setText("Total Amount : 0.0 " );
                System.out.println(e.toString());
                data.clear();
                return data;
            }
        }
        else if(sd!=null)
        {
            data.clear();
            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                List<Object[]> result = session.createQuery("select s.customer.name, sum(s.totalmilk), avg(s.fat), avg(s.degree), sum(s.gfat), avg(s.rate), sum(s.totalamt) from BuffaloMilk s where s.date=:id and s.d=1 group by s.customer.name").setParameter("id",sd).list();
                int i = 1;
                for(Object[] obj : result){
                    String cname1 =String.valueOf(obj[0]);
                    double totalmilk1 = Double.parseDouble(String.valueOf(obj[1]));
                    double avgfat1 = Double.parseDouble(String.valueOf(obj[2]));
                    double avgdegree1 = Double.parseDouble(String.valueOf(obj[3]));
                    double avgsnf1 = Double.parseDouble(String.valueOf(obj[4]));
                    double avgrate1 = Double.parseDouble(String.valueOf(obj[5]));
                    double tpayment1 = Double.parseDouble(String.valueOf(obj[6]));
                    SalesGstReport a = new SalesGstReport();
                    a.setSnumber(i++);
                    a.setCname(cname1);
                    a.setTotalmilk(Global.round2(totalmilk1));
                    a.setAvgfat(Global.round2(avgfat1));
                    a.setAvgdegree(Global.round2(avgdegree1));
                    a.setAvgsnf(Global.round2(avgsnf1));
                    a.setAvgrate(Global.round2(avgrate1));
                    a.setTpayment(Global.round2(tpayment1));
                    data.add(a);
                    ttlmilk = ttlmilk + Global.round2(a.getTotalmilk());
                    ttlavggenfat = ttlavggenfat + Global.round2(a.getAvgsnf());
                    ttlamt = ttlamt + Global.round2(a.getTpayment());
                }
                transaction.commit();
                session.close();
                lblTotalMilk.setText("Total Milk : " + Global.round2(ttlmilk));
                lblAvgGenFat.setText("Total General FAT : " + Global.round2(ttlavggenfat));
                lblTotalAmount.setText("Total Amount : " + Global.round2(ttlamt));
                return data;
            }
            catch (Exception e)
            {
                lblTotalMilk.setText("Total Milk : 0.0 " );
                lblAvgGenFat.setText("Total General FAT : 0.0 " );
                lblTotalAmount.setText("Total Amount : 0.0 " );
                System.out.println(e.toString());
                data.clear();
                return data;
            }
        }
        else if(ed!=null)
        {
            data.clear();
            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                List<Object[]> result = session.createQuery("select s.customer.name, sum(s.totalmilk), avg(s.fat), avg(s.degree), sum(s.gfat), avg(s.rate), sum(s.totalamt) from BuffaloMilk s where s.date=:id1 and s.d=1 group by s.customer.name").setParameter("id1",ed).list();
                int i = 1;
                for(Object[] obj : result){
                    String cname1 =String.valueOf(obj[0]);
                    double totalmilk1 = Double.parseDouble(String.valueOf(obj[1]));
                    double avgfat1 = Double.parseDouble(String.valueOf(obj[2]));
                    double avgdegree1 = Double.parseDouble(String.valueOf(obj[3]));
                    double avgsnf1 = Double.parseDouble(String.valueOf(obj[4]));
                    double avgrate1 = Double.parseDouble(String.valueOf(obj[5]));
                    double tpayment1 = Double.parseDouble(String.valueOf(obj[6]));
                    SalesGstReport a = new SalesGstReport();
                    a.setSnumber(i++);
                    a.setCname(cname1);
                    a.setTotalmilk(Global.round2(totalmilk1));
                    a.setAvgfat(Global.round2(avgfat1));
                    a.setAvgdegree(Global.round2(avgdegree1));
                    a.setAvgsnf(Global.round2(avgsnf1));
                    a.setAvgrate(Global.round2(avgrate1));
                    a.setTpayment(Global.round2(tpayment1));
                    data.add(a);
                    ttlmilk = ttlmilk + Global.round2(a.getTotalmilk());
                    ttlavggenfat = ttlavggenfat + Global.round2(a.getAvgsnf());
                    ttlamt = ttlamt + Global.round2(a.getTpayment());
                }
                transaction.commit();
                session.close();
                lblTotalMilk.setText("Total Milk : " + Global.round2(ttlmilk));
                lblAvgGenFat.setText("Total General FAT : " + Global.round2(ttlavggenfat));
                lblTotalAmount.setText("Total Amount : " + Global.round2(ttlamt));
                return data;
            }
            catch (Exception e)
            {
                lblTotalMilk.setText("Total Milk : 0.0 " );
                lblAvgGenFat.setText("Total General FAT : 0.0 " );
                lblTotalAmount.setText("Total Amount : 0.0 " );
                System.out.println(e.toString());
                data.clear();
                return data;
            }
        }
        else
        {
            data.clear();
            return data;
        }
    }
}

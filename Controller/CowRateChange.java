package Controller;
import VTable.BillProd;
import VTable.Customer;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import DB.CowRate;
import DB.Global;
import VTable.RateChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CowRateChange {
    @FXML
    private Label lblcust, lbldate;
    @FXML
    private DatePicker date;
    @FXML
    private Label lblWarn;
    @FXML
    private Button btnSave, btnReset;
    @FXML
    private TableView<RateChange> tbl;

    private ObservableList<VTable.RateChange> datat = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        btnReset.setOnAction(event -> {
            tbl.getItems().clear();
            lblWarn.setText("");
        });

        date.setValue(LocalDate.now());

        if(Global.lang){
            lblcust.setText("Cow Milk Rate Change");
            lbldate.setText("Date :");
        }



        try {
            tbl.setEditable(true);
            tbl.getSelectionModel().cellSelectionEnabledProperty().set(true);

            TableColumn fat = new TableColumn("Fat");
            fat.setMinWidth(200);
            fat.setCellValueFactory(new PropertyValueFactory<VTable.RateChange, String>("fat"));

            TableColumn snf = new TableColumn("snf");
            snf.setMinWidth(200);
            snf.setCellValueFactory(new PropertyValueFactory<VTable.RateChange, String>("snf"));


            TableColumn price = new TableColumn("Price");
            price.setMinWidth(300);
            price.setCellValueFactory(new PropertyValueFactory<VTable.RateChange, String>("rate"));
            price.setCellFactory(TextFieldTableCell.forTableColumn());
            price.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<VTable.RateChange, String>>) t1 -> {
                t1.getTableView().getItems().get(t1.getTablePosition().getRow()).setRate(t1.getNewValue());
                tbl.getSelectionModel().selectNext();
                tbl.refresh();
            });

            tbl.getColumns().addAll(fat,snf,price);
            tbl.setItems(datat);




      }catch (Exception e){
            System.out.println("ERROR : "+e);
        }
        tbl.setItems(getCustomerData());

        btnSave.setOnAction(event -> {
//            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();

//                List<CowRate> cowRateList = session.createQuery("from DB.CowRate s where s.d=1").list();
//                for(DB.CowRate cowRate : cowRateList)
//                {
//                    cowRate.setD(0);
//
//                    session.update(cowRate);
//                }

                for (VTable.RateChange rc : datat) {
                    if (Global.isDouble(rc.getRate())) {
                        System.out.println(rc.getRate());
                        DB.CowRate cowRate = new CowRate();
                        System.out.println(rc.getId());
                        cowRate.setId(rc.getId());
                        cowRate.setFat(Double.parseDouble(rc.getFat()));
                        cowRate.setSnf(Double.parseDouble(rc.getSnf()));
                        cowRate.setRate(Double.parseDouble(rc.getRate()));
                        cowRate.setD(1);
                        session.update(cowRate);
                    }
                }
                transaction.commit();
                session.close();
                lblWarn.setText("Cow Milk Rate Update Successfully..!!");
//            }catch (Exception e){
//                System.out.println("Error in Save Record...!!"+e);
//            }
        });
//btnPrint.setOnAction(event -> printAllInvoice());

    }

    private ObservableList<RateChange> getCustomerData() {
        Session session = Global.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<DB.CowRate> clist = session.createQuery("from DB.CowRate s where s.d=1").list();
            int i = 1;
            for (Iterator iterator = clist.iterator(); iterator.hasNext(); i++) {
                DB.CowRate c = (DB.CowRate) iterator.next();

                VTable.RateChange c1 = new  VTable.RateChange();
                c1.setId(c.getId());
                c1.setFat(String.valueOf(c.getFat()));
                c1.setSnf(String.valueOf(c.getSnf()));
                c1.setRate(String.valueOf(c.getRate()));
                datat.add(c1);
            }
            transaction.commit();
            session.close();
            return datat;
        } catch (Exception e) {
            return datat;
        }
    }



}

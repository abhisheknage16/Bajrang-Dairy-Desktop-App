package Controller;

import DB.Global;
import VTable.RateChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class BuffaloRateChange {
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
    private ObservableList<RateChange> datat = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        date.setValue(LocalDate.now());

        if(Global.lang){
            lblcust.setText("Buffalo Milk Rate Change");
            lbldate.setText("Date :");
        }

        btnReset.setOnAction(event -> {
            tbl.getItems().clear();
            lblWarn.setText("");
        });

        tbl.setEditable(true);
        tbl.getSelectionModel().cellSelectionEnabledProperty().set(true);


        TableColumn fat = new TableColumn("FAT");
        fat.setMinWidth(200);
        fat.setCellValueFactory(new PropertyValueFactory<RateChange, Double>("fat"));


        TableColumn price = new TableColumn("Price");
        price.setMinWidth(300);
        price.setCellValueFactory(new PropertyValueFactory<VTable.RateChange, String>("rate"));
        price.setCellFactory(TextFieldTableCell.forTableColumn());
        price.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<RateChange, String>>) t -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setRate(t.getNewValue());
            tbl.getSelectionModel().selectNext();
            tbl.refresh();
        });

        tbl.getColumns().addAll( fat, price);

        tbl.setItems(datat);

        setTableData();

//        if (datat.size() == 0) {
//            double temp = 5.1;
//            for (int i = 1; i <= 63; i++) {
//                VTable.RateChange sd1 = new VTable.RateChange();
////                sd1.setSrno(i);
//                sd1.setFat(String.valueOf(Global.round(temp)));
//                temp = temp+0.1;
//                sd1.setRate("");
//                datat.add(sd1);
//            }
//        }



        btnSave.setOnAction(event -> {
            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();

                // write query here
                List<DB.BuffaloRate> cowRateList = session.createQuery("from DB.BuffaloRate s where s.d=1").list();
                for(DB.BuffaloRate cowRate : cowRateList)
                {
                    cowRate.setD(0);
                    session.update(cowRate);
                }

                for (VTable.RateChange rc : datat) {
                    if (Global.isDouble(rc.getRate())) {
                        DB.BuffaloRate cowRate = new DB.BuffaloRate();
                        cowRate.setFat(Double.parseDouble(rc.getFat()));
                        cowRate.setRate(Double.parseDouble(rc.getRate()));
                        cowRate.setD(1);
                        session.persist(cowRate);
                    }
                }

                transaction.commit();
                session.close();
                lblWarn.setText("Buffalo Milk Rate Update Successfully..!!");
            }catch (Exception e){
                System.out.println(e+"Error in Save Record...!!");
            }
        });
    }

    private void setTableData() {
        try {
            datat.clear();
            tbl.refresh();
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            double temp = 5.1;
            for (int i = 1; i <= 63; i++) {
                DB.BuffaloRate buffaloRate = (DB.BuffaloRate) session.createQuery("from DB.BuffaloRate s where s.fat=:id and s.d=1").setParameter("id", Global.round(temp)).uniqueResult();
                if(buffaloRate != null){
                    RateChange d = new RateChange();
                    d.setId(buffaloRate.getId());
                    d.setFat(String.valueOf(buffaloRate.getFat()));
                    d.setRate(String.valueOf(buffaloRate.getRate()));
                    datat.add(d);
                }
                else{
                    VTable.RateChange sd1 = new VTable.RateChange();
                    sd1.setFat(String.valueOf(Global.round(temp)));
                    sd1.setRate("");
                    datat.add(sd1);
                }
                temp = temp+0.1;
            }

            tbl.refresh();

            transaction.commit();
            session.close();
        }catch (Exception e){
            System.out.println("Fetching record : "+e.toString());
        }
    }
}

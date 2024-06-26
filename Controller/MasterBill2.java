package Controller;

import DB.*;
import VTable.Bill;
import com.sun.prism.impl.Disposer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MasterBill2
{
    @FXML
    private Button btnExport, btnSave, btnPrint;
    @FXML
    private TableView<Bill> tbl;
    @FXML
    private DatePicker sdate, edate, bdate;
    @FXML
    private TextField txtSearch, txtBno;
    @FXML
    private Label lblTitle, lblamount, lblbill, lblmilk, lblgfat;
    @FXML
    private ObservableList<Bill> data = FXCollections.observableArrayList();
    private FilteredList<Bill> filteredData;

    private double gttlgfat = 0.0, gttlamount = 0.0,  gcttlltr = 0.0, gcttlgfat = 0.0, gcttlamt = 0.0, gcttlsnf = 0.0;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblTitle.setText("Master Bill Cow");
            txtBno.setPromptText("Bill No.");
            btnSave.setText("Save");
            btnPrint.setText("Print");
            sdate.setPromptText("From");
            edate.setPromptText("Up To");
            txtSearch.setPromptText("Search");
            btnExport.setText("Export");
        }
        try
        {
            tbl.setEditable(true);
            tbl.getSelectionModel().cellSelectionEnabledProperty().set(true);

            TableColumn tsrno = new TableColumn("अनु.क्र.");
            tsrno.setMinWidth(20);
            tsrno.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Integer>("srno"));

            TableColumn tcno = new TableColumn("सभासद नं");
            tcno.setMinWidth(100);
            tcno.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Integer>("cno"));

            TableColumn tcname = new TableColumn("सभासदांचे नाव");
            tcname.setMinWidth(125);
            tcname.setCellValueFactory(new PropertyValueFactory<VTable.Bill, String>("cname"));

            TableColumn tttlmilk = new TableColumn("एकूण दूध");
            tttlmilk.setMinWidth(100);
            tttlmilk.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Double>("ttlmilk"));

            TableColumn tttlgfat = new TableColumn("एकूण जनरल फॅट");
            tttlgfat.setMinWidth(150);
            tttlgfat.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Double>("ttlgfat"));
//
//            TableColumn tttlgsnf = new TableColumn("एकूण जनरल एनएफएस");
//            tttlgsnf.setMinWidth(150);
//            tttlgsnf.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Double>("ttlgsnf"));
//

            TableColumn tttlamount = new TableColumn("एकूण रक्कम");
            tttlamount.setMinWidth(150);
            tttlamount.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Double>("ttlamount"));

            TableColumn thafta = new TableColumn("हप्ता");
            thafta.setMinWidth(25);
            thafta.setCellValueFactory(new PropertyValueFactory<VTable.Bill, String>("hafta"));
            thafta.setCellFactory(TextFieldTableCell.forTableColumn());
            thafta.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Bill, String>>) t ->
            {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setHafta(t.getNewValue());
                double ttl = t.getTableView().getItems().get(t.getTablePosition().getRow()).getTtlamount();
                if(Global.isDouble(t.getNewValue()))
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setPaid(Global.round2(ttl - Double.parseDouble(t.getNewValue())));
                tbl.getSelectionModel().selectNext();
            });

            TableColumn tpaid = new TableColumn("पैसे देणे");
            tpaid.setMinWidth(150);
            tpaid.setCellValueFactory(new PropertyValueFactory<VTable.Bill, Double>("paid"));

            tbl.getColumns().addAll(tsrno, tcno, tcname, tttlmilk, tttlgfat, tttlamount, thafta, tpaid);

            TableColumn col_action1 = new TableColumn<>("Print");
            tbl.getColumns().add(col_action1);
            col_action1.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
            col_action1.setCellFactory((Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>) p -> new ButtonCell1());

            TableColumn col_action = new TableColumn<>("Action");
            tbl.getColumns().add(col_action);
            col_action.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
            col_action.setCellFactory((Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>) p -> new ButtonCell());

        }
        catch (Exception ignored) {}

        bdate.setValue(LocalDate.now());

        filteredData = new FilteredList<>(data, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getCno().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (customer.getCname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        txtSearch.setOnKeyReleased(event -> setSearchData());

        btnExport.setOnAction(event -> {
            try {
                printReport();
            }
            catch (Exception ignored)
            {
            }
        });

        sdate.setOnAction(event -> getTableData());

        edate.setOnAction(event -> getTableData());

        tbl.setItems(data);

        btnPrint.setOnAction(event -> printInvoice());

        btnSave.setOnAction(event -> saveBill());

    }

//  print report


//

    private void saveBill()
    {
        try
        {
            LocalDate sd = bdate.getValue();
            if(data.size() > 0 && sd != null)
            {
                String remark = txtBno.getText();
                if(remark.equals(""))
                    remark = "Bill Date : "+ sd;

                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                for(Bill s : data)
                {
                    double hafta = 0.0;
                    DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.sno=:id and s.name=:id1").setParameter("id", s.getCno()).setParameter("id1", s.getCname()).uniqueResult();
                    if(customer != null)
                    {
                        if(Global.isDouble(s.getHafta()))
                            hafta = Double.parseDouble(s.getHafta());
                        if(hafta != 0.0)
                        {
                            double pending1 = customer.getPending();
                            double deposited1 = customer.getDeposite() + hafta;
                            if(pending1 >= deposited1)
                            {
                                pending1 = pending1 - deposited1;
                                deposited1 = 0.0;
                            }
                            else
                            {
                                deposited1 = deposited1 - pending1;
                                pending1 = 0.0;
                            }
                            customer.setPending(pending1);
                            customer.setDeposite(deposited1);
                            session.update(customer);

                            DB.CustomerPaymentDeposite c = new CustomerPaymentDeposite();
                            c.setAmount(hafta);
                            c.setCustomer(customer);
                            c.setCredit(pending1);
                            c.setDeposite(deposited1);
                            c.setDate(sd);
                            c.setRemark(remark);
                            c.setD(1);
                            session.persist(c);
                        }
                    }
                }
                transaction.commit();
                session.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Payment Successfully added..!!");
                alert.setHeaderText(null);
                alert.setContentText("Customer Payment Successfully added..!!");
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Something went wrong..!!");
                alert.setHeaderText(null);
                alert.setContentText("Please select valid dates..!!");
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something went wrong..!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid dates..!!");
            alert.showAndWait();
        }
    }

    private void getTableData()
    {
        LocalDate sd1 = sdate.getValue();
        LocalDate ed1 = edate.getValue();
        if(sd1 != null && ed1 != null)
        {
            try
            {
                tbl.getItems().clear();
                data.clear();
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();

                List<DB.Customer> clist = session.createQuery("from DB.Customer s where s.d=1").list();
                int i = 1;
                for (DB.Customer customer : clist)
                {
                    long cid = customer.getId();
                    VTable.Bill bill = new Bill();
                    bill.setSrno(i);
                    bill.setCno(customer.getSno());
                    bill.setCname(customer.getName());
                    bill.setHafta(String.valueOf(customer.getHafta()));

                    double ttlmilk = 0.0, ttlgfat = 0.0,ttlsnf = 0.0, ttlamount = 0.0;

//              for Morning
                    {


//
//                    for Cow
                        {
                            LocalDate sd = sd1;
                            LocalDate ed = ed1.plusDays(1);;
                            while(sd.isBefore(ed))
                            {
                                double bttlltr = 0.0, bavgfat = 0.0, bavgsnf = 0.0, bavgrate = 0.0, bttlgfat = 0.0, bttlamount = 0.0;
//                      Total Milk sum
                                {
                                    Query query1 = session.createQuery("select sum(s.totalmilk) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bttlltr = Double.parseDouble(s1);
                                }

//                      Total Fat Avg
                                {
                                    Query query1 = session.createQuery("select avg(s.fat) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgfat = Double.parseDouble(s1);
                                }



                                //                              Total SNF Avg
                                {
                                    Query query1 = session.createQuery("select avg(s.degree) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgsnf = Double.parseDouble(s1);
                                }
//                      Average rate
                                {
                                    Query query1 = session.createQuery("select avg(s.rate) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgrate = Double.parseDouble(s1);
                                }

                                bttlltr = Global.round2(bttlltr);
                                bavgfat = Global.round2(bavgfat);
                                bavgsnf = Global.round2(bavgsnf);
                                bavgrate = Global.round2(bavgrate);



                                bttlgfat = Global.round(bavgfat * bttlltr);
                                bttlamount = Global.round2(bttlgfat * bavgrate);



                                ttlmilk = ttlmilk + Global.round2(bttlltr);
                                ttlgfat = ttlgfat + Global.round2(bttlgfat);
                                ttlsnf = bavgsnf + Global.round2(bavgsnf);
                                ttlamount = ttlamount + Global.round2(bttlamount);

                                sd = sd.plusDays(1);
                            }
                        }

                    }

//              for Evening
                    {


//                    for Cow
                        {
                            LocalDate sd = sd1;
                            LocalDate ed = ed1.plusDays(1);;
                            while(sd.isBefore(ed))
                            {
                                double bttlltr = 0.0, bavgfat = 0.0, bavgrate = 0.0,bavgsnf = 0.0,  bttlgfat = 0.0, bttlamount = 0.0;
//                      Total Milk sum
                                {
                                    Query query1 = session.createQuery("select sum(s.totalmilk) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bttlltr = Double.parseDouble(s1);
                                }

//                      Total Fat Avg
                                {
                                    Query query1 = session.createQuery("select avg(s.fat) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgfat = Double.parseDouble(s1);
                                }
//                                Total SNF Avg
                                {
                                    Query query1 = session.createQuery("select avg(s.degree) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgsnf = Double.parseDouble(s1);
                                }

//                      Average rate
                                {
                                    Query query1 = session.createQuery("select avg(s.rate) from DB.CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                                    String s1 = String.valueOf(query1.uniqueResult());
                                    if (Global.isDouble(s1))
                                        bavgrate = Double.parseDouble(s1);
                                }

                                bttlltr = Global.round2(bttlltr);
                                bavgfat = Global.round2(bavgfat);
                                bavgrate = Global.round2(bavgrate);

                                bttlgfat = Global.round2(bavgfat * bttlltr);
                                bttlamount = Global.round2(bttlgfat * bavgrate);

                                ttlmilk = ttlmilk + Global.round2(bttlltr);
                                ttlgfat = ttlgfat + Global.round2(bttlgfat);
                                ttlsnf = bavgsnf + Global.round2(bavgsnf);
                                ttlamount = ttlamount + Global.round2(bttlamount);

                                sd = sd.plusDays(1);
                            }
                        }

                    }



                    if(ttlmilk != 0.0)
                    {
                        bill.setTtlmilk(Global.round2(ttlmilk));
                        bill.setTtlgfat(Global.round2(ttlgfat));
                        bill.setTtlgsnf(Global.round2(ttlsnf));
                        bill.setTtlamount(Global.round2(ttlamount));
                        bill.setPaid(Global.round2(ttlamount - customer.getHafta()));
                        data.add(bill);
                        i++;
                    }
                }
                transaction.commit();
                session.close();
                tbl.setItems(data);
                setTableBottomLabel();
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                tbl.getItems().clear();
                data.clear();
            }
        }
        else
        {
            tbl.getItems().clear();
            data.clear();
        }
    }

    private void setSearchData()
    {
        SortedList<Bill> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbl.comparatorProperty());
        tbl.setItems(sortedData);
        setTableBottomLabel();
    }

    private void setTableBottomLabel()
    {
        double ttlbill = 0.0, ttlmilk = 0.0, ttlgfat = 0.0, ttlamount = 0.0;
        try
        {
            for (int i = 0; i < tbl.getItems().size(); i++)
            {
                ttlbill = ttlbill + 1;
                ttlmilk = ttlmilk + Double.parseDouble(tbl.getColumns().get(3).getCellData(i).toString());
                ttlgfat = ttlgfat + Double.parseDouble(tbl.getColumns().get(4).getCellData(i).toString());
                ttlamount = ttlamount + Double.parseDouble(tbl.getColumns().get(5).getCellData(i).toString());
            }
            lblbill.setText("Total Bill : "+Global.round2(ttlbill));
            lblmilk.setText("Total Milk : "+Global.round2(ttlmilk));
            lblgfat.setText("Total G.Fat : "+Global.round2(ttlgfat));
            lblamount.setText("Total Amount : "+Global.round2(ttlamount));
        }
        catch (Exception e)
        {
            lblbill.setText("Total Bill : 0.0");
            lblmilk.setText("Total Milk : 0.0");
            lblgfat.setText("Total G.Fat : 0.0");
            lblamount.setText("Total Amount : 0.0");
        }
    }

    //    For multiple invoice
    private void printInvoice()
    {
        LocalDate sd = sdate.getValue();
        LocalDate ed = edate.getValue();
        String sd1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(sd);
        String ed1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(ed);
        if(data.size() > 0 && sd != null && ed != null)
        {
            List<Map<String, Object>> ParamList = new ArrayList<Map<String, Object>>();
            List<JRDataSource> SourceList = new ArrayList<JRDataSource>();
            for(Bill s : data)
            {
                SourceList.add(getReportTableData(sd, ed, s.getCno(), s.getCname()));
                ParamList.add(getReportParameter(sd1, ed1, s.getCno(), s.getCname(), gttlgfat, gttlamount, s.getHafta(), Global.round2(gttlamount - Double.parseDouble(s.getHafta())),  gcttlltr, gcttlgfat, gcttlamt, gcttlsnf));
            }
            try
            {
                createReport(ParamList, SourceList);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something went wrong..!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid dates..!!");
            alert.showAndWait();
        }
    }

    private JRDataSource getReportTableData(LocalDate sd, LocalDate ed, String cno, String cname)
    {
        try
        {
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.sno=:id and s.name=:id1").setParameter("id", cno).setParameter("id1", cname).uniqueResult();
            long cid = customer.getId();
            gttlgfat = 0.0; gttlamount = 0.0; gcttlsnf = 0.0; gcttlltr = 0.0; gcttlgfat = 0.0; gcttlamt = 0.0;
            ed = ed.plusDays(1);
            List<VTable.BillProd> bplist = new ArrayList();
            int i = 1;
            while(sd.isBefore(ed))
            {

//              for Morning
                {
                    VTable.BillProd bp = new VTable.BillProd();
                    bp.setSrno(i++);
                    bp.setDate(sd);
                    bp.setStrdate(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(sd));

                    double bttlltr = 0.0, bavgfat = 0.0, bttlgfat = 0.0, bttlamt = 0.0, bavgrate = 0.0, bttlamt1 = 0.0;
                    double cttlltr = 0.0, cavgfat = 0.0, cttlgfat = 0.0, cttlamt = 0.0, cavgrate = 0.0, cttlamt1 = 0.0;
                    double snf = 0.0;

//                    for cow
                    {
//                    Total Milk sum
                        {
                            Query query1 = session.createQuery("select sum(s.totalmilk) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cttlltr = Double.parseDouble(s1);
                        }

//                    Total Fat Avg
                        {
                            Query query1 = session.createQuery("select avg(s.fat) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cavgfat = Double.parseDouble(s1);
                        }

                        //                        Average rate
                        {
                            Query query1 = session.createQuery("select avg(s.rate) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cavgrate = Double.parseDouble(s1);
                        }
                        //                                        Total SNF Avg
                        {
                            Query query1 = session.createQuery("select avg(s.degree) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                snf = Double.parseDouble(s1);

                        }



//                    Total GFat sum
//                        {
//                            Query query1 = session.createQuery("select sum(s.gfat) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
//                            String s1 = String.valueOf(query1.uniqueResult());
//                            if (Global.isDouble(s1))
//                                cttlgfat = Double.parseDouble(s1);
//                        }

//                    Total Amount sum
                        {
                            Query query1 = session.createQuery("select sum(s.totalamt) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=1").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cttlamt1 = Double.parseDouble(s1);
                        }
                    }

                    bttlltr = Global.round2(bttlltr);
                    bavgfat = Global.round2(bavgfat);
                    bttlgfat = Global.round2(bavgfat * bttlltr);
                    bavgrate = Global.round2(bavgrate);
                    // bttlamt = Global.round2(bttlltr * bavgrate);
                    bttlamt = Global.round2(bttlamt1);

                    cttlltr = Global.round2(cttlltr);
                    cavgfat = Global.round2(cavgfat);
                    cttlgfat = Global.round2(cttlltr * cavgfat);
                    cavgrate = Global.round2(cavgrate);
                    //cttlamt = Global.round2(cttlltr * cavgrate);
                    cttlamt = Global.round2(cttlamt1);

                    bp.setBltr(bttlltr);
                    bp.setBfat(bavgfat);
                    bp.setBgfat(bttlgfat);
                    bp.setBttl(bttlamt1);

                    bp.setCltr(cttlltr);
                    bp.setCfat(cavgfat);

                    bp.setCsnf(snf);
                    bp.setCgfat(cttlgfat);
                    bp.setCttl(cttlamt1);

                    gttlgfat = gttlgfat + Global.round2(bttlgfat + cttlgfat);
                    gttlamount = gttlamount + Global.round2(bttlamt1 + cttlamt1);



                    gcttlltr = gcttlltr + cttlltr;
                    gcttlgfat = gcttlgfat + cttlgfat;
                    gcttlamt = gcttlamt + cttlamt1;
                    gcttlsnf = gcttlsnf + snf;

                    bplist.add(bp);
                }

//              for Evening
                {
                    VTable.BillProd bp = new VTable.BillProd();
                    bp.setSrno(i++);
                    bp.setDate(sd);
                    bp.setStrdate(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(sd));

                    double bttlltr = 0.0, bavgfat = 0.0, bttlgfat = 0.0, bttlamt = 0.0, bavgrate = 0.0, bttlamt1 = 0.0;
                    double cttlltr = 0.0, cavgfat = 0.0, cttlgfat = 0.0, cttlamt = 0.0, cavgrate = 0.0, cttlamt1 = 0.0;;
                    double snf =0.0;
//

//                    for cow
                    {
//                    Total Milk sum
                        {
                            Query query1 = session.createQuery("select sum(s.totalmilk) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cttlltr = Double.parseDouble(s1);
                        }

//                    Total Fat Avg
                        {
                            Query query1 = session.createQuery("select avg(s.fat) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cavgfat = Double.parseDouble(s1);
                        }
//                                        Total SNF Avg
                        {
                            Query query1 = session.createQuery("select avg(s.degree) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                snf = Double.parseDouble(s1);

                        }


//                    Total rate
                        {
                            Query query1 = session.createQuery("select avg(s.rate) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cavgrate = Double.parseDouble(s1);
                        }

//                    Total GFat sum
//                        {
//                            Query query1 = session.createQuery("select sum(s.gfat) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
//                            String s1 = String.valueOf(query1.uniqueResult());
//                            if (Global.isDouble(s1))
//                                cttlgfat = Double.parseDouble(s1);
//                        }

//                    Total Amount sum
                        {
                            Query query1 = session.createQuery("select sum(s.totalamt) from CowMilk s where s.date=:id and s.d=1 and s.customer.id=:id1 and s.me=0").setParameter("id", sd).setParameter("id1", cid);
                            String s1 = String.valueOf(query1.uniqueResult());
                            if (Global.isDouble(s1))
                                cttlamt1 = Double.parseDouble(s1);
                        }
                    }

                    bttlltr = Global.round2(bttlltr);
                    bavgfat = Global.round2(bavgfat);
                    bttlgfat = Global.round2(bttlltr * bavgfat);
                    bavgrate = Global.round2(bavgrate);
//                    bttlamt = Global.round2(bttlgfat * bavgrate);
                    bttlamt = Global.round2(bttlltr * bavgrate);


                    cttlltr = Global.round2(cttlltr);
                    cavgfat = Global.round2(cavgfat);
                    cttlgfat = Global.round2(cttlltr * cavgfat);
                    cavgrate = Global.round2(cavgrate);
//                    cttlamt = Global.round2(cttlgfat * cavgrate);
                    cttlamt = Global.round2(cttlltr * cavgrate);

                    bp.setBltr(bttlltr);
                    bp.setBfat(bavgfat);
                    bp.setBgfat(bttlgfat);
                    bp.setBttl(bttlamt1);

                    bp.setCltr(cttlltr);
                    bp.setCfat(cavgfat);
                    bp.setCsnf(snf);
                    bp.setCgfat(cttlgfat);
                    bp.setCttl(cttlamt1);

                    gttlgfat = gttlgfat + (bttlgfat + cttlgfat);
                    gttlamount = gttlamount + (bttlamt1 + cttlamt1);

                    gcttlltr = gcttlltr + cttlltr;
                    gcttlgfat = gcttlgfat + cttlgfat;
                    gcttlamt = gcttlamt + cttlamt1;
                    gcttlsnf = gcttlsnf + snf;

                    bplist.add(bp);
                }

                sd = sd.plusDays(1);
            }

            transaction.commit();
            session.close();
            return new JRBeanCollectionDataSource(bplist, true);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

    private Map<String, Object> getReportParameter(String sd, String ed, String cno, String cname, double ttlmilk, double ttlamount, String hafta, double paid,  double cttlltr, double cttlgfat, double cttlamt, double cttlsnf)
    {
        try
        {
            java.util.HashMap map = new java.util.HashMap(1);
            map.put("sdate",""+sd);
            map.put("edate",""+ed);
            map.put("cno",""+cno);
            map.put("cname",""+cname);
            map.put("ttlltr",""+Global.round2(ttlmilk));
            map.put("ttlamount",""+Global.round2(ttlamount));
            map.put("hafta",""+hafta);
            map.put("paid",""+Global.round2(paid));

            map.put("cttl",""+Global.round2(cttlltr)+"           "+Global.round2(cttlgfat)+"           "+Global.round2(cttlsnf)+"          "+Global.round2(cttlamt));
            return map;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void createReport(List<Map<String, Object>> paramList, List<JRDataSource> sourceList)
    {
        try
        {
            JasperPrint jasperPrint = null;
            final String resourcePath = "/Design/invoicesp.jasper";
            InputStream reportStream = this.getClass().getResourceAsStream(resourcePath);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            Map<String, Object> parameters = paramList.get(0);
            JRDataSource datasource = sourceList.get(0);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);

            if (paramList.size() > 1)
            {
                for (int i = 1; i < paramList.size(); i++)
                {
                    JasperPrint jasperPrint_next = JasperFillManager.fillReport(jasperReport, paramList.get(i), sourceList.get(i));
                    List pages = jasperPrint_next.getPages();
                    for (int j = 0; j < pages.size(); j++)
                    {
                        JRPrintPage object = (JRPrintPage) pages.get(j);
                        jasperPrint.addPage(object);
                    }
                }
            }

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle("Invoice");
            jasperViewer.setVisible(true);
            JasperPrintManager.printReport(jasperPrint, true);

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    //    print report
    private void printReport()
    {
        LocalDate sd = sdate.getValue();
        LocalDate ed = edate.getValue();
        String sd1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(sd);
        String ed1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(ed);
        if(data.size() > 0 && sd != null && ed != null)
        {
            List<Map<String, Object>> ParamList = new ArrayList<Map<String, Object>>();
            List<JRDataSource> SourceList = new ArrayList<JRDataSource>();

            SourceList.add(getReportTableData1());
            ParamList.add(getReportParameter1(sd1, ed1));
            try
            {
                createReport1(ParamList, SourceList);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something went wrong..!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid dates..!!");
            alert.showAndWait();
        }
    }

    private JRDataSource getReportTableData1()
    {
        try
        {
            List<VTable.Bill> bplist = new ArrayList();
            int j = 1;
            for (int i = 0; i < tbl.getItems().size(); i++)
            {
                VTable.Bill b = new Bill();
                b.setSrno(j++);
                b.setCno(tbl.getColumns().get(1).getCellData(i).toString());
                b.setCname(tbl.getColumns().get(2).getCellData(i).toString());
                b.setTtlmilk(Double.parseDouble(tbl.getColumns().get(3).getCellData(i).toString()));
                b.setTtlgfat(Double.parseDouble(tbl.getColumns().get(4).getCellData(i).toString()));
                b.setTtlamount(Double.parseDouble(tbl.getColumns().get(5).getCellData(i).toString()));
                b.setHafta(tbl.getColumns().get(6).getCellData(i).toString());
                b.setPaid(Double.parseDouble(tbl.getColumns().get(7).getCellData(i).toString()));
                b.setSign("");
                bplist.add(b);
            }
            return new JRBeanCollectionDataSource(bplist, true);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

    private Map<String, Object> getReportParameter1(String sd, String ed)
    {
        try
        {
            java.util.HashMap map = new java.util.HashMap(1);
            map.put("sdate",""+sd);
            map.put("edate",""+ed);
            return map;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void createReport1(List<Map<String, Object>> paramList, List<JRDataSource> sourceList)
    {
        try
        {
            JasperPrint jasperPrint = null;
            final String resourcePath = "/Design/invoice1.jasper";
            InputStream reportStream = this.getClass().getResourceAsStream(resourcePath);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            Map<String, Object> parameters = paramList.get(0);
            JRDataSource datasource = sourceList.get(0);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);

            if (paramList.size() > 1)
            {
                for (int i = 1; i < paramList.size(); i++)
                {
                    JasperPrint jasperPrint_next = JasperFillManager.fillReport(jasperReport, paramList.get(i), sourceList.get(i));
                    List pages = jasperPrint_next.getPages();
                    for (int j = 0; j < pages.size(); j++)
                    {
                        JRPrintPage object = (JRPrintPage) pages.get(j);
                        jasperPrint.addPage(object);
                    }
                }
            }

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle("Invoice");
            jasperViewer.setVisible(true);
            JasperPrintManager.printReport(jasperPrint, true);

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    //    for single invoice
    private void printSingleInvoice(String cno, String cname, String hafta)
    {
        LocalDate sd = sdate.getValue();
        LocalDate ed = edate.getValue();
        String sd1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(sd);
        String ed1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(ed);

        List<Map<String, Object>> ParamList = new ArrayList<Map<String, Object>>();
        List<JRDataSource> SourceList = new ArrayList<JRDataSource>();
        SourceList.add(getReportTableData(sd, ed, cno, cname));
        ParamList.add(getReportParameter(sd1, ed1, cno, cname, gttlgfat, gttlamount, hafta, Global.round2(gttlamount - Double.parseDouble(hafta)), gcttlltr, gcttlgfat, gcttlamt, gcttlsnf));
        try
        {
            createReport(ParamList, SourceList);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    private class ButtonCell extends TableCell<Disposer.Record, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonCell(){
            cellButton.setOnAction(t ->
            {
                Bill currentPerson = (Bill) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                data.remove(currentPerson);
                setTableBottomLabel();
            });
        }
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
            else{
                setGraphic(null);
            }
        }
    }

    private class ButtonCell1 extends TableCell<Disposer.Record, Boolean> {
        final Button cellButton = new Button("Print");

        ButtonCell1(){
            cellButton.setOnAction(t ->
            {
                Bill currentPerson = (Bill) ButtonCell1.this.getTableView().getItems().get(ButtonCell1.this.getIndex());
                printSingleInvoice(currentPerson.getCno(), currentPerson.getCname(), currentPerson.getHafta());
            });
        }
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
            else{
                setGraphic(null);
            }
        }
    }

}

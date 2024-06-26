package Controller;

import DB.Customer;
import DB.CustomerPaymentCredit;
import DB.CustomerPaymentDeposite;
import DB.Global;
import VTable.CreditDebit;
import com.sun.prism.impl.Disposer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountActivity
{
    @FXML
    private Button btnExport;
    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker sdate, edate;
    @FXML
    private TableView<CreditDebit> tbl;
    @FXML
    private ComboBox<String> comboCname;
    @FXML
    private TableColumn<CreditDebit, Integer> tsrno;
    @FXML
    private TableColumn<CreditDebit, LocalDate> tdate;
    @FXML
    private TableColumn<CreditDebit, String> tcno, tcname, tremark, ttype;
    @FXML
    private TableColumn<CreditDebit, Double> tamount, tpending, tdeposited;
    @FXML
    private ObservableList<String> custList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<CreditDebit> data = FXCollections.observableArrayList();
    @FXML
    private FilteredList<CreditDebit> filteredData;
    @FXML
    private Label lblaccountactivity;

    public void initialize()
    {
        if(Global.lang)
        {
            lblaccountactivity.setText("View Customer Account Activity");
            sdate.setPromptText("From");
            edate.setPromptText("Up To");
            comboCname.setPromptText("--Select--");
            txtSearch.setPromptText("Search");
            btnExport.setText("Export");
            tsrno.setText("Sr.No.");
            tdate.setText("Date");
            ttype.setText("Type");
            tcno.setText("Customer No.");
            tcname.setText("Customer Name");
            tremark.setText("Remark");
            tamount.setText("Amount");
            tpending.setText("Pending");
            tdeposited.setText("Deposited");
        }
        setData();
        new AutoCompleteComboBoxListener<String>(comboCname);

        tcno.setCellValueFactory(new PropertyValueFactory<>("cno"));
        tsrno.setCellValueFactory(new PropertyValueFactory<>("srno"));
        tdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        ttype.setCellValueFactory(new PropertyValueFactory<>("activity"));
        tcname.setCellValueFactory(new PropertyValueFactory<>("cname"));
        tremark.setCellValueFactory(new PropertyValueFactory<>("remark"));
        tamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tpending.setCellValueFactory(new PropertyValueFactory<>("pending"));
        tdeposited.setCellValueFactory(new PropertyValueFactory<>("deposite"));

        TableColumn col_action = new TableColumn<>("Delete");
        tbl.getColumns().add(col_action);
        col_action.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
        col_action.setCellFactory((Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>) p -> new ButtonCell());

        sdate.setOnAction(event -> setTableData());

        edate.setOnAction(event -> setTableData());

        comboCname.setOnAction(event -> setTableData());

        filteredData = new FilteredList<>(data, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getCname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (customer.getCno().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (customer.getActivity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        txtSearch.setOnKeyReleased(event -> setSearchData());

        tbl.setItems(data);

        btnExport.setOnAction(event -> {
            try {
                Workbook workbook = new HSSFWorkbook();
                Sheet spreadsheet = workbook.createSheet("सभासद खाते क्रियाकलाप यादी");
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(0);
                for (int j = 0; j < tbl.getColumns().size(); j++) {
                    row.createCell(j).setCellValue(tbl.getColumns().get(j).getText());
                }
                for (int i = 0; i < tbl.getItems().size(); i++) {
                    row = spreadsheet.createRow(i + 1);
                    for (int j = 0; j < tbl.getColumns().size(); j++) {
                        if(tbl.getColumns().get(j).getCellData(i) != null) {
                            row.createCell(j).setCellValue(tbl.getColumns().get(j).getCellData(i).toString());
                        }
                        else {
                            row.createCell(j).setCellValue("");
                        }
                    }
                }
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("सेव्ह करण्यासाठी फाईल निर्दिष्ट करा..");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File (*.xls)", "*.xls");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(new Stage());
                if (file != null) {
                    try {
                        FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
                        workbook.write(fileOut);
                        fileOut.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            catch (Exception ignored)
            {
            }
        });

    }

    private void setSearchData()
    {
        SortedList<CreditDebit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbl.comparatorProperty());
        tbl.setItems(sortedData);
    }

    private void setTableData()
    {
        try
        {
            tbl.getItems().clear();
            data.clear();
            LocalDate sd = sdate.getValue();
            LocalDate ed = edate.getValue();
            String cname = comboCname.getSelectionModel().getSelectedItem();
            long cid = getCustomerId(cname);
            List<DB.CustomerPaymentDeposite> cpdlist = new ArrayList<>();
            List<DB.CustomerPaymentCredit> cpclist = new ArrayList<>();
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            if(sd != null && ed != null)
            {
                if(cid != 0)
                {
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date>=:id and s.date<=:id1 and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id1", ed).setParameter("id2", cid).list();
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date>=:id and s.date<=:id1 and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id1", ed).setParameter("id2", cid).list();
                }
                else
                {
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date>=:id and s.date<=:id1 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id1", ed).list();
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date>=:id and s.date<=:id1 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id1", ed).list();
                }
            }
            else if(sd != null)
            {
                if(cid != 0)
                {
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date=:id and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id2", cid).list();
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date=:id and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", sd).setParameter("id2", cid).list();
                }
                else
                {
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date=:id and s.d=1 order by s.date asc").setParameter("id", sd).list();
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date=:id and s.d=1 order by s.date asc").setParameter("id", sd).list();
                }
            }
            else if(ed != null)
            {
                if(cid != 0)
                {
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date=:id and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", ed).setParameter("id2", cid).list();
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date=:id and s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id", ed).setParameter("id2", cid).list();
                }
                else
                {
                    cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.date=:id and s.d=1 order by s.date asc").setParameter("id", ed).list();
                    cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.date=:id and s.d=1 order by s.date asc").setParameter("id", ed).list();
                }
            }
            else if(cid != 0)
            {
                cpclist = session.createQuery("from DB.CustomerPaymentCredit s where s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id2", cid).list();
                cpdlist = session.createQuery("from DB.CustomerPaymentDeposite s where s.customer.id=:id2 and s.d=1 order by s.date asc").setParameter("id2", cid).list();
            }

            int i = 1;
            Iterator<CustomerPaymentDeposite> iterator = cpdlist.iterator();
            Iterator<CustomerPaymentCredit> iterator1 = cpclist.iterator();
            CustomerPaymentDeposite d = null;
            CustomerPaymentCredit c = null;
            for ( ; iterator.hasNext() || iterator1.hasNext();  )
            {
                if(iterator.hasNext())
                    d = iterator.next();
                else
                    d = null;
                if(iterator1.hasNext())
                    c = iterator1.next();
                else
                    c = null;

                if(d != null && c != null)
                {
                    if(d.getDate().isAfter(c.getDate()))
                    {
                        CreditDebit cd = new CreditDebit();
                        cd.setSrno(i++);
                        cd.setDate(c.getDate());
                        cd.setActivity("Credit");
                        cd.setCno(c.getCustomer().getSno());
                        cd.setCname(c.getCustomer().getName());
                        cd.setRemark(c.getRemark());
                        cd.setAmount(c.getAmount());
                        cd.setPending(c.getCredit());
                        cd.setDeposite(c.getDeposite());
                        cd.setId(c.getId());
                        cd.setCid(c.getCustomer().getId());
                        data.add(cd);
                    }
                    else
                    {
                        CreditDebit cd = new CreditDebit();
                        cd.setSrno(i++);
                        cd.setDate(d.getDate());
                        cd.setActivity("Debit");
                        cd.setCno(d.getCustomer().getSno());
                        cd.setCname(d.getCustomer().getName());
                        cd.setRemark(d.getRemark());
                        cd.setAmount(d.getAmount());
                        cd.setPending(d.getCredit());
                        cd.setDeposite(d.getDeposite());
                        cd.setId(d.getId());
                        cd.setCid(d.getCustomer().getId());
                        data.add(cd);
                    }
                }
                else if(d != null)
                {
                    CreditDebit cd = new CreditDebit();
                    cd.setSrno(i++);
                    cd.setDate(d.getDate());
                    cd.setActivity("Debit");
                    cd.setCno(d.getCustomer().getSno());
                    cd.setCname(d.getCustomer().getName());
                    cd.setRemark(d.getRemark());
                    cd.setAmount(d.getAmount());
                    cd.setPending(d.getCredit());
                    cd.setDeposite(d.getDeposite());
                    cd.setId(d.getId());
                    cd.setCid(d.getCustomer().getId());
                    data.add(cd);
                }
                else if(c != null)
                {
                    CreditDebit cd = new CreditDebit();
                    cd.setSrno(i++);
                    cd.setDate(c.getDate());
                    cd.setActivity("Credit");
                    cd.setCno(c.getCustomer().getSno());
                    cd.setCname(c.getCustomer().getName());
                    cd.setRemark(c.getRemark());
                    cd.setAmount(c.getAmount());
                    cd.setPending(c.getCredit());
                    cd.setDeposite(c.getDeposite());
                    cd.setId(c.getId());
                    cd.setCid(c.getCustomer().getId());
                    data.add(cd);
                }
                else
                    break;
            }

            transaction.commit();
            session.close();
            tbl.setItems(data);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            tbl.getItems().clear();
            data.clear();
        }
    }

    private long getCustomerId(String cname)
    {
        if(cname.equals(""))
            return 0;
        else if(custList.contains(cname))
        {
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1").setParameter("id", cname).uniqueResult();
            if(customer != null)
            {
                transaction.commit();
                session.close();
                return customer.getId();
            }
            transaction.commit();
            session.close();
            return 0;
        }
        return 0;
    }

    private void setData()
    {
        Session session = Global.getSession();
        Transaction transaction = session.beginTransaction();
        List<Customer> clist = session.createQuery("from DB.Customer s where s.d=1").list();
        for(DB.Customer c : clist)
            custList.add(c.getName());
        comboCname.getItems().addAll(custList);
        transaction.commit();
        session.close();
    }

    public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent>
    {

        private ComboBox comboBox;
        private StringBuilder sb;
        private ObservableList<T> data;
        private boolean moveCaretToPos = false;
        private int caretPos;
        public AutoCompleteComboBoxListener(final ComboBox comboBox) {
            this.comboBox = comboBox;
            sb = new StringBuilder();
            data = comboBox.getItems();
            this.comboBox.setEditable(true);
            this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    comboBox.hide();
                }
            });
            this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
        }
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.UP) {
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            } else if(event.getCode() == KeyCode.DOWN) {
                if(!comboBox.isShowing()) {
                    comboBox.show();
                }
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            } else if(event.getCode() == KeyCode.BACK_SPACE) {
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
            } else if(event.getCode() == KeyCode.DELETE) {
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
            }
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                    || event.isControlDown() || event.getCode() == KeyCode.HOME
                    || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                return;
            }
            ObservableList list = FXCollections.observableArrayList();
            for (int i=0; i<data.size(); i++) {
                if(data.get(i).toString().toLowerCase().startsWith(
                        AutoCompleteComboBoxListener.this.comboBox
                                .getEditor().getText().toLowerCase())) {
                    list.add(data.get(i));
                }
            }
            String t = comboBox.getEditor().getText();
            comboBox.setItems(list);
            comboBox.getEditor().setText(t);
            if(!moveCaretToPos) {
                caretPos = -1;
            }
            moveCaret(t.length());
            if(!list.isEmpty()) {
                comboBox.show();
            }
        }
        private void moveCaret(int textLength) {
            if(caretPos == -1) {
                comboBox.getEditor().positionCaret(textLength);
            } else {
                comboBox.getEditor().positionCaret(caretPos);
            }
            moveCaretToPos = false;
        }
    }

    private class ButtonCell extends TableCell<Disposer.Record, Boolean> {
        final Button cellButton = new Button("Delete");
        ButtonCell(){
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent t) {
                    VTable.CreditDebit currentPerson = (VTable.CreditDebit) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                    data.remove(currentPerson);

                    Session session = Global.getSession();
                    Transaction transaction = session.beginTransaction();
                    DB.CustomerPaymentDeposite cpc = (CustomerPaymentDeposite) session.createQuery("from DB.CustomerPaymentDeposite s where s.id=:id and s.d=1").setParameter("id", currentPerson.getId()).uniqueResult();
                    cpc.setD(0);
                    DB.Customer customer = cpc.getCustomer();
                    double deposited1 = customer.getDeposite();
                    double pending1 = customer.getPending() + cpc.getAmount();
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
                    transaction.commit();
                    session.close();
                }
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

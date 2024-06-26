package Controller;

import DB.Customer;
import DB.CustomerPaymentCredit;
import DB.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class AddCredit
{
    @FXML
    private DatePicker date;
    @FXML
    private Label lblWarn;
    @FXML
    private TextArea txtRemark;
    @FXML
    private TextField txtCno, txtAmount;
    @FXML
    private Button btnAdd, btnReset;
    @FXML
    private ComboBox<String> comboCname;
    @FXML
    private Label lblCno, lblPending, lblDeposited, custcredit, lbldate, lblcustno, lblcustname, lblcomment, lblamt;
    @FXML
    private ObservableList<String> data = FXCollections.observableArrayList();
    private double pending, deposited;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            custcredit.setText("Customer Credit");
            lbldate.setText("Date :");
            lblcustno.setText("Customer No. :");
            lblcustname.setText("Customer Name :");
            lblcomment.setText("Remark :");
            lblamt.setText("Amount :");
            lblPending.setText("Pending :");
            lblDeposited.setText("Deposited :");
            btnAdd.setText("Add");
            btnReset.setText("Reset");
            date.setPromptText("xx-yy-zzzz");
            txtCno.setPromptText("Customer No.");
            comboCname.setPromptText("Customer Name");
            txtRemark.setPromptText("Remark");
            txtAmount.setPromptText("Amount");
        }
        try
        {
            lblWarn.setTextFill(Paint.valueOf(Global.labelColor1));
            lblCno.setTextFill(Paint.valueOf(Global.labelColor1));
            lblPending.setTextFill(Paint.valueOf(Global.labelColor1));
            lblDeposited.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }

        new AutoCompleteComboBoxListener<String>(comboCname);
        date.setValue(LocalDate.now());

        setData();
        resetData();

        btnReset.setOnAction(event -> resetData());

        comboCname.setOnAction(event -> checkComboCustomer());

        btnAdd.setOnAction(event -> {
            long cid = checkCustomer();
            if(cid != 0 && Global.isDouble(txtAmount.getText()))
            {
                double amt = Double.parseDouble(txtAmount.getText());
                double pending1 = pending + amt;
                double deposited1 = deposited;
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
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.id=:id and s.d=1 and s.status=1").setParameter("id", cid).uniqueResult();
                customer.setPending(pending1);
                customer.setDeposite(deposited1);
                session.update(customer);

                DB.CustomerPaymentCredit c = new CustomerPaymentCredit();
                c.setAmount(amt);
                c.setCustomer(customer);
                c.setCredit(pending1);
                c.setDeposite(deposited1);
                c.setDate(date.getValue());
                c.setRemark(txtRemark.getText());
                c.setD(1);
                session.persist(c);

                transaction.commit();
                session.close();

                resetData();
                if (Global.lang)
                    lblWarn.setText("Money raised from customer account .. !!");
                else
                    lblWarn.setText("सभासद खात्यातून पैसे जमा झाले..!!");
            }
            if(Global.lang)
                lblWarn.setText("Please enter valid details..!!");
            else
                lblWarn.setText("कृपया वैध तपशील प्रविष्ट करा..!!");
        });

    }

    private long checkCustomer()
    {
        try
        {
            long cno = 0;
            String cname = txtCno.getText();
            if(!cname.equals(""))
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.sno=:id and s.d=1 and s.status=1").setParameter("id", cname).uniqueResult();
                if(customer != null)
                    cno = customer.getId();
                else
                    cno = 0;
                transaction.commit();
                session.close();
            }
            else
                cno = 0;
            return cno;
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    private void checkComboCustomer()
    {
        try
        {
            String cname = comboCname.getSelectionModel().getSelectedItem();
            if(!cname.equals(""))
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1 and s.status=1").setParameter("id", cname).uniqueResult();
                if(customer != null)
                    lblCno.setText(customer.getSno());
                else
                    lblCno.setText("");
                transaction.commit();
                session.close();
            }
            else
                lblCno.setText("");
        }
        catch (Exception e)
        {
            lblCno.setText("");
        }
    }

    private void resetData()
    {
        txtCno.setText("");
        comboCname.setValue("");
        txtRemark.setText("");
        txtAmount.setText("");
        if(Global.lang)
            lblPending.setText("प्रलंबित : 0.0");
        else
            lblPending.setText("Pending : 0.0");
        if(Global.lang)
            lblDeposited.setText("जमा : 0.0");
        else
            lblDeposited.setText("Deposited : 0.0");
        lblCno.setText("");
        pending = 0.0;
        deposited = 0.0;
    }

    private void setData()
    {
        Session session = Global.getSession();
        Transaction transaction = session.beginTransaction();
        List<Customer> clist = session.createQuery("from DB.Customer s where s.d=1 and s.status=1").list();
        for(DB.Customer customer : clist)
        {
            data.add(customer.getName());
        }
        transaction.commit();
        session.close();
        comboCname.getItems().addAll(data);
    }

    public void onCnoChanged(KeyEvent keyEvent)
    {
        try
        {
            lblCno.setText("");
            String cname = txtCno.getText();
            if(!cname.equals(""))
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.sno=:id and s.d=1 and s.status=1").setParameter("id", cname).uniqueResult();
                if(customer != null)
                {
                    comboCname.setValue(customer.getName());
                    lblPending.setText("प्रलंबित : "+customer.getPending());
                    lblDeposited.setText("जमा : "+customer.getDeposite());
                    pending = customer.getPending();
                    deposited = customer.getDeposite();
                }
                else
                {
                    comboCname.setValue("");
                    lblPending.setText("प्रलंबित : 0.0");
                    lblDeposited.setText("जमा : 0.0");
                    pending = 0.0;
                    deposited = 0.0;
                }
                transaction.commit();
                session.close();
            }
            else
            {
                comboCname.setValue("");
                lblPending.setText("प्रलंबित : 0.0");
                lblDeposited.setText("जमा : 0.0");
                pending = 0.0;
                deposited = 0.0;
            }
        }
        catch (Exception e)
        {
            comboCname.setValue("");
            lblPending.setText("प्रलंबित : 0.0");
            lblDeposited.setText("जमा : 0.0");
            pending = 0.0;
            deposited = 0.0;
        }
        setAmount();
    }

    private void setAmount()
    {
        if(Global.isDouble(txtAmount.getText()))
        {
            double pending1 = pending + Double.parseDouble(txtAmount.getText());
            double deposited1 = deposited;
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
            lblPending.setText("प्रलंबित : "+Global.round2(pending1));
            lblDeposited.setText("जमा : "+Global.round2(deposited1));
        }
        else
        {
            lblPending.setText("प्रलंबित : "+pending);
            lblDeposited.setText("जमा : "+deposited);
        }
    }

    public void onAmountChanged(KeyEvent keyEvent)
    {
        setAmount();
    }

    private class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent>
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
            }
            else if(event.getCode() == KeyCode.DOWN) {
                if(!comboBox.isShowing()) {
                    comboBox.show();
                }
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            }
            else if(event.getCode() == KeyCode.BACK_SPACE) {
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

}

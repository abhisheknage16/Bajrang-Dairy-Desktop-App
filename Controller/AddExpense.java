package Controller;

import DB.Expense;
import DB.Global;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class AddExpense {

    @FXML
    private Button btnAdd, btnReset;
    @FXML
    private DatePicker date;
    @FXML
    private TextField txtEname, txtTtl;
    @FXML
    private Label lblWarn, lblTitle, lblDate, lblname, lblTotal;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblTitle.setText("Add Expense");
            lblDate.setText("Date :");
            lblname.setText("Expense Name :");
            lblTotal.setText("Total :");
            date.setPromptText("xx-yy-zzzz");
            txtEname.setPromptText("Expense Name");
            txtTtl.setPromptText("Total");
            btnAdd.setText("Add");
            btnReset.setText("Reset");
        }
            date.setValue(LocalDate.now());
            btnReset.setOnAction(event -> {
                date.setValue(LocalDate.now());
                txtEname.setText("");
                txtTtl.setText("0");
                lblWarn.setText("");
            });
            btnAdd.setOnAction(event -> {
                try {
                    String ename1 = txtEname.getText();
                    if (ename1.equals(""))
                        if(!Global.lang)
                            lblWarn.setText("कृपया खर्च प्रविष्ट करा..!!");
                        else
                            lblWarn.setText("Please enter Expense..!!");
                    else if(!Global.isDouble(txtTtl.getText()))
                        if (!Global.lang)
                            lblWarn.setText("कृपया वैध एकूण प्रविष्ट करा..!!");
                        else
                            lblWarn.setText("Please enter Total Expense..!!");
                    else {
                        double ttl1 = Double.parseDouble(txtTtl.getText());
                        LocalDate date1 = date.getValue();
                        Session session = Global.getSession();
                        Transaction transaction = session.beginTransaction();
                        Expense expense = new Expense();
                        expense.setName(ename1);
                        expense.setDate(date1);
                        expense.setTtl(ttl1);
                        expense.setD(1);
                        session.persist(expense);
                        transaction.commit();
                        if (!Global.lang)
                            lblWarn.setText("खर्च जोडला..!!");
                        else
                            lblWarn.setText("Expense Added..!!");
                    }
                }
                catch (Exception e)
                {
                    if (!Global.lang)
                        lblWarn.setText("कृपया पुन्हा प्रयत्न करा..!!");
                    else
                        lblWarn.setText("Please try again..!!");
                }
            });
    }
}

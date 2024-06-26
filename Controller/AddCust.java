package Controller;

import DB.Global;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class AddCust
{
    @FXML
    private DatePicker date;
    @FXML
    private TextField cno, cname, mobile, accno, ifsc, txtHafta;
    @FXML
    private TextArea addr;
    @FXML
    private RadioButton rbtnclose, rbtnopen;
    @FXML
    private Button add, reset;
    @FXML
    private Label warn;
    @FXML
    private Label lblcust, lbldate, lblcustno, lblcustname, lblcustaddr, lblcustmob, lblcustacc, lblcustifsc, lblweek;
    @FXML
    private Label lblstatus;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblcust.setText("Add Customer");
            lbldate.setText("Date :");
            lblcustno.setText("Customer No. :");
            lblcustname.setText("Customer Name :");
            add.setText("Add");
            reset.setText("Reset");
            lblcustaddr.setText("Address :");
            lblcustmob.setText("Mobile No. :");
            lblcustacc.setText("Account No. :");
            lblcustifsc.setText("IFSC Code :");
            lblweek.setText("HAFTA :");
            lblstatus.setText("Status :");
            rbtnclose.setText("Close");
            rbtnopen.setText("Open");
            date.setPromptText("xx-yy-zzzz");
            cno.setPromptText("Customer No.");
            cname.setPromptText("Customer Name");
            addr.setPromptText("Address");
            mobile.setPromptText("Mobile No.");
            accno.setPromptText("Account No.");
            ifsc.setPromptText("IFSC Code");
            txtHafta.setPromptText("HAFTA");
        }

        try
        {
            warn.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }

        date.setValue(LocalDate.now());

        reset.setOnAction(event -> {
            cname.setText("");
            mobile.setText("");
            accno.setText("");
            ifsc.setText("");
            addr.setText("");
            warn.setText("");
            txtHafta.setText("");
        });

        add.setOnAction(event -> {

            String cname1 = cname.getText();
            String accno1 = accno.getText();
            String ifsc1 = ifsc.getText();
            String addr1 = addr.getText();
            String cno1 = cno.getText();
            String mobile1 = mobile.getText();
            LocalDate date1 = date.getValue();

            if(cname1.equals(""))
                if (!Global.lang)
                    warn.setText("कृपया वैध सभासद नाव प्रविष्ट करा...!!");
                else
                    warn.setText("Please Select valid Customer..!!");
            else
            {
                try
                {
                    Session session = DB.Global.getSession();
                    Transaction transaction = session.beginTransaction();
                    DB.Customer customer = new DB.Customer();
                    customer.setName(cname1);
                    customer.setMob(mobile1);
                    customer.setSno(cno1);
                    customer.setAddr(addr1);
                    customer.setAccno(accno1);
                    customer.setIfsc(ifsc1);
                    customer.setDate(date1);

                    if (rbtnopen.isSelected())
                        customer.setStatus(1);
                    else if (rbtnclose.isSelected())
                        customer.setStatus(0);
                    customer.setPending(0.0);
                    customer.setDeposite(0.0);
                    if(Global.isDouble(txtHafta.getText()))
                        customer.setHafta(Double.parseDouble(txtHafta.getText()));
                    customer.setD(1);
                    session.persist(customer);

                    transaction.commit();
                    session.close();
                    if (!Global.lang)
                        warn.setText("सभासद यशस्वीरित्या जोडले...!!");
                    else
                        warn.setText("Customer Added Successfully..!!");
                }
                catch (Exception e)
                {
                    if (!Global.lang)
                        warn.setText("काहीतरी चूक झाली...!!");
                    else
                        warn.setText("Something went wrong..!!");
                }
            }
        });
    }
}

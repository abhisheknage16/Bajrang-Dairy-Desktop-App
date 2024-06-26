package Controller;
import DB.CowMilk;
import DB.Customer;
import DB.Global;
import DB.Rate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CowMilkPurchase
{

    @FXML
    private DatePicker date;
    @FXML
    private RadioButton rdbtnMorning, rdbtnEvening;
    @FXML
    private TextField txtCustNo, txtMilk, txtDegree, txtFAT, txtRate, txtTotalAmt;
    @FXML
    private ComboBox<String> txtCustName;
    @FXML
    private Button btnAdd, btnPrint, btnReset;
    @FXML
    private Label warn, lblTitle, lbldate, lbltime, lblcno, lblcustname, lblmilk, lblfat, lbldeg,  lblrate, lbltotalamount;
    @FXML
    private ObservableList<String> data = FXCollections.observableArrayList();

    private double fixFat = 0.0, fixRate = 0.0;

    @FXML
    public void initialize()
    {

        if(Global.lang)
        {
            lblTitle.setText("Cow Milk Purchase");
            lbldate.setText("Date :");
            lbltime.setText("Time :");
            lblcno.setText("Customer No. :");
            lblcustname.setText("Customer Name :");
            lblmilk.setText("Milk :");
            lblfat.setText("Fat :");
            lbldeg.setText("SNF :");
            lblrate.setText("Rate :");
            lbltotalamount.setText("Total Amount :");
            btnAdd.setText("Add");
            btnPrint.setText("Print");
            btnReset.setText("Reset");
            date.setPromptText("xx-yy-zzzz");
            rdbtnEvening.setText("Evening");
            rdbtnMorning.setText("Morning");
            txtCustNo.setPromptText("Customer No.");
            txtCustName.setPromptText("Customer Name");
            txtMilk.setPromptText("in Lit.");
            txtFAT.setPromptText("Fat");
            txtDegree.setPromptText("SNF");

            txtRate.setText("Rate");
            txtTotalAmt.setPromptText("Total Amount");
        }

        try
        {
            warn.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }
        date.setValue(LocalDate.now());

        setData();

        btnAdd.setOnAction(event -> save());

        btnReset.setOnAction(event -> reset());

    }

    private void setData()
    {
        try
        {
            date.setValue(LocalDate.now());
            data.clear();
            txtCustName.getItems().clear();
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            List<Customer> clist = session.createQuery("from DB.Customer s where s.d=1 and s.status=1").list();
            for(DB.Customer customer : clist)
            {
                data.add(customer.getName());
            }
            txtCustName.getItems().addAll(data);

            try
            {
                DB.Rate rate = (Rate) session.createQuery("from DB.Rate s where s.d=1 and s.cur=1 and s.name=:id").setParameter("id", "cow").uniqueResult();
                if( rate!= null)
                {
                    fixFat = rate.getNfat();
                    fixRate = rate.getNrate();
                }
            }
            catch (Exception ignored)
            {
            }
            transaction.commit();
            session.close();
            txtRate.setText(String.valueOf(fixRate));
        }
        catch (Exception ignored)
        {
            txtRate.setText("0.0");
        }
    }

    private void reset()
    {
        txtCustNo.setText("");
        txtCustName.setValue("");
        txtMilk.setText("");
        txtDegree.setText("");
        txtFAT.setText("");

        txtRate.setText(String.valueOf(fixRate));
        txtTotalAmt.setText("");
        warn.setText("");
    }

    private boolean checkCustomer()
    {
        String cname = txtCustName.getSelectionModel().getSelectedItem();
        if(!cname.equals(""))
        {
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1").setParameter("id", cname).uniqueResult();
            if(customer != null)
            {
                transaction.commit();
                session.close();
                return true;
            }
            else
            {
                transaction.commit();
                session.close();
                return false;
            }
        }
        else
            return false;

    }

    private void save()
    {

//        try
//        {
            double fat = Double.parseDouble(txtFAT.getText());
            double degree = Double.parseDouble(txtDegree.getText());
            double milk = Double.parseDouble(txtMilk.getText());


            double rate1 = Double.parseDouble(txtRate.getText());
            double total = Double.parseDouble(txtTotalAmt.getText());

            if(checkCustomer())
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                String cname = txtCustName.getSelectionModel().getSelectedItem();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1").setParameter("id", cname).uniqueResult();
                DB.Rate rate = (Rate) session.createQuery("from DB.Rate s where s.d=1 and s.cur=1 and s.name=:id").setParameter("id", "cow").uniqueResult();

                DB.CowMilk cowMilk = new CowMilk();
                cowMilk.setD(1);
                if(rdbtnMorning.isSelected())
                    cowMilk.setMe(1);
                else
                    cowMilk.setMe(0);
                cowMilk.setCustomer(customer);
                cowMilk.setDate(date.getValue());
                cowMilk.setTime(LocalTime.now());
                cowMilk.setCdegree(rate.getNdegree());
                cowMilk.setCfat(rate.getNfat());
                cowMilk.setCrate(rate.getNrate());

                cowMilk.setRate(rate1);
                cowMilk.setTotalamt(total);
                cowMilk.setTotalmilk(milk);
                cowMilk.setDegree(degree);
                cowMilk.setFat(fat);
                cowMilk.setGfat(0.0);

                session.persist(cowMilk);
                transaction.commit();
                session.close();
                if (!Global.lang)
                    warn.setText("दूध जोडले..!!");
                else
                    warn.setText("Milk Added..!!");
            }
            else
            {
                if (!Global.lang)
                    warn.setText("कृपया वैध सभासद निवडा..!!");
                else
                    warn.setText("Please select valid Customer..!!");
            }
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.toString());
//            if (!Global.lang)
//                warn.setText("कृपया वैध तपशील प्रविष्ट करा..!!");
//            else
//                warn.setText("Please enter correct Data..!!");
//        }
    }

    private void setFinalTotal()
    {
        if(Global.isDouble(txtMilk.getText()) && Global.isDouble(txtFAT.getText())
         && Global.isDouble(txtMilk.getText()))
        {
            double fat = Double.parseDouble(txtFAT.getText());
            double milk = Double.parseDouble(txtMilk.getText());
//            txtGFAT.setText(String.valueOf(Global.round2((fat * milk))));
//            double temp = fat * milk;
            double milk1 = 0.0, degree1 = 0.0;

            double fixRate1 = 0.0;
            if (Global.isDouble(txtMilk.getText()))
                milk1 = Double.parseDouble(txtMilk.getText());
            if(Global.isDouble(txtRate.getText()))
                fixRate1 = Double.parseDouble(txtRate.getText());
            if (Global.isDouble(txtDegree.getText()))
                degree1 = Double.parseDouble(txtDegree.getText());
            txtTotalAmt.setText(String.valueOf(Global.round2(milk1 * fixRate1 )));
        }
    }

    public void onCustChanged(KeyEvent keyEvent)
    {
        String cno = txtCustNo.getText();
        if(!cno.equals(""))
        {
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            try
            {
                List<Customer> clist = session.createQuery("from DB.Customer s where s.sno=:id and s.d=1 and s.status=1").setParameter("id", cno).list();
                if(clist.size() ==1)
                {
                    for(DB.Customer c : clist)
                    {
                        txtCustName.setValue(c.getName());
                    }
                }
                else
                    txtCustName.setValue("");
            }
            catch (Exception ignored)
            {

            }
            transaction.commit();
            session.close();
        }
        else
            txtCustName.setValue("");
    }

    public void onMilkChanged(KeyEvent keyEvent)
    {
        String fat = txtFAT.getText();
        String snf = txtDegree.getText();
        if (Global.isDouble(fat) && Global.isDouble(snf)){
            double fat1 = Double.parseDouble(fat);
            double snf1 = Double.parseDouble(snf);

//            if (fat1>5.5 ){
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();

                DB.CowRate cowRate = (DB.CowRate) session.createQuery("from DB.CowRate s where s.fat=:id and s.d=1 and s.snf=:id1").setParameter("id", fat1).setParameter("id1", snf1).uniqueResult();
                System.out.println("SMN"+cowRate.getSnf()+"fat"+cowRate.getFat());
                if (cowRate!=null)
                    txtRate.setText(String.valueOf(cowRate.getRate()));


                transaction.commit();
                session.close();
//            }else {
//                Session session = Global.getSession();
//                Transaction transaction = session.beginTransaction();
//
//                DB.CowRate cowRate = (DB.CowRate) session.createQuery("from DB.CowRate s where s.fat=:id and s.d=1 and s.snf:=id1").setParameter("id", Double.parseDouble(fat)).setParameter("id1", Double.parseDouble(snf)).uniqueResult();
//                if (cowRate!=null)
//
//                    txtRate.setText(String.valueOf(cowRate.getRate()));
//
//
//                transaction.commit();
//                session.close();
//            }
            setFinalTotal();
        }
    }

    public void onRateChanged(KeyEvent keyEvent)
    {
        setFinalTotal();
    }
}

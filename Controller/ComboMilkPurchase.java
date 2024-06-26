package Controller;

import DB.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created By STN21
 */

public class ComboMilkPurchase
{
    @FXML
    private DatePicker cowDate, buffaloDate;
    @FXML
    private RadioButton cowRdbtnMorning, buffaloRdbtnMorning, cowRdbtnEvening, buffaloRdbtnEvening;
    @FXML
    private TextField txtCowCustNo, txtCowMilk, txtCowFAT, txtCowDegree, txtCowGFAT, txtCowRate;
    @FXML
    private ComboBox<String> comboCowCustName, comboBuffaloCustName;
    @FXML
    private TextField txtCowTotalAmt;
    @FXML
    private Button btnCowAdd, btnCowReset, btnResetAll, btnCowPrint;
    @FXML
    private Label lblCowWarn, lblBuffaloWarn;
    @FXML
    private TextField txtBuffaloCustNo, txtBuffaloMilk, txtBuffaloFAT, txtBuffaloDegree;
    @FXML
    private TextField txtBuffaloGFAT, txtBuffaloRate, txtBuffaloTotalAmt;
    @FXML
    private Button btnBuffaloAdd, btnBuffaloReset, btnBuffaloPrint;
    @FXML
    private Label lbltitle, lblcow, lbldate, lbltime, lblcno, lblcname, lblmilk, lblfat, lbldeg, lblgeneralfat, lblrate, lbltotalamount;
    @FXML
    private Label lblbuffalo, lbldate1, lbltime1, lblcno1, lblcname1, lblmilk1, lblfat1, lbldeg1, lblgeneralfat1, lblrate1, lbltotalamount1;

    @FXML
    private ObservableList<String> data = FXCollections.observableArrayList();

    private double fixFat = 0.0, fixRate = 0.0, fixFat1 = 0.0, fixRate1 = 0.0;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lbltitle.setText("Buy Both Milk");
            lblcow.setText("Cow");
            lbldate.setText("Date :");
            lbltime.setText("Time :");
            lblcno.setText("Customer No. :");
            lblcname.setText("Customer Name :");
            lblmilk.setText("Milk :");
            lblfat.setText("Fat :");
            lbldeg.setText("Degree :");
            lblgeneralfat.setText("General Fat :");
            lblrate.setText("Rate :");
            lbltotalamount.setText("Total Amount :");

            lblbuffalo.setText("Buffalo");
            lbldate1.setText("Date :");
            lbltime1.setText("Time :");
            lblcno1.setText("Customer No. :");
            lblcname1.setText("Customer Name :");
            lblmilk1.setText("Milk :");
            lblfat1.setText("Fat :");
            lbldeg1.setText("Degree :");
            lblgeneralfat1.setText("General Fat :");
            lblrate1.setText("Rate :");
            lbltotalamount1.setText("Total Amount :");

            cowRdbtnMorning.setText("Morning");
            cowRdbtnEvening.setText("Evening");
            buffaloRdbtnEvening.setText("Evening");
            buffaloRdbtnMorning.setText("Morning");

            btnCowAdd.setText("Add");
            btnCowPrint.setText("Print");
            btnCowReset.setText("Reset");
            btnResetAll.setText("Reset All");
            btnBuffaloAdd.setText("Add");
            btnBuffaloPrint.setText("Print");
            btnBuffaloReset.setText("Reset");

            cowDate.setPromptText("xx-yy-zzzz");
            txtCowCustNo.setPromptText("Customer No.");
            txtCowMilk.setPromptText("in Lit.");
            comboCowCustName.setPromptText("--Select--");
            txtCowFAT.setPromptText("Fat");
            txtCowDegree.setPromptText("Degree");
            txtCowGFAT.setPromptText("General Fat");
            txtCowRate.setPromptText("Rate");
            txtCowTotalAmt.setPromptText("Total Amount");

            buffaloDate.setPromptText("xx-yy-zzzz");
            txtBuffaloCustNo.setPromptText("Customer No.");
            txtBuffaloMilk.setPromptText("in Lit.");
            comboBuffaloCustName.setPromptText("--Select--");
            txtBuffaloFAT.setPromptText("Fat");
            txtBuffaloDegree.setPromptText("Degree");
            txtBuffaloGFAT.setPromptText("General Fat");
            txtBuffaloRate.setPromptText("Rate");
            txtBuffaloTotalAmt.setPromptText("Total Amount");
        }
//

        cowDate.setValue(LocalDate.now());
        buffaloDate.setValue(LocalDate.now());

        setData();
        setData1();

        comboResetAll();

        try
        {
            //// code is here

            btnCowReset.setOnAction(event -> cowResetAll());

            btnBuffaloReset.setOnAction(event -> buffaloResetAll());

            btnResetAll.setOnAction(event -> comboResetAll());

            btnCowAdd.setOnAction(event -> cowSave());

            btnBuffaloAdd.setOnAction(event -> buffaloSave());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void comboResetAll()
    {
        cowResetAll();
        buffaloResetAll();
    }

    /////// for cow

    private void setData()
    {
        try
        {
            cowDate.setValue(LocalDate.now());
            data.clear();
            comboCowCustName.getItems().clear();
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            List<Customer> clist = session.createQuery("from DB.Customer s where s.d=1 and s.status=1").list();
            for(DB.Customer customer : clist)
            {
                data.add(customer.getName());
            }
            comboCowCustName.getItems().addAll(data);

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
        }
        catch (Exception ignored)
        {

        }
    }

    private boolean checkCustomer()
    {
        String cname = comboCowCustName.getSelectionModel().getSelectedItem();

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

    private void cowResetAll()
    {
        txtCowCustNo.setText("");
        comboCowCustName.setValue("");
        txtCowMilk.setText("");
        txtCowFAT.setText("");
        txtCowDegree.setText("");
        txtCowGFAT.setText("");
        txtCowRate.setText(String.valueOf(fixRate));
        txtCowTotalAmt.setText("");
        lblCowWarn.setText("");
    }

    private void cowSave()
    {
        try
        {
            double fat = Double.parseDouble(txtCowFAT.getText());
            double gfat = Double.parseDouble(txtCowGFAT.getText());
            double milk = Double.parseDouble(txtCowMilk.getText());
            double degree = 0.0;
            if(Global.isDouble(txtCowDegree.getText()))
                degree = Double.parseDouble(txtCowDegree.getText());
            double rate1 = Double.parseDouble(txtCowRate.getText());
            double total = Double.parseDouble(txtCowTotalAmt.getText());

            if(checkCustomer())
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                String cname = comboCowCustName.getSelectionModel().getSelectedItem();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1").setParameter("id", cname).uniqueResult();
                DB.Rate rate = (Rate) session.createQuery("from DB.Rate s where s.d=1 and s.cur=1 and s.name=:id").setParameter("id", "cow").uniqueResult();

                DB.CowMilk cowMilk = new CowMilk();
                cowMilk.setD(1);
                if(cowRdbtnMorning.isSelected())
                    cowMilk.setMe(1);
                else
                    cowMilk.setMe(0);
                cowMilk.setCustomer(customer);
                cowMilk.setDate(cowDate.getValue());
                cowMilk.setTime(LocalTime.now());
                cowMilk.setCdegree(rate.getNdegree());
                cowMilk.setCfat(rate.getNfat());
                cowMilk.setCrate(rate.getNrate());

                cowMilk.setRate(rate1);
                cowMilk.setTotalamt(total);
                cowMilk.setTotalmilk(milk);
                cowMilk.setDegree(degree);
                cowMilk.setFat(fat);
                cowMilk.setGfat(gfat);

                session.persist(cowMilk);
                transaction.commit();
                session.close();
                if (!Global.lang)
                    lblCowWarn.setText("दूध जोडले..!!");
                else
                    lblCowWarn.setText("Milk Added..!!");
            }
            else
            {
                if (!Global.lang)
                    lblCowWarn.setText("कृपया वैध सभासद निवडा..!!");
                else
                    lblCowWarn.setText("Please Select Valid Customer..!!");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            if (!Global.lang)
                lblCowWarn.setText("कृपया वैध तपशील प्रविष्ट करा..!!");
            else
                lblCowWarn.setText("Please Enter Correct Data..!!");
        }
    }

    @FXML
    void onCustChanged()
    {
        String cno = txtCowCustNo.getText();
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
                        comboCowCustName.setValue(c.getName());
                    }
                }
                else
                    comboCowCustName.setValue("");
            }
            catch (Exception ignored)
            {

            }
            transaction.commit();
            session.close();
        }
        else
            comboCowCustName.setValue("");
    }

    @FXML
    void onMilkChanged()
    {
        setFinalTotal();
    }

    private void setFinalTotal()
    {
        if(Global.isDouble(txtCowMilk.getText()) && Global.isDouble(txtCowFAT.getText()))
        {
            double fat = Double.parseDouble(txtCowFAT.getText());
            double milk = Double.parseDouble(txtCowMilk.getText());
            txtCowGFAT.setText(String.valueOf(Global.round2(fat * milk)));
            double gfat = fat * milk;
            double fixRate1 = 0.0;
            if(Global.isDouble(txtCowRate.getText()))
                fixRate1 = Double.parseDouble(txtCowRate.getText());
            txtCowTotalAmt.setText(String.valueOf(Global.round2(gfat * fixRate1)));
        }
    }

    @FXML
    void onRateChanged()
    {
        setFinalTotal();
    }

    ////// for buffalo
    private void setData1()
    {
        try
        {
            buffaloDate.setValue(LocalDate.now());
            data.clear();
            comboBuffaloCustName.getItems().clear();
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            List<Customer> clist = session.createQuery("from DB.Customer s where s.d=1 and s.status=1").list();
            for(DB.Customer customer : clist)
            {
                data.add(customer.getName());
//                data.add(customer.getSno());
            }
            comboBuffaloCustName.getItems().addAll(data);

            try
            {
                DB.Rate rate = (Rate) session.createQuery("from DB.Rate s where s.d=1 and s.cur=2 and s.name=:id").setParameter("id", "buffalo").uniqueResult();
                if( rate!= null)
                {
                    fixFat1 = rate.getNfat();
                    fixRate1 = rate.getNrate();
                }
            }
            catch (Exception ignored)
            {
            }
            transaction.commit();
            session.close();
        }
        catch (Exception ignored)
        {

        }
    }


    private boolean checkCustomer1()
    {
        String cname = comboBuffaloCustName.getSelectionModel().getSelectedItem();

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

    private void buffaloResetAll()
    {
        txtBuffaloCustNo.setText("");
        comboBuffaloCustName.setValue("");
        txtBuffaloMilk.setText("");
        txtBuffaloFAT.setText("");
        txtBuffaloDegree.setText("");
        txtBuffaloGFAT.setText("");
        txtBuffaloRate.setText(String.valueOf(fixRate1));
        txtBuffaloTotalAmt.setText("");
        lblBuffaloWarn.setText("");
    }

    private void buffaloSave()
    {
        try
        {
            double fat = Double.parseDouble(txtBuffaloFAT.getText());
            double gfat = Double.parseDouble(txtBuffaloGFAT.getText());
            double milk = Double.parseDouble(txtBuffaloMilk.getText());
            double degree = 0.0;
            if(Global.isDouble(txtBuffaloDegree.getText()))
                degree = Double.parseDouble(txtBuffaloDegree.getText());
            double rate1 = Double.parseDouble(txtBuffaloRate.getText());
            double total = Double.parseDouble(txtBuffaloTotalAmt.getText());

            if(checkCustomer1())
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                String cname = comboBuffaloCustName.getSelectionModel().getSelectedItem();
                DB.Customer customer = (Customer) session.createQuery("from DB.Customer s where s.name=:id and s.d=1").setParameter("id", cname).uniqueResult();
                DB.Rate rate = (Rate) session.createQuery("from DB.Rate s where s.d=1 and s.cur=2 and s.name=:id").setParameter("id", "buffalo").uniqueResult();

                DB.BuffaloMilk cowMilk = new BuffaloMilk();
                cowMilk.setD(1);
                if(buffaloRdbtnMorning.isSelected())
                    cowMilk.setMe(1);
                else
                    cowMilk.setMe(0);
                cowMilk.setCustomer(customer);
                cowMilk.setDate(buffaloDate.getValue());
                cowMilk.setTime(LocalTime.now());
                cowMilk.setCdegree(rate.getNdegree());
                cowMilk.setCfat(rate.getNfat());
                cowMilk.setCrate(rate.getNrate());

                cowMilk.setRate(rate1);
                cowMilk.setTotalamt(total);
                cowMilk.setTotalmilk(milk);
                cowMilk.setDegree(degree);
                cowMilk.setFat(fat);
                cowMilk.setGfat(gfat);

                session.persist(cowMilk);
                transaction.commit();
                session.close();
                if (!Global.lang)
                    lblBuffaloWarn.setText("दूध जोडले..!!");
                else
                    lblBuffaloWarn.setText("Milk Added..!!");
            }
            else
            {
                if (!Global.lang)
                    lblBuffaloWarn.setText("कृपया वैध सभासद निवडा..!!");
                else
                    lblBuffaloWarn.setText("Please select Valid Customer..!!");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            if (!Global.lang)
                lblBuffaloWarn.setText("कृपया वैध तपशील प्रविष्ट करा..!!");
            else
                lblBuffaloWarn.setText("Please Enter Correct Data..!!");
        }
    }

    @FXML
    void onCustChanged1()
    {
        String cno = txtBuffaloCustNo.getText();
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
                        comboBuffaloCustName.setValue(c.getName());
                    }
                }
                else
                    comboBuffaloCustName.setValue("");
            }
            catch (Exception ignored)
            {

            }
            transaction.commit();
            session.close();
        }
        else
            comboBuffaloCustName.setValue("");
    }

    @FXML
    void onMilkChanged1()
    {
        setFinalTotal1();
    }

    private void setFinalTotal1()
    {
        if(Global.isDouble(txtBuffaloMilk.getText()) && Global.isDouble(txtBuffaloFAT.getText()))
        {
            double fat = Double.parseDouble(txtBuffaloFAT.getText());
            double milk = Double.parseDouble(txtBuffaloMilk.getText());
            txtBuffaloGFAT.setText(String.valueOf(Global.round2(fat * milk)));
            double gfat = fat * milk;
            double fixRate1 = 0.0;
            if(Global.isDouble(txtBuffaloRate.getText()))
                fixRate1 = Double.parseDouble(txtBuffaloRate.getText());
            txtBuffaloTotalAmt.setText(String.valueOf(Global.round2(gfat * fixRate1)));
        }
    }

    @FXML
    void onRateChanged1()
    {
        setFinalTotal1();
    }


}

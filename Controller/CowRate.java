package Controller;

import DB.Global;
import DB.Rate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class CowRate {

    @FXML
    private TextField degree, fat, newrate, crate, currentrate, newrate1;

    @FXML
    private Button add, add1, reset, reset1;

    @FXML
    private Label warn, warn1, lblchangerate, lblpurchase, lblsale, lblcurprice, lblnewrate, lblcurprice1, lblnewrate1;


    @FXML
    public  void initialize()
    {
        if(Global.lang)
        {
            lblchangerate.setText("Cow Milk Price Change");
            lblpurchase.setText("Purchase");
            lblsale.setText("Sale");
            lblcurprice.setText("Current Price :");
            lblnewrate.setText("New Price :");
            lblcurprice1.setText("Current Price :");
            lblnewrate1.setText("New Price :");
            crate.setPromptText("Current Price");
            newrate.setPromptText("New Price");
            currentrate.setPromptText("Current Price");
            newrate1.setPromptText("New Price");
            add.setText("Add");
            reset.setText("Reset");
            add1.setText("Add");
            reset1.setText("Reset");
        }
        try
        {
            warn.setTextFill(Paint.valueOf(Global.labelColor1));
            warn1.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }


        try{
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            DB.Rate rate = (DB.Rate) session.createQuery("from DB.Rate s where s.name=:id and s.cur=:id1 and s.d=1").setParameter("id1",1).setParameter("id", "cow").uniqueResult();
            degree.setText(String.valueOf(rate.getNdegree()));
            fat.setText(String.valueOf(rate.getNfat()));
            newrate.setText("0");
            crate.setText(String.valueOf(rate.getNrate()));
            currentrate.setText(String.valueOf(rate.getNsrate()));
            newrate1.setText("0");
            transaction.commit();
            session.close();
        }
        catch (Exception ignored)
        {
        }

        reset.setOnAction(event -> {
            degree.setText("0");
            fat.setText("0");
            newrate.setText("0");
            warn.setText("");
        });
        reset1.setOnAction(event -> {
            newrate1.setText("0");
            warn1.setText("");
        });

        add.setOnAction(event -> {


                if (!Global.isDouble(degree.getText()))
                    if (!Global.lang)
                        warn.setText("कृपया योग्य डिग्री टाका..!!");
                    else
                        warn.setText("Please enter correct Degree..!!");
                else if (!Global.isDouble(fat.getText()))
                    if (!Global.lang)
                        warn.setText("कृपया योग्य फॅट टाका..!!");
                    else
                        warn.setText("Please enter correct FAT..!!");
                else if (!Global.isDouble(newrate.getText()))
                    if (!Global.lang)
                        warn.setText("कृपया योग्य दर टाका..!!");
                    else
                        warn.setText("Please enter correct Rate..!!");
                else {
                    Session session = Global.getSession();
                    Transaction transaction = session.beginTransaction();
                    DB.Rate rate = (DB.Rate) session.createQuery("from DB.Rate s where s.name=:id and s.cur=:id1 and s.d=1").setParameter("id1", 1).setParameter("id", "cow").uniqueResult();
                    rate.setNdegree(Double.parseDouble(degree.getText()));
                    rate.setNfat(Double.parseDouble(fat.getText()));
                    rate.setOrate(rate.getNrate());
                    rate.setNrate(Double.parseDouble(newrate.getText()));
                    rate.setSdate(LocalDate.now());
                    session.update(rate);
                    transaction.commit();

                    transaction.begin();
                    Rate rate1 = new Rate();
                    rate1.setNdegree(rate.getNdegree());
                    rate1.setNfat(rate.getNfat());
                    rate1.setOrate(rate.getOrate());
                    rate1.setNrate(rate.getNrate());
                    rate1.setNsrate(rate.getNsrate());
                    rate1.setOsrate(rate.getOsrate());
                    rate1.setSdate(rate.getSdate());
                    rate1.setSdate(rate.getSdate());
                    rate1.setName(rate.getName());
                    rate1.setCur(0);
                    session.persist(rate1);
                    transaction.commit();

                    session.close();
                    if (!Global.lang)
                        warn.setText("दर बदलला..!!");
                    else
                        warn.setText("Rate Change..!!");
                }

        });

        add1.setOnAction(event -> {
            if(!Global.isDouble(newrate1.getText()))
                if (!Global.lang)
                        warn1.setText("कृपया योग्य दर प्रविष्ट करा..!!");
                else
                    warn1.setText("Please enter correct rate..!!");
            else
            {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Rate rate = (DB.Rate) session.createQuery("from DB.Rate s where s.name=:id and s.cur=:id1 and s.d=1").setParameter("id1",1).setParameter("id", "cow").uniqueResult();
                rate.setOsrate(rate.getNsrate());
                rate.setNsrate(Double.parseDouble(newrate1.getText()));
                rate.setSdate(LocalDate.now());
                session.update(rate);

                transaction.commit();

                transaction.begin();
                Rate rate1 = new Rate();
                rate1.setNdegree(rate.getNdegree());
                rate1.setNfat(rate.getNfat());
                rate1.setOrate(rate.getOrate());
                rate1.setNrate(rate.getNrate());
                rate1.setNsrate(rate.getNsrate());
                rate1.setOsrate(rate.getOsrate());
                rate1.setSdate(rate.getSdate());
                rate1.setSdate(rate.getSdate());
                rate1.setName(rate.getName());
                rate1.setCur(0);
                session.persist(rate1);
                transaction.commit();
                session.close();
                if (!Global.lang)
                    warn1.setText("दर बदलला..!!");
                else
                    warn1.setText("Rate Change..!!");
            }
        });

    }




}

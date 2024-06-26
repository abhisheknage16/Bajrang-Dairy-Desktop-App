package Controller;

import DB.Details;
import DB.Global;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.hibernate.Session;
import org.hibernate.Transaction;

/****Raushan Bharti****/

public class ColorPickerr {

    @FXML
    private Button btnSet;
    @FXML
    private Button btnReset;
    @FXML
    private ColorPicker comboCenter, comboLeft, comboTop, comboLabel;
    @FXML
    private Label lblWarn, lblTitle, lblcenter, lblLeft, lblTop, lblLabel;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblTitle.setText("Set Color");
            lblcenter.setText("Center :");
            lblLeft.setText("Left :");
            lblTop.setText("Top :");
            lblLabel.setText("Label :");
            btnSet.setText("Set");
            btnReset.setText("Reset");
        }

        try
        {
            lblWarn.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }


        try
        {

            lblWarn.setTextFill(Paint.valueOf(Global.labelColor1));

            comboCenter.setValue(Color.valueOf(Global.center));
            comboLeft.setValue(Color.valueOf(Global.left));
            comboTop.setValue(Color.valueOf(Global.top));
            comboLabel.setValue(Color.valueOf(Global.labelColor));
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        btnSet.setOnAction(event -> {

            try
            {
                Color c = comboCenter.getValue();
                Color l = comboLeft.getValue();
                Color t = comboTop.getValue();
                Color lbl = comboLabel.getValue();

                Global.center = String.valueOf(c);
                Global.left = String.valueOf(l);
                Global.top = String.valueOf(t);
                Global.labelColor = String.valueOf(lbl);

                Color lbl1 = Color.valueOf(Global.labelColor);
                Global.labelColor1 = "#" + Integer.toHexString(lbl1.hashCode());

                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Details details = (Details) session.createQuery("from DB.Details s where s.id=1").uniqueResult();
                details.setLeft(Global.left);
                details.setTop(Global.top);
                details.setCenter(Global.center);
                details.setLabel(Global.labelColor);
                transaction.commit();
                session.close();
                if (!Global.lang)
                    lblWarn.setText("रंग सेटिंग अद्यतनित..!!");
                else
                    lblWarn.setText("Color Setting Update..!!");
            }
            catch (Exception e)
            {
                if (!Global.lang)
                    lblWarn.setText("कृपया पुन्हा प्रयत्न करा..!!");
                else
                    lblWarn.setText("Please try again..!!");
            }
        });

        btnReset.setOnAction(event -> {
            try {
                Session session = Global.getSession();
                Transaction transaction = session.beginTransaction();
                DB.Details details = (Details) session.createQuery("from DB.Details s where s.id=1").uniqueResult();
                Global.left = details.getLeft();
                Global.top = details.getTop();
                Global.center = details.getCenter();
                Global.labelColor = details.getLabel();
                transaction.commit();
                session.close();
                comboCenter.setValue(Color.valueOf(Global.center));
                comboLeft.setValue(Color.valueOf(Global.left));
                comboTop.setValue(Color.valueOf(Global.top));
                comboLabel.setValue(Color.valueOf(Global.labelColor));
                lblWarn.setText("");
            }
            catch (Exception e)
            {

            }
        });

    }

}

package Controller;

import DB.Global;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Anil on 21/08/2018
 */
public class Dashboard
{
    @FXML
    private Label lblLastLogin, lblCurrentTime, lblDashboard;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            lblDashboard.setText("Bajarang Dairy");
        }
        try
        {
            lblCurrentTime.setTextFill(Paint.valueOf(Global.labelColor1));
            lblLastLogin.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }

        ///// for last login in our application
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
        Date date;
        date = java.util.Calendar.getInstance().getTime();
//        System.out.println(formatter.format(date));

//        Date date=java.util.Calendar.getInstance().getTime();
//        System.out.println(date);

        lblLastLogin.setText(formatter.format(date));

//        ////  for clock in application
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a");
            LocalDateTime now = LocalDateTime.now();

            //please put label name instead of time below
            lblCurrentTime.setText(dtf.format(now));
        }));
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }

}

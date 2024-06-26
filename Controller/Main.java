package Controller;

import DB.Global;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import static java.lang.System.exit;

/**
 * Created by Anil on 21/08/2018
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        if(Global.flag == 1) {
            MainController controller = new MainController();
            Scene scene = new Scene(
                    controller.getContent(), 482, 337, Color.AQUA
            );
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.jpg").toString()));
            primaryStage.setTitle(Global.client);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        else
        {
            Warning warning = new Warning();
            Scene scene = new Scene(
                    warning.getContent(), 482, 337, Color.AQUA
            );
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.jpg").toString()));
            primaryStage.setTitle(Global.client);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    @Override
    public void stop() throws Exception {
        Global.closeFactory();
        exit(0);
    }
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> strmac = new ArrayList<>();
            while(networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if(mac != null)
                {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                    {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    strmac.add(sb.toString());
                }
            }
            Global.flag = 1;
            for (Iterator iterator = strmac.iterator(); iterator.hasNext();) {
                String sss = String.valueOf(iterator.next());
                if(sss.equals("70-F3-95-0B-10-94") || sss.equals("00-0F-00-79-50-45")) {
                    Global.flag= 1;
                }
            }
            if(Global.flag == 1)
            {
                MyThread t1 = new MyThread();
                new Thread(t1).start();
            }
            launch(args);

        }
        catch (SocketException ignored){
        }
    }
}

class MyThread implements Runnable
{
    public MyThread()
    {
    }
    @Override
    public void run()
    {
        Session session = DB.Global.getSession();
        session.close();
    }
}


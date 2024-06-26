package Controller;

import DB.Details;
import DB.Global;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * Created by Anil on 21/08/2018
 */
public class Home {

    @FXML
    private StackPane stack;
    @FXML
    public Pane leftPane;
    @FXML
    public Label lblBajarang, lblDairy;
    @FXML
    public MenuBar menuBar;
    @FXML
    private Button btnCowMilkPurchase, btnBuffaloMilkPurchase, btnMilkSale, btnClearalltab;
    @FXML
    private MenuItem backup, restore, refreshsoftware;
    @FXML
    private MenuItem updatepass, updateseq, addcust, exit, viewcust;
    @FXML
    private MenuItem addexpanses, viewexpanses;
    @FXML
    private MenuItem cowmilk, buffalomilk, milksale, combomilkpurchase;
    @FXML
    private MenuItem cowrate1, viewcowrate1, buffalorate1, viewbuffalorate1, milkreports, milkreports1, milksalesreport, setColor;//cowrate, buffalorate,
    @FXML
    private MenuItem masterbill,masterbill2, sendmsg, viewmsg, viewallmilkpurchase;
    @FXML
    private MenuItem viewcowmilk, viewbuffalomilk;
    @FXML
    private MenuItem addcredit, viewcredit, adddebit, viewdebit, viewpending, viewactivity;
    @FXML
    public static TabPane tab1;
    @FXML
    private MenuItem notepad, calculator;
    @FXML
    private Menu customer, payment, milkpurchase, viewmilkpurchase, ratechange, report, expense, master, helpp, home;

    public SingleSelectionModel<Tab> selectionModel ;
    public static SingleSelectionModel<Tab> singleSelectionModel1 ;

    @FXML
    public void initialize()
    {
        if(Global.lang)
        {
            home.setText("Home");
            updatepass.setText("Update Password");
            updateseq.setText("Update Seq");
            setColor.setText("Set Color");
            backup.setText("Back Up");
            restore.setText("Restore");
            refreshsoftware.setText("Refresh Software");
            exit.setText("Exit");

            customer.setText("Customer");
            addcust.setText("Add Customer");
            viewcust.setText("View Customer");

            payment.setText("Payment");
            addcredit.setText("Add Credit");
            viewcredit.setText("View Credit Report");
            adddebit.setText("Add Debit");
            viewdebit.setText("View Debit Report");
            viewpending.setText("Pending Payment");
            viewactivity.setText("Account Activity");

            milkpurchase.setText("Milk Purchase");
            combomilkpurchase.setText("Buy Both Milk");
            cowmilk.setText("Cow Milk");
            buffalomilk.setText("Buffalo Milk");
            milksale.setText("Milk Sale");

            viewmilkpurchase.setText("View Milk Purchase");
            viewcowmilk.setText("View Cow Milk Purchase");
            viewbuffalomilk.setText("View Buffalo Milk Purchase");

            ratechange.setText("Rate Change");
//            cowrate.setText("Cow Milk Prices Change");
//            buffalorate.setText("Buffalo Milk Prices Change");

            cowrate1.setText("Cow Milk Price Change");
            viewcowrate1.setText("View Cow Milk Rate");
            buffalorate1.setText("Buffalo Milk Price Change");
            viewbuffalorate1.setText("View Buffalo Rate");


            report.setText("Report");
            milkreports.setText("Cow Milk Purchase Report");
            milkreports1.setText("Buffalo Milk Purchase Report");
            viewallmilkpurchase.setText("View All Milk Purchase Report");
            milksalesreport.setText("Milk Sales Report");

            expense.setText("Expense");
            addexpanses.setText("Add Expanses");
            viewexpanses.setText("View Expenses");

            master.setText("Master Bill");
            masterbill.setText("Master Bill Buffelo");
            masterbill2.setText("Master Bill Cow");

            helpp.setText("Help");
            notepad.setText("Notepad");
            calculator.setText("Calculator");

            btnCowMilkPurchase.setText("Cow Milk Purchase");
            btnBuffaloMilkPurchase.setText("Buffalo Milk Purchase");
            btnMilkSale.setText("Milk Sales");
            btnClearalltab.setText("Close All Tab");

            lblBajarang.setText("Bajarang");
            lblDairy.setText("Dairy");

        }
//        color setting module code start
        try
        {
            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();
            DB.Details details = (Details) session.createQuery("from DB.Details s where s.id=1").uniqueResult();
            Global.left = details.getLeft();
            Global.top = details.getTop();
            Global.center = details.getCenter();
            Global.labelColor = details.getLabel();
            transaction.commit();
            session.close();

            Color c = Color.valueOf(Global.center);
            String c1 = "#" + Integer.toHexString(c.hashCode());

            Color l = Color.valueOf(Global.left);
            String l1 = "#" + Integer.toHexString(l.hashCode());

            Color t = Color.valueOf(Global.top);
            String t1 = "#" + Integer.toHexString(t.hashCode());

            Color lbl = Color.valueOf(Global.labelColor);
            Global.labelColor1 = "#" + Integer.toHexString(lbl.hashCode());

            stack.setStyle("-fx-background-color: "+c1);
            leftPane.setStyle("-fx-background-color: "+l1);
            menuBar.setStyle("-fx-background-color: "+t1);
        }
        catch (Exception e)
        {
//            System.out.println(e);
        }
//        color setting module code end

        tab1 = new TabPane();
        stack.getChildren().clear();
        stack.getChildren().add(tab1);

        selectionModel = tab1.getSelectionModel();
        singleSelectionModel1 = tab1.getSelectionModel();

        try
        {
            Tab tab;
            if(!Global.lang)
                tab = new Tab("डॅशबोर्ड");
            else
                tab = new Tab("Dashboard");
            tab.setClosable(false);
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.getChildren().clear();
            Node node = FXMLLoader.load(this.getClass().getResource("/Design/dashboard.fxml"));
            stackPane.getChildren().setAll(node);
            tab.setContent(stackPane);

            tab1.getTabs().add(tab);
            singleSelectionModel1.select(tab);
        }
        catch (Exception ignored)
        {
        }
/////// Button Action

        btnClearalltab.setOnAction(event ->{

            System.out.println("Close Tabs");
            int ee = tab1.getTabs().size();
            tab1.getTabs().remove(1, ee);

        });

        btnCowMilkPurchase.setOnAction(event -> {
            try
            {
                Tab tab = new Tab("गाईचे दूध खरेदी");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/cowmilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        btnBuffaloMilkPurchase.setOnAction(event -> {
            try
            {
                Tab tab = new Tab("म्हशीचे दूध खरेदी");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/buffalomilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        btnMilkSale.setOnAction(event -> {
            try
            {
                Tab tab = new Tab("दूध विक्री");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/milksale.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

///////// Button Action End

////////  Help Module start
        calculator.setOnAction(event -> {
            try
            {
                Runtime.getRuntime().exec("calc.exe");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        notepad.setOnAction(event -> {
            try
            {
                Runtime.getRuntime().exec("notepad.exe");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

////////  Help Module end
//          home module start
        exit.setOnAction(event -> exit(0));

        updatepass.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("संकेतशब्द अद्यतनित करा");
                else
                tab = new Tab("Update Password");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/updatepass.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e){
                e.printStackTrace();
            }

        });

        updateseq.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सुरक्षा प्रश्न अद्यतनित करा");
                else
                tab = new Tab("Update Seq");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/updateseq.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        setColor.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("रंग सेटिंग बदला");
                else
                    tab = new Tab("Set Color");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/colorpicker.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
//        home module end


        addcust.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद जोडा");
                else
                tab = new Tab("Add Customer");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/addcust.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewcust.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("सभासद बघा");
                else
                tab = new Tab("View Customer");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewcust.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        combomilkpurchase.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("दोन्ही दुधाची खरेदी");
                else
                    tab = new Tab("Buy Both Milk");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/combomilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });


        cowmilk.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("गाय दूध खरेदी");
                else
                    tab = new Tab("Cow Milk Purchase");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/cowmilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        buffalomilk.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("म्हैस दूध खरेदी");
                else
                tab = new Tab("Buffalo Milk Purchase");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/buffalomilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        milksale.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("दूध विक्री");
                else
                    tab = new Tab("Milk Sale");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/milksale.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

//        cowrate.setOnAction(event -> {
//            try {
//                Tab tab;
//                if(!Global.lang)
//                tab = new Tab("गाय दूध दर बदल");
//                else
//                    tab = new Tab("Cow Rate");
//                tab.setClosable(true);
//                StackPane stackPane = new StackPane();
//                stackPane.setAlignment(Pos.CENTER);
//                stackPane.getChildren().clear();
//                Node node = FXMLLoader.load(this.getClass().getResource("/Design/cowrate.fxml"));
//                stackPane.getChildren().setAll(node);
//                tab.setContent(stackPane);
//                tab1.getTabs().add(tab);
//                singleSelectionModel1.select(tab);
//            }
//            catch(IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        buffalorate.setOnAction(event -> {
//            try {
//                Tab tab;
//                if(!Global.lang)
//                tab = new Tab("म्हैस  दूध दर बदल");
//                else
//                    tab = new Tab("Buffalo Change Rate");
//
//                tab.setClosable(true);
//                StackPane stackPane = new StackPane();
//                stackPane.setAlignment(Pos.CENTER);
//                stackPane.getChildren().clear();
//                Node node = FXMLLoader.load(this.getClass().getResource("/Design/buffalorate.fxml"));
//                stackPane.getChildren().setAll(node);
//                tab.setContent(stackPane);
//                tab1.getTabs().add(tab);
//                singleSelectionModel1.select(tab);
//            }
//            catch(IOException e) {
//                e.printStackTrace();
//            }
//        });

        cowrate1.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("गाय दूध दर बदल");
                else
                    tab = new Tab("Cow Rate Change");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/cowratechange.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });


        viewcowrate1.setOnAction(event -> {
            try {
                Tab tab;
                tab = new Tab("View Cow Rate");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewcowratechange.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        buffalorate1.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("म्हैस दूध दर बदल");
                else
                    tab = new Tab("Buffalo Change Rate Change");

                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/buffaloratechange.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewbuffalorate1.setOnAction(event -> {
            try {
                Tab tab;
                tab = new Tab("View Buffalo Rate");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewbuffaloratechange.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        milkreports.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("गायीच्या दुधाचे अहवाल");
                else
                    tab = new Tab("Milk Reports");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/cowreports.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        milkreports1.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("म्हशीच्या दुधाचा अहवाल");
                else
                    tab = new Tab("Buffalo Milk Reports");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/buffaloreports.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewallmilkpurchase.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("सर्व दूध खरेदी पहा");
                else
                    tab = new Tab("View All Milk Purchase Reports");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewallmilkpurchase.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        milksalesreport.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("दूध विक्री अहवाल");
                else
                tab = new Tab("Milk Sales Report");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/milksalesreports.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });


        masterbill.setOnAction(event -> {

            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("मास्टर बिल म्हैस");
                else
                    tab = new Tab("Master Bill Buffello ");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/masterbill.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        masterbill2.setOnAction(event -> {

            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("मास्टर बिल गाय");
                else
                    tab = new Tab("Master Bill Cow");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/masterbill2.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });


        sendmsg.setOnAction(event -> {

            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("संदेश पाठवा");
                else
                    tab = new Tab("Send Message");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/sendmessage.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewmsg.setOnAction(event -> {

            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("संदेश पहा");
                else
                    tab = new Tab("View Message");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewmessage.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        addexpanses.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("खर्च जोडा");
                else
                    tab = new Tab("Add Expense");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/addexpense.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        viewexpanses.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("खर्च पहा");
                else
                    tab = new Tab("View Expanse");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewexpense.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        viewcowmilk.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("गायचे दूध पहा");
                else
                    tab = new Tab("View Cow Milk");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewcowmilk.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        viewbuffalomilk.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("म्हशीचे दूध पहा");
                else
                tab = new Tab("View Buffalo Milk");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewbuffalomilk.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        addcredit.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद क्रेडिट");
                else
                    tab = new Tab("Add Credit");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/addcredit.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        viewcredit.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद क्रेडिट बघा");
                else
                    tab = new Tab("View Credit");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewcredit.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        adddebit.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद डेबिट");
                else
                    tab = new Tab("View Debit");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/adddebit.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewdebit.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद डेबिट बघा");
                else
                    tab = new Tab("View Debit");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewdebit.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        viewactivity.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सभासद खाते क्रियाकलाप पहा");
                else
                    tab = new Tab("View Activity");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/accountactivity.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        viewpending.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("सभासद प्रलंबित बघा");
                else
                    tab = new Tab("View Pending");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/viewpending.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);

                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });

        backup.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                    tab = new Tab("बॅकअप सॉफ्टवेअर डेटा");
                else
                    tab = new Tab("Back Up");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/backup.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        restore.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("सॉफ्टवेअर डेटा पुनर्संचयित करा");
                else
                    tab = new Tab("Restore Software");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/restore.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        refreshsoftware.setOnAction(event -> {
            try {
                Tab tab;
                if(!Global.lang)
                tab = new Tab("रीफ्रेश सॉफ्टवेअर");
                else
                    tab = new Tab("Refresh Software");
                tab.setClosable(true);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                stackPane.getChildren().clear();
                Node node = FXMLLoader.load(this.getClass().getResource("/Design/refreshsoftware.fxml"));
                stackPane.getChildren().setAll(node);
                tab.setContent(stackPane);
                tab1.getTabs().add(tab);
                singleSelectionModel1.select(tab);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });


    }

    public void setStack(Node node) {


        stack.getChildren().clear();
        stack.getChildren().setAll(node);
    }

    public void loadStack(String fxml) {
        try {
            setStack(
                    FXMLLoader.load(
                            StackNavigator.class.getResource(
                                    fxml
                            )
                    )
            );
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}

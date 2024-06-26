package Controller;

import DB.Global;
import DB.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Anil on 09/07/2018
 */
public class Forgetpass {
    @FXML
    private Button cancel,update;
    @FXML
    private TextField uname, ans1;
    @FXML
    private PasswordField pass1, pass2;
    @FXML
    private Label warn, forgetpass, name, seqQue, ans, pass, cpass;
    @FXML
    private ComboBox squestion;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize()
    {
        if(Global.lang)
        {
            forgetpass.setText("Forget Password");
            name.setText("User Name :");
            seqQue.setText("Security Question :");
            pass.setText("New Password :");
            cpass.setText("Confirm Password :");
            ans.setText("Answer :");
            uname.setPromptText("User Name");
            squestion.setPromptText("--Select--");
            ans1.setPromptText("Answer");
            pass1.setPromptText("Password");
            pass2.setPromptText("Confirm Password");
        }
        try
        {
            warn.setTextFill(Paint.valueOf(Global.labelColor1));
            ans.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }
        setData();
         cancel.setOnAction(event ->
         {
             try
             {
                 Parent root;
                 root = FXMLLoader.load(MainController.class.getResource("/Design/login.fxml"));
                 Stage stage = new Stage();
                 stage.setTitle(Global.client);
                 Screen screen = Screen.getPrimary();
                 Rectangle2D bounds = screen.getVisualBounds();
                 stage.getIcons().add(new Image(this.getClass().getResource("logo.jpg").toString()));
                 stage.setScene(new Scene(root, 400, 350));
                 stage.setResizable(false);
                 stage.show();
                 ((Node) (event.getSource())).getScene().getWindow().hide();
             }
             catch (Exception e)
             {
                     warn.setText("काहीतरी चूक झाली..!!" + e);
             }
         });

         update.setOnAction(event ->
         {
             String nme = uname.getText();
             String answ = ans.getText();
             String passwd1 = pass1.getText();
             String passwd2 = pass2.getText();
             if (nme.isEmpty())
                 if (!Global.lang)
                     warn.setText("कृपया युजरनेम टाका..!!");
                 else
                     warn.setText("Please enter username..!!");
             else if (answ.isEmpty())
                 if (!Global.lang)
                     warn.setText("कृपया उत्तर प्रविष्ट करा..!!");
                 else
                     warn.setText("Please enter answer..!!");
             else if (passwd1.equals(""))
                 if (!Global.lang)
                     warn.setText("कृपया संकेतशब्द प्रविष्ट करा");
                 else
                     warn.setText("Please enter password..!!");
             else if (!passwd1.equals(passwd2))
                 if (!Global.lang)
                     warn.setText("संकेतशब्द जुळत नाही");
                 else
                     warn.setText("Password not match..!!");
             else
             {
                 try
                 {
                     String ques = squestion.getValue().toString();
                     Session session = Global.getSession();
                     Transaction transaction = session.beginTransaction();
                     User user = session.load(User.class, nme);
                     if (user == null)
                     {
                         if (!Global.lang)
                            warn.setText("कृपया योग्य युजरनेम टाका ..!!");
                         else
                             warn.setText("Please enter username..!!");
                     }
                     else if (ques.equals(user.getQname()) && answ.equals(user.getAns()))
                     {
                         if (passwd1.equals(passwd2))
                         {
                             user.setPass(passwd1);
                             if (!Global.lang)
                                 warn.setText("संकेतशब्द अद्यतनित केला..!!");
                             else
                                 warn.setText("Password updated..!!");
                                 uname.setText("");
                                 ans.setText("");
                                 pass1.setText("");
                                 pass2.setText("");
                                 session.persist(user);
                         }
                         else
                         {
                             if (!Global.lang)
                                warn.setText("कृपया अचूक संकेतशब्द प्रविष्ट करा..!!");
                             else
                                 warn.setText("Please enter correct password..!!");
                         }
                     }
                     else
                     {
                         if (!Global.lang)
                            warn.setText("कृपया योग्य डेटा प्रविष्ट करा..!!");
                         else
                             warn.setText("Please enter correct data..!!");
                     }
                     transaction.commit();
                     session.close();
                 }
                 catch (Exception e)
                 {
                     if (!Global.lang)
                        warn.setText("कृपया योग्य डेटा प्रविष्ट करा..!!");
                     else
                         warn.setText("Please enter correct data..!!");
                 }
             }
         });

    }

    private void setData()
    {
            ObservableList<String> options =
                    FXCollections.observableArrayList(
                            "तुमचा आवडता चित्रपट कोणता?",
                            "तुमचा आवडता रंग कोणता आहे?",
                            "आपल्या आवडत्या पाळीव प्राण्याचे नाव काय आहे?",
                            "तुमची आवडती वेबसाइट कोणती आहे?",
                            "आपण कोणत्या हायस्कूलमध्ये शिक्षण घेतले?",
                            "तुम्ही कोणत्या शहरात जन्मलास?",
                            "आपला आवडता अभिनेता, संगीतकार किंवा कलाकार कोण आहे?",
                            "तुमचे आवडते पुस्तक कोणते आहे?"
                    );
            squestion.setItems(options);
    }
}

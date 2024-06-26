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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static java.lang.System.exit;

/**
 * Created by Anil on 21/08/2018
 */
public class MainController implements Controller
{
    @FXML
    private javafx.scene.control.Button login, reset;
    @FXML
    private javafx.scene.control.TextField uname;
    @FXML
    private PasswordField pass;
    @FXML
    private Label warn, forpass;

    @FXML
    private ComboBox<String> comboLang;

    private ObservableList<String> lang = FXCollections.observableArrayList("English", "Marathi");

    @FXML
    private void initialize()
    {

        comboLang.getItems().addAll(lang);

        comboLang.setValue("English");

        reset.setOnAction(event -> exit(0));

        forpass.setOnMouseClicked(event ->
        {
            try {
                Parent root;
                root = FXMLLoader.load(MainController.class.getResource("/Design/forgetpass.fxml"));
                Stage stage = new Stage();
                stage.setTitle(DB.Global.client);
                stage.getIcons().add(new Image(this.getClass().getResource("logo.jpg").toString()));
                stage.setScene(new Scene(root, 540, 380));
                stage.setResizable(false);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                warn.setText("काहीतरी चूक झाली..!!");
            }
        });

        login.setOnAction((event) ->
        {
                String name = uname.getText();
                String password = pass.getText();
                if (name.isEmpty()) {
                    warn.setText("कृपया युजरनेम टाका..!!");
                } else if (password.isEmpty()) {
                    warn.setText("कृपया संकेतशब्द प्रविष्ट करा..!!");
                }
                else
                    {
                    try
                    {
                        Session session = DB.Global.getSession();
                        User user = (User) session.createQuery("from User l where l.name='" + name + "' and l.pass='" + password + "' ").uniqueResult();
                        session.close();
                        if (user == null) {
                            warn.setText("कृपया योग्य युजरनेम आणि संकेतशब्द प्रविष्ट करा..!!");
                        } else {

                            if(comboLang.getSelectionModel().getSelectedItem().equals("Marathi"))
                                Global.lang = false;
                            LocalTime lt = user.getTime();
                            Instant instant = lt.atDate(user.getDate()).atZone(ZoneId.systemDefault()).toInstant();
                            Date time = Date.from(instant);
                            Global.llogin = time.toString();
                            Global.uname = name;
                            Session session1 = Global.getSession();
                            Transaction transaction = session1.beginTransaction();
                            User user1 = session1.load(User.class, user.getName());
                            user1.setDate(LocalDate.now());
                            user1.setTime(LocalTime.now());
                            session1.persist(user1);
                            transaction.commit();
                            session1.close();

                            Parent root;

                            root = FXMLLoader.load(Home.class.getResource("/Design/home.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle(DB.Global.client);
                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();
                            stage.getIcons().add(new Image(this.getClass().getResource("logo.jpg").toString()));
                            stage.setScene(new Scene(root, bounds.getWidth() - 50, bounds.getHeight() - 50));
                            stage.setMaximized(true);
                            stage.show();
                            ((Node) (event.getSource())).getScene().getWindow().hide();
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                        warn.setText("काहीतरी चूक झाली..!!");
                    }
                }
            });
    }

    public MainController()
    {
    }

    @Override
    public Parent getContent()
    {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainController.class.getResource("/Design/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }
}

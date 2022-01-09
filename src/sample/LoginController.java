package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    public LoginModel loginModel = new LoginModel();

    @FXML
    private Text loginfeedback;

    @FXML
    private TextField username;

    @FXML
    private PasswordField inputpassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(loginModel.isDbConnect()){
            System.out.println("Database Connected - Login");;
        }
        else {
            System.out.println("Database not Connected - Try again!");
        }
    }

    public void Login(ActionEvent event) {
        try {
            if(loginModel.isLogin(username.getText(),inputpassword.getText())) {
                loginfeedback.setStyle("-fx-fill: green;");

                loginfeedback.setText("There you go!");

                Userinfo user = new Userinfo();
                user.Getinfo(username.getText(), inputpassword.getText());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("wordlist.fxml"));
                Parent root = loader.load();

                Stage window = (Stage)loginbtn.getScene().getWindow();
                window.setScene(new Scene(root));
            }
            else loginfeedback.setText("Invalid Information!");
        }

        catch (SQLException | IOException e) {
            loginfeedback.setText("Invalid Information");
            e.printStackTrace();
        }
    }

    @FXML
    private Button loginbtn;

    public void tosignup(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));

        Stage window = (Stage)loginbtn.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}

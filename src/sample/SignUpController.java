package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {



    @FXML
    private Button signupbtn;

    @FXML
    private TextField email;

    @FXML
    private PasswordField inputpassword;

    @FXML
    private Text loginfeedback;


    @FXML
    private TextField username;

    public void loginpage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Stage window = (Stage)signupbtn.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void tologin(ActionEvent event) throws Exception{
        loginpage();
    }

    public SignUpModel signupmodel = new SignUpModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(signupmodel.isDbConnect()) {
            System.out.println("Database Connected - Signup");
        }
        else {
            System.out.println("Database not Connected - Try again!");
        }
    }


    public void signup(ActionEvent event) {
        try {
            if(username.getText().isEmpty() || inputpassword.getText().isEmpty() || email.getText().isEmpty()) {
                loginfeedback.setText("You must fill in all of the forms");
            }
            else {
                if(signupmodel.isSignUp(username.getText(), email.getText(), inputpassword.getText())) {
                    loginfeedback.setStyle("-fx-fill: green;");
                    loginfeedback.setText("Successful!");

                    loginpage();
                }
                else {
                    loginfeedback.setText("Invalid information!");
                }
            }
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
            loginfeedback.setText("Unsuccessful! Try again");
        }
    }

}

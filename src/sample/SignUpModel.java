package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.sql.*;

public class SignUpModel {
    Connection connection;

    public SignUpModel() {
        connection = SqliteConnection.Connector();
        if(connection == null) System.exit(1);
    }

    public boolean isDbConnect() {
        try{
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSignUp(String username, String email, String password) throws SQLException {

        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
        String query1 = "SELECT * FROM users WHERE username = ?";
        ResultSet rst = null;
        try{
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1, username);
            rst = preparedStatement.executeQuery();
            if(rst.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sign Up Failed");
                    alert.setHeaderText("This username is available. Try another!");
                    alert.setContentText("Try Again!");
                    alert.getDialogPane().setMinHeight(180);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
                    dialogPane.getStyleClass().add("dialog-pane");

                if(alert.showAndWait().get() == ButtonType.OK) {
                    alert.close();
                }
                return false;
            }
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            int flag = preparedStatement.executeUpdate();

           if(flag > 0) return true;
           else return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            preparedStatement.close();
            rst.close();
            connection.close();
        }
    }
}

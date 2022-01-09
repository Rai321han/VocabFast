package sample;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    Connection connection;
    public LoginModel() {
        connection = SqliteConnection.Connector();

        if(connection == null) System.exit(1);
    }

    public boolean isDbConnect() {
        try {
            return !connection.isClosed();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLogin(String user, String pass) throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM users WHERE username = ? and password = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2,pass);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return true;
            }
            else {
                return false;
            }

        }

        catch (Exception e){
            return false;
        }
        finally {
            preparedStatement.close();
            resultSet.close();
        }
    }


}

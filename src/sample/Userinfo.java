package sample;

import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Userinfo {
    public static String username;
    public static String password;
    public static int totalWords=0;
    public static int userid;

    public void Getinfo(String user, String pass) throws SQLException {
        username = user;
        password = pass;

        Connection conn = SqliteConnection.Connector();

        String query = "SELECT user_id FROM users WHERE username=? and password=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1,username);
        pst.setString(2,password);

        ResultSet rslt = pst.executeQuery();
        userid = rslt.getInt("user_id");

        conn.close();
        rslt.close();
    }

    public static int getTotalWords() throws SQLException {
        String query = "SELECT COUNT(*) AS totalwords FROM wordlist WHERE user_id = ?";

        Connection conn = SqliteConnection.Connector();
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1,Userinfo.userid);

        ResultSet rs = pst.executeQuery();

        totalWords = rs.getInt("totalwords");
        conn.close();
        rs.close();
        return totalWords;
    }

    public int getUserid() {
        return userid;
    }


    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }
}

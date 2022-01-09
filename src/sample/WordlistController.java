package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class WordlistController implements Initializable {

    @FXML
    private TableView<WordRecord> tableView;

    @FXML
    private TableColumn<WordRecord, String> definition;

    @FXML
    private TableColumn<WordRecord, String> word;


    @FXML
    private TextArea insertedDefinition;

    @FXML
    private Text wordcount;

    @FXML
    private TextField insertedWord;

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button savebtn;

    @FXML
    private Button addbtn;

    @FXML
    private Button cancelbtn;

    @FXML
    private TextField searchbox;

    @FXML
    private TextField previewDefinition;

    @FXML
    private TextField previewWord;

    @FXML
    void wordlist(MouseEvent event){
        bp.setCenter(ap);
        active(btn2);
        notactive(btn3,btn4);
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("You're about to log out");
        alert.setContentText("You'll have to login again!");
        alert.getDialogPane().setMinHeight(180);
       // alert.show();

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        if(alert.showAndWait().get() == ButtonType.OK) {
//            Stage stage = (Stage) btn4.getScene().getWindow();
//            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage window = (Stage)btn4.getScene().getWindow();
            window.setScene(new Scene(root));
        }
    }

    @FXML
    void playquiz(MouseEvent event) throws IOException {
        loadpage("playquiz");
        active(btn3);
        notactive(btn2,btn4);
    }

    //Method for loading FXML by clicking nav button
    private void loadpage(String page) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        bp.setCenter(root);
    }

    private void active(Button btn_name){
        btn_name.setStyle("-fx-background-color: primary-light;");
    }

    private void notactive(Button btn_name2, Button btn_name3) {
        btn_name2.setStyle("-fx-background-color: primary;");
        btn_name3.setStyle("-fx-background-color: primary;");
    }

    public ObservableList<WordRecord> wordlist = FXCollections.observableArrayList();
    public ObservableList<WordRecord> selectedRow;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        savebtn.setDisable(true);
        savebtn.setVisible(false);
        cancelbtn.setDisable(true);
        cancelbtn.setVisible(false);

        insertedDefinition.setWrapText(true);

        word.setCellValueFactory(new PropertyValueFactory<WordRecord, String>("word"));
        definition.setCellValueFactory(new PropertyValueFactory<WordRecord, String>("definition"));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedRow = tableView.getSelectionModel().getSelectedItems();

        loaddata();
    }


    //Loading Data from database to table
    public void loaddata(){
        try {

            Connection conn = SqliteConnection.Connector();
            if (conn == null) System.out.println("Database not connected");

            String query = "SELECT * FROM wordlist WHERE user_id = ?";

            PreparedStatement pst = null;
            assert conn != null;
            pst = conn.prepareStatement(query);
            pst.setInt(1,Userinfo.userid);

            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                wordlist.add(new WordRecord(rs.getString("word"), rs.getString("definition")));
            }

            tableView.setItems(wordlist);

            setCount();

            conn.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Adding new word
    @FXML

    void addword(MouseEvent event) throws SQLException {


        Connection conn1 = SqliteConnection.Connector();

        if(conn1 == null) System.out.println("Database not connected");

        String query = "SELECT * FROM wordlist WHERE user_id=? AND word=? AND definition =?";
        PreparedStatement pst1 = null;
        assert conn1 != null;
        pst1 = conn1.prepareStatement(query);
        pst1.setInt(1,Userinfo.userid);
        pst1.setString(2,insertedWord.getText());
        pst1.setString(3,insertedDefinition.getText());

        ResultSet rst = pst1.executeQuery();


        if(rst.next()) {

            alertbox("Duplicate Word","This word is already in the list","Please add a different word");
            pst1.close();
            rst.close();
            conn1.close();
        }
        else if(!insertedWord.getText().isEmpty() && !insertedDefinition.getText().isEmpty()) {
            pst1.close();
            rst.close();
            conn1.close();
            int userid = Userinfo.userid;

            Connection conn = SqliteConnection.Connector();
            if(conn == null) System.out.println("Database not connected");

            query = "INSERT INTO wordlist(user_id,word,definition) VALUES(?,?,?)";
            PreparedStatement pst = null;

            assert conn != null;
            pst = conn.prepareStatement(query);
            pst.setInt(1,userid);
            pst.setString(2,insertedWord.getText());
            pst.setString(3,insertedDefinition.getText());

            pst.executeUpdate();

            WordRecord wordRecord = new WordRecord(insertedWord.getText(), insertedDefinition.getText());
            tableView.getItems().add(wordRecord);

            conn.close();
            setCount();
        }
        insertedWord.clear();
        insertedDefinition.clear();

    }

    public void alertbox(String tt, String header, String ct){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tt);
        alert.setHeaderText(header);
        alert.setContentText(ct);
        alert.getDialogPane().setMinHeight(180);
        alert.show();

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }

    //Method for Total Words
    public void setCount() throws SQLException {
        String query = "SELECT COUNT(*) AS totalwords FROM wordlist WHERE user_id = ?";

        Connection conn = SqliteConnection.Connector();
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1,Userinfo.userid);

        ResultSet rs = pst.executeQuery();

        int totalwords = rs.getInt("totalwords");
        Userinfo.totalWords = totalwords;
        wordcount.setText(""+totalwords);

        if(totalwords==0){
            deleteBtn.setDisable(true);
            editBtn.setDisable(true);
        }

        conn.close();
//        rs.close();
    }


    //Deleting Row
    public void deleterow(MouseEvent mouseEvent) throws SQLException {

        String query = "DELETE FROM wordlist WHERE user_id=? and word=? and definition =?";

        Connection conn = SqliteConnection.Connector();
        PreparedStatement pst = conn.prepareStatement(query);

        int deleted=0;

        for(int i = 0; i < selectedRow.size(); i++) {
            String wrd = selectedRow.get(i).getWord();
            String df = selectedRow.get(i).getDefinition();


            pst.setInt(1,Userinfo.userid);
            pst.setString(2,wrd);
            pst.setString(3,df);


            deleted = pst.executeUpdate();
        }
        String s = selectedRow.size()+" words has been deleted";
        System.out.println(deleted + " row deleted");
        alertbox("Deletion",s,"Press OK to continue");
        wordlist.removeAll(selectedRow); //Deleting From UI
        conn.close();
        setCount();

    }

    //Method for handling EDIT and DELETE buttons
    public void rowcount(MouseEvent mouseEvent) {
        saveandcancelhide();
        insertedWord.clear();
        insertedDefinition.clear();

        if(selectedRow.size() > 1) {
            editBtn.setDisable(true);
            deleteBtn.setDisable(false);
        }
        else if(selectedRow.size() == 1) {
            editBtn.setDisable(false);
            deleteBtn.setDisable(false);
        }
        else {
            editBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    public void disselect(MouseEvent mouseEvent) {
//        tableView.getSelectionModel().clearSelection();
//        editBtn.setDisable(true);
//        deleteBtn.setDisable(true);
    }


//    //Saving the edited word
    public void saveword(MouseEvent mouseEvent) throws SQLException {

        String query = "UPDATE wordlist SET word =?, definition =? WHERE word_no = (SELECT word_no FROM wordlist WHERE word =? AND definition =? AND user_id = ?)";
        Connection conn = SqliteConnection.Connector();
        assert conn != null;
        PreparedStatement pst = conn.prepareStatement(query);
        try{
            pst.setString(1,insertedWord.getText());
            pst.setString(2,insertedDefinition.getText());
            pst.setString(3, selectedRow.get(0).getWord());
            pst.setString(4,selectedRow.get(0).getDefinition());
            pst.setInt(5,Userinfo.userid);
            pst.executeUpdate();

            tableView.getItems().clear();
            loaddata();

            saveandcancelhide();

            insertedDefinition.clear();
            insertedWord.clear();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        finally {
            pst.close();
            conn.close();
        }

}

//    //Edit button
    public void editword(MouseEvent mouseEvent) {
        savebtn.setDisable(false);
        savebtn.setVisible(true);
        deleteBtn.setDisable(true);
        cancelbtn.setVisible(true);
        cancelbtn.setDisable(false);

        String wrd = selectedRow.get(0).getWord();
        String df = selectedRow.get(0).getDefinition();

        insertedWord.setText(wrd);
        insertedDefinition.setText(df);

    }

    public void cancelSave(MouseEvent mouseEvent) {
        deleteBtn.setDisable(false);
        insertedWord.clear();
        insertedDefinition.clear();
        saveandcancelhide();

    }

    //Hiding save and cancel button
    public void saveandcancelhide() {
        addbtn.setVisible(true);
        addbtn.setDisable(false);
        savebtn.setDisable(true);
        savebtn.setVisible(false);
        cancelbtn.setVisible(false);
        cancelbtn.setDisable(true);
    }
}



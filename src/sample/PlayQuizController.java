package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayQuizController implements Initializable {

    @FXML
    private Button startbtn;

    @FXML
    private BorderPane bp;

    @FXML
    private Button nextbtn;

    @FXML
    private Button resultbtn;

    @FXML
    private Label correctAnswer;

    @FXML
    private Label wrongAnswer;


    ToggleGroup toggleGroup = new ToggleGroup();

    RadioButton option1 = new RadioButton();
    RadioButton option2 = new RadioButton();
    RadioButton option3 = new RadioButton();
    RadioButton option4 = new RadioButton();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextbtn.setVisible(false);
        resultbtn.setVisible(false);
        correctAnswer.setWrapText(true);

    }

    public void startquiz(MouseEvent mouseEvent) throws SQLException {
        Quizclass.score = 0;
        Quizclass.noOfQuiz = 0;
        if(eligibility()) {
            nextbtn.setVisible(true);
            startbtn.setDisable(true);


            quiz();
//
//            Connection con = SqliteConnection.Connector();
//            ResultSet resultset = null;
//
//                Quizclass quizobject = new Quizclass();
//
//                String query = "SELECT word,definition FROM wordlist WHERE user_id = ? ORDER BY random() LIMIT 4";
//                assert con != null;
//            PreparedStatement pst = con.prepareStatement(query);
//                pst.setInt(1,Userinfo.userid);
//                resultset = pst.executeQuery();
//
//                quiz(resultset);
//
//                pst.close();
//                con.close();
        }
    }

    public void quiz() throws SQLException {
        Connection con = SqliteConnection.Connector();
        ResultSet rst = null;

        Quizclass quizobject = new Quizclass();

        String query = "SELECT word,definition FROM wordlist WHERE user_id = ? ORDER BY random() LIMIT 4";
        assert con != null;
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,Userinfo.userid);
        rst = pst.executeQuery();

        String Q = rst.getString("word");
        String A = rst.getString("definition");

        quizobject.setQuestion(Q);
        VBox vbox = new VBox();
        vbox.getStyleClass().add("questionbox");
        Label question = new Label("Meaning of "+ "'"+quizobject.getQuestion()+"'");
        question.getStyleClass().add("question");

        quizobject.setAnswer(rst.getString("definition"));
        List<String> list=new ArrayList<String>();

        rst.next();
        list.add(rst.getString("definition"));
        rst.next();
        list.add(rst.getString("definition"));
        rst.next();
        list.add(rst.getString("definition"));
        rst.next();
        list.add(rst.getString("definition"));

        Collections.shuffle(list);

        RadioButton option1 = new RadioButton(list.get(0));
        RadioButton option2 = new RadioButton(list.get(1));
        RadioButton option3 = new RadioButton(list.get(2));
        RadioButton option4 = new RadioButton(list.get(3));

        option1.setToggleGroup(toggleGroup);
        option2.setToggleGroup(toggleGroup);
        option3.setToggleGroup(toggleGroup);
        option4.setToggleGroup(toggleGroup);

        vbox.getChildren().addAll(question,option1,option2,option3,option4);
        bp.setCenter(vbox);

        pst.close();
        rst.close();
        con.close();

        bp.setBottom(nextbtn);
        String s = quizobject.getAnswer();

        nextbtn.setOnAction((event -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
            String toogleGroupValue = "";
            if(selectedRadioButton != null){
                toogleGroupValue = selectedRadioButton.getText();
            }
            else{
                String ans = "Correct Answer: " + quizobject.getAnswer();
                wrongAnswer.setVisible(true);
                correctAnswer.setVisible(false);
                wrongAnswer.setText(ans);
            }


            if(Quizclass.noOfQuiz < 4){
                if(s.equals(toogleGroupValue)) {
                    correctAnswer.setVisible(true);
                    correctAnswer.setText("You answer is correct!");
                    wrongAnswer.setVisible(false);
                    Quizclass.score++;
                }
                else {
                    String ans = "Correct Answer: " + quizobject.getAnswer();
                    wrongAnswer.setVisible(true);
                    correctAnswer.setVisible(false);
                    wrongAnswer.setText(ans);
                }
                try {
                    ++Quizclass.noOfQuiz;
                    quiz();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
            else{

                if(s.equals(toogleGroupValue)) {
                    correctAnswer.setVisible(true);
                    System.out.println("compare");
                    correctAnswer.setText("You answer is correct!");
                    wrongAnswer.setVisible(false);
                    Quizclass.score++;
                }
                else {
                    String ans = "Correct Answer: " + quizobject.getAnswer();
                    wrongAnswer.setVisible(true);
                    correctAnswer.setVisible(false);
                    wrongAnswer.setText(ans);
                }

                resultbtn.setVisible(true);
                startbtn.setVisible(false);
                nextbtn.setVisible(false);
                option1.setDisable(true);
                option2.setDisable(true);
                option3.setDisable(true);
                option4.setDisable(true);
            }

        }));
    }

    public boolean eligibility() throws SQLException {
        if(Userinfo.getTotalWords() >= 5) {
            return true;
        }
        else{
            alertbox("Play Quiz", "Not enough words!", "You must have at least 5 words in your wordlist");
            return false;
        }

    }

    public void alertbox(String tt, String header, String ct){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tt);
        alert.setHeaderText(header);
        alert.setContentText(ct);
        alert.getDialogPane().setMinHeight(180);
        //alert.show();

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        if(alert.showAndWait().get() == ButtonType.OK) {
            alert.close();
        }
    }

    public void result(MouseEvent mouseEvent) throws IOException {
        startbtn.setVisible(true);
        startbtn.setDisable(false);
        correctAnswer.setText("");
        wrongAnswer.setText("");
        String score = "You've got " +Quizclass.score + " out of 5";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText(score);
        alert.setContentText("Click OK to end this quiz");
        alert.getDialogPane().setMinHeight(180);
        // alert.show();

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");


        nextbtn.setVisible(false);
        resultbtn.setVisible(false);
        Quizclass.noOfQuiz = 0;
        Quizclass.score = 0;

        if(alert.showAndWait().get() == ButtonType.OK) {
            alert.close();
        }

    }

}

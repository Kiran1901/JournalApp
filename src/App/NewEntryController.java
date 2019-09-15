package App;
import Connectivity.ConnectionClass;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NewEntryController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Button okButton;

    @FXML
    public Text timeText,dateText;
    public TextArea textArea;

    public NewEntryController(Dialog dialog){
        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
    }

    public void initialize(){
        dateText.setText(LocalDate.now().toString());
        timeText.setText(formatter.format(LocalTime.now()));
        textArea.addEventHandler(KeyEvent.KEY_RELEASED, e->OnKeyReleaseCheckText());
    }

    public void OnKeyReleaseCheckText(){
        String text = textArea.getText();
        boolean disableButton = text.isEmpty();
        okButton.setDisable(disableButton);
    }

    public void OnClick_OKButton(){
        String TABLE_NAME="timeline";
        String USER_NAME="Kiran";
        String TEXT_DATA=textArea.getText();
        String DATE= LocalDate.now().toString();
        String TIME = formatter.format(LocalTime.now());
        String ID;

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            statement.execute("INSERT INTO "+ TABLE_NAME + " (user,date,time,text) VALUES('" + USER_NAME + "','" + DATE + "','"+ TIME + "','" + TEXT_DATA + "')" );
            ResultSet res = statement.executeQuery("SELECT ID FROM timeline WHERE DATE='"+DATE+"' AND TIME='"+TIME+"' AND USER='"+USER_NAME+"';");
            res.next();
            ID = res.getString("ID");
            Controller.entries.add(0,new FeedBox(ID,DATE,TIME,TEXT_DATA));
            System.out.println(Controller.entries);
            statement.close();
            conn.close();
        }catch (SQLException e){
            System.out.println("MySQL db conn error");
            e.printStackTrace();
        }
        System.out.println("NewEntry Window closed with OK button");

    }

}

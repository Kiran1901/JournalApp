package App;

import Connectivity.ConnectionClass;
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

public class EditEntryController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private String id;
    private Button okButton;
    private String oldText;

    @FXML
    public Text timeText,dateText;
    public TextArea textArea;

    public EditEntryController(String ID, Dialog dialog){
        this.id = ID;
        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
    }

    public void initialize(){

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE user='Kiran' AND id="+ Integer.parseInt(id) + ";" );
            list.next();
            timeText.setText(list.getString("time"));
            dateText.setText(list.getString("date"));
            textArea.setText(list.getString("text"));
            oldText=list.getString("text");
            textArea.addEventHandler(KeyEvent.KEY_RELEASED, e->OnKeyReleaseCheckText());
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException");
        }

    }

    public void OnKeyReleaseCheckText(){
        String text = textArea.getText();
        boolean disableButton = (text.equals(oldText) || text.isEmpty());
        okButton.setDisable(disableButton);
    }

    public void OnClick_OKButton(FeedBox feedBox){
        String TABLE_NAME="timeline";
        String USER_NAME="Kiran";
        String TEXT_DATA = textArea.getText();

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            statement.execute("UPDATE timeline SET text=" + "'" + TEXT_DATA + "'" + " WHERE ID=" + id + " AND user=" + "'"+USER_NAME + "';");
            statement.close();
            conn.close();
        }catch (SQLException e){
            System.out.println("MySQL db conn error");
            e.printStackTrace();
        }
        int index=Controller.entries.indexOf(feedBox);
        Controller.entries.add(index,new FeedBox(id,dateText.getText(),timeText.getText(),textArea.getText()));
        Controller.entries.remove(index+1);
        System.out.println("onClick:Button@OKButton");
        System.out.println("EditEntry Dialog closed with OK button");
        System.out.println(Controller.entries);
    }

}

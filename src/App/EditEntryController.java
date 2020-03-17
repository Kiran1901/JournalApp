package App;

import Bean.TimelineBean;
import Connectivity.ConnectionClass;
import Connectivity.TimelineDao;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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

        TimelineDao dao = new TimelineDao();
        TimelineBean timelineBean = new TimelineBean();
        timelineBean = dao.selectEntryByNameId(Integer.parseInt(id));
        timeText.setText(timelineBean.getTime());
        dateText.setText(timelineBean.getDate());
        textArea.setText(timelineBean.getText());
        oldText=timelineBean.getText();
        textArea.addEventHandler(KeyEvent.KEY_RELEASED, e->OnKeyReleaseCheckText());

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
        String id = feedBox.get_id();

        TimelineBean timelineBean = new TimelineBean();
        timelineBean.setUser(USER_NAME);
        timelineBean.setText(TEXT_DATA);
        timelineBean.setId(Integer.parseInt(id));
        TimelineDao dao = new TimelineDao();
        dao.updateEntryByIdUser(timelineBean);
        for (FeedBox boxes:Controller.entries){
            if(boxes.get_id().equals(id)){
                boxes.setTextField(TEXT_DATA);break;
            }
        }
        if(Controller.datewiseEntry.contains(feedBox)){
            Controller.datewiseEntry.get(Controller.datewiseEntry.indexOf(feedBox)).setTextField(TEXT_DATA);
        }

        System.out.println("onClick:Button@OKButton");
        System.out.println("EditEntry Dialog closed with OK button");
        System.out.println("list:"+ Controller.entries);
        System.out.println("datewise:" + Controller.datewiseEntry);
    }

}
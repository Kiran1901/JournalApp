package App;

import Connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.*;


public class Controller {

    @FXML
    VBox feedVBox;
    @FXML
    Pane calendarPane;
    @FXML
    VBox calendarVBox;
    int calendarCount;

    @FXML
    VBox entriesList;

    @FXML
    public void initialize(){

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE user='Kiran';" );
            while (list.next()){
                entriesList.getChildren().add(new FeedBox(list.getString("date"),list.getString("time"),list.getString("text")));
            }
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException");
        }


    }

    @FXML
    public void OnClick_newEntryButton(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("newEntryWindow.fxml"));
        Stage newEntryWindow = new Stage();
        newEntryWindow.setTitle("New Entry");
        newEntryWindow.setScene(new Scene(root, 600, 433));
        newEntryWindow.setResizable(false);

        newEntryWindow.show();

        newEntryWindow.setOnCloseRequest(e -> {
            newEntryWindow.close();
            System.out.println("NewEntry Window Exited with X button");
        });
        System.out.println("onClick:Button@newEntryButton");
    }

    @FXML
    public void OnClickEntry(ActiveEvent event){

    }

    public void loadCalendar(Event event) {
        calendarCount++;
        if(calendarCount==1) {
            System.out.println("onClick:Pane@Calendar");
            VBox vb = new FullCalendarView(YearMonth.now()).getView();
            calendarVBox.getChildren().add(vb);
        }
    }

//    public void closeCalendar(Event event) {
//        System.out.println("Calendar Closed");
//    }
}
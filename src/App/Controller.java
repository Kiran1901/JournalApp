package App;

import Connectivity.ConnectionClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.*;


public class Controller {


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
                entriesList.getChildren().add(new FeedBox(list.getString("ID"),list.getString("date"),list.getString("time"),list.getString("text")));
            }
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException");
        }

    }

    @FXML
    public void OnClick_newEntryButton(){
        try {
            Dialog<ButtonType> newEntryWindow = new Dialog<>();
            newEntryWindow.initOwner(entriesList.getScene().getWindow());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("NewEntryDialog.fxml"));
            newEntryWindow.getDialogPane().getButtonTypes().add(ButtonType.OK);
            newEntryWindow.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            NewEntryController newEntryController = new NewEntryController(newEntryWindow);
            loader.setController(newEntryController);
            newEntryWindow.getDialogPane().setContent(loader.load());

            Optional<ButtonType> res = newEntryWindow.showAndWait();
            if(res.isPresent() && res.get()==ButtonType.OK){
                newEntryController.OnClick_OKButton();
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
        System.out.println("onClick:Button@newEntryButton");
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
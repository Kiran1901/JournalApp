package App;

import Connectivity.ConnectionClass;
import com.mysql.jdbc.exceptions.MySQLQueryInterruptedException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;


public class Controller {

    public static ObservableList<FeedBox> entries;
    public static ObservableMap<String,FeedBox> entriesMap;
    public static ObservableList<FeedBox> datewiseEntry;
    public static LocalDate date;

    @FXML
    Pane calendarPane;
    @FXML
    VBox calendarVBox;
    @FXML
    VBox internalVBox;
    int calendarCount;

    @FXML
    VBox entriesList;

    @FXML
    public void initialize(){
        entries = FXCollections.observableArrayList();
        datewiseEntry = FXCollections.observableArrayList();
        try {

            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE user='Kiran' ORDER BY ID DESC;" );
            while (list.next()){
                entries.add(new FeedBox(list.getString("ID"),list.getString("date"),list.getString("time"),list.getString("text")));
            }
            statement.close();
            conn.close();

            /*entriesMap.addListener((MapChangeListener.Change<? extends String,?extends FeedBox> changes)->{
                if (changes.wasAdded()){
                    System.out.println("Added");
                }else if (changes.wasRemoved()){
                    System.out.println("Removed");
                }
            });
*/

            entries.addListener((ListChangeListener.Change<? extends FeedBox> change) -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        entriesList.getChildren().add(change.getAddedSubList().get(0));
                        System.out.println("Added");
                    }else if (change.wasRemoved()) {
                        entriesList.getChildren().remove(change.getRemoved().get(0));
                        System.out.println("Removed");
                    }else if(change.wasUpdated()){
                        entriesList.getChildren().set(change.getFrom(),change.getList().get(change.getFrom()));
                        System.out.println("Updated");
                    }
                }
            });

            entriesList.getChildren().addAll(entries);
            System.out.println("entriesList:"+entriesList.getChildren());

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
            loader.setLocation(getClass().getResource("/FXMLFiles/NewEntryDialog.fxml"));
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
        datewiseEntry.clear();
        calendarCount++;
        if(calendarCount==1) {
            System.out.println("onClick:Pane@Calendar");
            VBox vb = new FullCalendarView(YearMonth.now()).getView();
            calendarVBox.getChildren().add(vb);
        }
        Bindings.bindContent(internalVBox.getChildren(),datewiseEntry);

    }

}

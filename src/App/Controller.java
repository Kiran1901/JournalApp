package App;

import Connectivity.ConnectionClass;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
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

    public static ObservableList<Node> entries;
    public static ObservableList<Node> datewiseEntry;
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

            entriesList.getChildren().addAll(entries);
            Bindings.bindContentBidirectional(entriesList.getChildren(),entries);

            System.out.println(entriesList.getChildren());

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
        Bindings.bindContentBidirectional(internalVBox.getChildren(),datewiseEntry);

    }

   /* public void showEntry()
    {
//        System.out.println("Date is "+date);
        try {
            System.out.println("Datednkwqjdjkkqwjd is"+date);
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE date='"+date+"';");
            while (list.next()){
                System.out.println("text "+list.getString("text"));
                internalVBox.getChildren().add(new FeedBox(list.getString("ID"),list.getString("date"),list.getString("time"),list.getString("text")));
            }
            statement.close();
            conn.close();
        }catch (SQLException s){
            s.printStackTrace();
            System.out.println("SQLException");
        }

    }*/

}

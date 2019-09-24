package App;

import Bean.TimelineBean;
import Connectivity.ConnectionClass;
import Connectivity.TimelineDao;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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

//    public static ObservableList<Node> entries;
//    public static ObservableList<Node> datewiseEntry;
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
    public void initialize() {
        entries = FXCollections.observableArrayList();
        datewiseEntry = FXCollections.observableArrayList();

//        datewiseEntry.addListener(new ListChangeListener<Node>() {
//            @Override
//            public void onChanged(Change<? extends Node> c) {
//                if (c.next()) {
//
//                }
//            }
//        });

//            ConnectionClass connectionClass = new ConnectionClass();
        Connection conn = ConnectionClass.getConnection();
//            Statement statement = conn.createStatement();
        TimelineDao dao = new TimelineDao();
        List<TimelineBean> list = dao.selectEntryByName();
        for (TimelineBean x : list) {
//                int i=x.getId();
//                Integer.toString(i);
            entries.add(new FeedBox(Integer.toString(x.getId()), x.getDate(), x.getTime(), x.getText()));
        }
        entriesList.getChildren().addAll(entries);
        Bindings.bindContentBidirectional(entriesList.getChildren(), entries);
        System.out.println(entriesList.getChildren());

//            while (list.next()){
//                entries.add(new FeedBox(list.getString("ID"),list.getString("date"),list.getString("time"),list.getString("text")));
//            }
//            statement.close();
//            conn.close();
//
//            entriesList.getChildren().addAll(entries);
//            Bindings.bindContentBidirectional(entriesList.getChildren(), entries);
//
//            System.out.println(entriesList.getChildren());

//        }catch (SQLException e){
//            e.printStackTrace();
//            System.out.println("SQLException");
//        }

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
        Bindings.bindContentBidirectional(internalVBox.getChildren(),(ObservableList<Node>) datewiseEntry);
//        Bindings.bindContentBidirectional();
    }

}

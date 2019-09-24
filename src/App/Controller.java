package App;

import Bean.TimelineBean;
import Connectivity.TimelineDao;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    public void initialize() {
        entries = FXCollections.observableArrayList();
        datewiseEntry = FXCollections.observableArrayList();

//        Connection conn = ConnectionClass.getConnection();
//            Statement statement = conn.createStatement();
        TimelineDao dao = new TimelineDao();
        List<TimelineBean> list = dao.selectEntryByName();
        for (TimelineBean x : list) {
//                int i=x.getId();
//                Integer.toString(i);
            entries.add(new FeedBox(Integer.toString(x.getId()), x.getDate(), x.getTime(), x.getText()));

        }
        entriesList.getChildren().addAll(entries);
        entries.addListener((ListChangeListener.Change<? extends FeedBox> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    entriesList.getChildren().add(0,change.getAddedSubList().get(0));
                    System.out.println("Added");
                } else if (change.wasRemoved()) {
                    entriesList.getChildren().remove(change.getRemoved().get(0));
                    System.out.println("Removed");
                } else if (change.wasUpdated()) {
                    entriesList.getChildren().set(change.getFrom(), change.getList().get(change.getFrom()));
                    System.out.println("Updated");
                }
            }
        });
        System.out.println("entriesList:" + entriesList.getChildren());

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

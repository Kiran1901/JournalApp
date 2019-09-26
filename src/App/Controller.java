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

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    @FXML
    ChoiceBox typeChoiceBox;

    private int calendarCount;

    @FXML
    VBox entriesList;

    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("New Journal Entry","New Account Entry");
//        typeChoiceBox.
        typeChoiceBox.setOnAction(e-> OnSelectNewEnry());
        typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
        entries = FXCollections.observableArrayList();
        datewiseEntry = FXCollections.observableArrayList();


        TimelineDao dao = new TimelineDao();
        List<TimelineBean> list = dao.selectEntryByName();
        for (TimelineBean x : list) {
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



    public void onClick_NewEntryButton2()
    {
        try
        {
            Dialog<ButtonType> newEntry2Window = new Dialog<>();
            newEntry2Window.initOwner(entriesList.getScene().getWindow());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXMLFiles/NewEntry2Dialog.fxml"));
            newEntry2Window.getDialogPane().getButtonTypes().add(ButtonType.OK);
            newEntry2Window.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            NewEntryController2 newEntryController2 = new NewEntryController2(newEntry2Window);
            loader.setController(newEntryController2);
            newEntry2Window.getDialogPane().setContent(loader.load());

            Optional<ButtonType> res = newEntry2Window.showAndWait();
            if(res.isPresent() && res.get()==ButtonType.OK){
//                newEntryController2.OnClick_OKButton();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
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

    void OnSelectNewEnry(){
        if(typeChoiceBox.getSelectionModel().isSelected(0)){
            System.out.println("1st clicked");
            typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
            OnClick_newEntryButton();
        }else{
            if(typeChoiceBox.getSelectionModel().isSelected(1)) {
                System.out.println("2nd clicked");
                typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
                onClick_NewEntryButton2();
            }
        }
    }

}

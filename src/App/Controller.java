package App;

import Bean.DataConversion;
import Bean.TimelineBean;
import Connectivity.ConnectionClass;
import Connectivity.TimelineDao;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
//import org.jfree.chart.JFreeChart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;


public class Controller {

    public static ObservableList<FeedBox> entries;
    public static ObservableMap<String, FeedBox> entriesMap;
    public static ObservableList<FeedBox> datewiseEntry;
    public static ObservableList<String> mailRequiredList;
    public static ObservableList<TransactionBox> transactionBoxeList;
    public static LocalDate date;
    public Button sendMailButton;
    public VBox sendMailVBox;

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
    VBox accountsList;
    @FXML
    VBox mailListVBox;
    @FXML
    Button mailSubmitButton;
    @FXML
    DatePicker startDate1,endDate1,startDate2,endDate2;
    @FXML
    VBox accountEntriesChart,expensesChart;
    @FXML
    Button showAccountEntriesChart,showExpensesChart;

    @FXML
    public void initialize() {

        typeChoiceBox.getItems().addAll("New Journal Entry", "New Account Entry");
        typeChoiceBox.setOnAction(e -> OnSelectNewEnry());
        typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
        entries = FXCollections.observableArrayList();
        datewiseEntry = FXCollections.observableArrayList();
        mailRequiredList = FXCollections.observableArrayList();
//        mailSubmitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> sendMailToAll());
        mailSubmitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendMailToAll());
        transactionBoxeList = FXCollections.observableArrayList();

        initTimelineTab();
        Platform.runLater(() -> initAccountLogsTab());
        Platform.runLater(() -> initMailTab());
//        mailListVBox.getChildren().add(mailSubmitButton);
    }

    @FXML
    public void sendMailToAll() {

    }

    @FXML
    public void OnClick_newEntryButton() {
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
            if (res.isPresent() && res.get() == ButtonType.OK) {
                newEntryController.OnClick_OKButton();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("onClick:Button@newEntryButton");
    }

    public void onClick_NewEntryButton2() {
        try {
            Dialog<ButtonType> newEntry2Window = new Dialog<>();
            newEntry2Window.initOwner(entriesList.getScene().getWindow());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXMLFiles/NewEntryDialog2.fxml"));
            newEntry2Window.getDialogPane().getButtonTypes().add(ButtonType.OK);
            newEntry2Window.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            NewEntryController2 newEntryController2 = new NewEntryController2(newEntry2Window);
            loader.setController(newEntryController2);
            Button okButton = ((Button) newEntry2Window.getDialogPane().lookupButton(ButtonType.OK));
            okButton.addEventFilter(ActionEvent.ACTION, e -> {
                if (!newEntryController2.checkIsEmpty()) {
                    e.consume();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("You forgot something!!!");
                    alert.showAndWait();
                } else {
                    System.out.println("Everything good");
                    newEntryController2.OnClick_OKButton();
                }
            });
            newEntry2Window.getDialogPane().setContent(loader.load());
            newEntry2Window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void OnSelectNewEnry() {
        if (typeChoiceBox.getSelectionModel().isSelected(0)) {
            System.out.println("1st clicked");
            typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
            OnClick_newEntryButton();
        } else {
            if (typeChoiceBox.getSelectionModel().isSelected(1)) {
                System.out.println("2nd clicked");
                typeChoiceBox.setValue(typeChoiceBox.getItems().get(0));
                onClick_NewEntryButton2();
            }

        }
    }

    public void initTimelineTab() {
        TimelineDao dao = new TimelineDao();
        List<TimelineBean> list = dao.selectEntryByName();
        for (TimelineBean x : list) {
            entries.add(new FeedBox(Integer.toString(x.getId()), x.getDate(), x.getTime(), x.getText()));

        }
        entriesList.getChildren().addAll(entries);
        entries.addListener((ListChangeListener.Change<? extends FeedBox> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    entriesList.getChildren().add(0, change.getAddedSubList().get(0));
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

    public void initAccountLogsTab() {

        transactionBoxeList.addListener((ListChangeListener.Change<? extends TransactionBox> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    accountsList.getChildren().add(0, change.getAddedSubList().get(0));
//                    System.out.println("ACC ADD");
                } else if (change.wasRemoved()) {
                    accountsList.getChildren().remove(change.getRemoved().get(0));
//                    System.out.println("ACC DELETE");
                }
              /*  else if (change.wasUpdated()) {
//                    accountsList.getChildren().set(change.getFrom(), change.getList().get(change.getFrom()));
                    System.out.println("ACC UPDATE");
                }       */
            }
        });


        try {
            Statement statement = ConnectionClass.getConnection().createStatement();
            ResultSet res1 = statement.executeQuery("SELECT account_log.date,account_log.time,account_log.data as 'acc_data',expenses.data as 'exp_data' " +
                    "FROM account_log " +
                    "LEFT JOIN expenses ON " +
                    "account_log.date=expenses.date and account_log.time=expenses.time " +
                    "UNION " +
                    "SELECT expenses.date,expenses.time,account_log.data as 'acc_data',expenses.data as 'exp_data' " +
                    "FROM account_log " +
                    "RIGHT JOIN expenses ON " +
                    "account_log.date=expenses.date and account_log.time=expenses.time;");


            while (res1.next()) {
                makeAccount_logWidget(res1.getString("date"), res1.getString("time"), res1.getString("acc_data"), res1.getString("exp_data"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        accountsList.getChildren().addAll();
    }

    public void loadCalendar(Event event) {
        datewiseEntry.clear();
        calendarCount++;
        if (calendarCount == 1) {
            System.out.println("onClick:Pane@Calendar");
            VBox vb = new FullCalendarView(YearMonth.now()).getView();
            calendarVBox.getChildren().add(vb);
        }
        Bindings.bindContent(internalVBox.getChildren(), datewiseEntry);
    }

    public void initMailTab() {

        mailRequiredList.addListener((ListChangeListener.Change<? extends String> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {

                    mailListVBox.getChildren().add((new HBox(15, new Text(change.getAddedSubList().get(0)), new TextField())));
                }
            }
        });

        mailListVBox.setSpacing(10);
        mailListVBox.setPadding(new Insets(20, 20, 20, 20));
        try {
            Statement statement = ConnectionClass.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT NAME FROM empty_mail");
            while (res.next()) {
                Controller.mailRequiredList.add(res.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mailSubmitButton.setOnAction(e -> {
            Iterator iterator = mailListVBox.getChildren().iterator();
            HBox hBox;
            String name, email;
            while (iterator.hasNext()) {
                hBox = ((HBox) iterator.next());
                name = ((Text) hBox.getChildren().get(0)).getText();
                email = ((TextField) hBox.getChildren().get(1)).getText();
                if (!email.isEmpty()) {
                    try {
                        Statement statement = ConnectionClass.getConnection().createStatement();
                        statement.executeUpdate("INSERT INTO mailing_list (name, email) VALUES ('" + name + "','" + email + "');");
                        statement.executeUpdate("DELETE from empty_mail where name='" + name + "';");
                        iterator.remove();
                        Controller.mailRequiredList.remove(name);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void makeAccount_logWidget(String date, String time, String accJsonString, String expJsonString) {
        List<AccountEntryBox> accountBoxes = new ArrayList<>();
        List<AccountEntryBox> expenseBoxes = new ArrayList<>();
        int count;
        JSONObject json;
        JSONArray arr;
        DataConversion dataConversion;
        AccountEntryBox accountEntryBox;
        TransactionBox transactionBox;
        try {
            if (accJsonString != null) {
                json = new JSONObject(accJsonString);
                count = json.getInt("count");
                arr = json.getJSONArray("data");
                int i1 = 0;
                while (i1 < count) {
                    dataConversion = new DataConversion(arr.getJSONObject(i1));
                    accountEntryBox = new AccountEntryBox(dataConversion);
                    accountEntryBox.disableAllFields();
                    accountBoxes.add(accountEntryBox);
                    i1 += 1;
                }
            }
            if (expJsonString != null) {
                json = new JSONObject(expJsonString);
                count = json.getInt("count");
                arr = json.getJSONArray("data");
                int i1 = 0;
                while (i1 < count) {
                    dataConversion = new DataConversion(arr.getJSONObject(i1));
                    accountEntryBox = new AccountEntryBox(dataConversion);
                    accountEntryBox.disableAllFields();
                    expenseBoxes.add(accountEntryBox);
                    i1 += 1;
                }
            }
            transactionBox = new TransactionBox(date, time, accountBoxes, expenseBoxes);
            transactionBoxeList.add(0, transactionBox);
//            accountsList.getChildren().add(transactionBox);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadCharts(Event event) {
        accountEntriesChart.getChildren().clear();
        showAccountEntriesChart.setOnAction(e -> {
            accountEntriesChart.getChildren().clear();
            if (startDate1.getValue() != null && endDate1.getValue() != null) {
                System.out.println("Upper button worked");
                JFreeCharts accountEntriesChart = new JFreeCharts(new CategoryAxis(), new NumberAxis());
                List<XYChart.Series> dataList = accountEntriesChart.createData(startDate1.getValue().toString(), endDate1.getValue().toString());
                for (XYChart.Series<String, Number> s : dataList) {
                    accountEntriesChart.getData().add(s);
                }
                accountEntriesChart.autosize();
                accountEntriesChart.setVisible(true);
                this.accountEntriesChart.getChildren().add(accountEntriesChart);
            }
        });

        showExpensesChart.setOnAction(e-> {
            if (startDate2.getValue() != null && endDate2.getValue() != null) {
                System.out.println("Lower button worked");
                expensesChart.getChildren().clear();
                JFreeCharts expensesChart = new JFreeCharts(new CategoryAxis(), new NumberAxis());
                XYChart.Series series = expensesChart.createExpenseSeries(startDate2.getValue().toString(), endDate2.getValue().toString());
                expensesChart.getData().add(series);
                expensesChart.autosize();
                expensesChart.setVisible(true);
                this.expensesChart.getChildren().add(expensesChart);
            }
        });

    }


}

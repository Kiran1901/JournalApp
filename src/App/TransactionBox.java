package App;

import Bean.DataConversion;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class TransactionBox extends VBox {

    private int id;
    private Text dateField,timeField,giveTake,expenses;
    private VBox topVBox,bottomVBox;
    private Button editTransactionBox;

    public TransactionBox(String date, String time, List<AccountEntryBox> accountEntryBoxList, List<AccountEntryBox> expenseList){

        dateField=new Text(date);
        timeField=new Text(time);
        giveTake=new Text("Account Entries: ");
        expenses=new Text("Expenses: ");
        editTransactionBox = new Button("Edit");

        dateField.setFont(Font.font("Times New Roman Bold",14));
        timeField.setFont(Font.font("Times New Roman Bold",14));
        giveTake.setFont(Font.font("Times New Roman Italic",12));
        expenses.setFont(Font.font("Times New Roman Italic",12));

        this.setPadding(new Insets(10,10,10,10));
        this.setPrefSize(700,150);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->setEditButtonVisibility(true));
        this.addEventHandler(MouseEvent.MOUSE_EXITED,e->setEditButtonVisibility(false));

        editTransactionBox.setOnAction(e->onClickEditButton());

        HBox header = new HBox(15,editTransactionBox,dateField,timeField);
        header.setPadding(new Insets(5,15,5,10));
        header.fillHeightProperty();
        header.setAlignment(Pos.TOP_RIGHT);

        topVBox = new VBox();
        topVBox.setPadding(new Insets(10,10,10,10));
        topVBox.setSpacing(5);
        topVBox.getChildren().addAll(accountEntryBoxList);

        bottomVBox = new VBox();
        bottomVBox.setPadding(new Insets(10,10,10,10));
        bottomVBox.setSpacing(5);
        bottomVBox.getChildren().addAll(expenseList);

        getChildren().add(header);
        if(!accountEntryBoxList.isEmpty()) getChildren().addAll(giveTake,topVBox);
        if (!expenseList.isEmpty()) getChildren().addAll(expenses,bottomVBox);
    }


    public Text getDateField() {
        return dateField;
    }

    public void setDateField(Text dateField) {
        this.dateField = dateField;
    }

    public Text getTimeField() {
        return timeField;
    }

    public void setTimeField(Text timeField) {
        this.timeField = timeField;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public VBox getTopVBox() {
        return topVBox;
    }

    public void setTopVBox(List list) {
        this.topVBox.getChildren().addAll(list);
    }

    public VBox getBottomVBox() {
        return bottomVBox;
    }

    public void setAllDisable(){
        topVBox.setDisable(true);
        bottomVBox.setDisable(true);
    }

    public void clearAllChildren(){
        topVBox.getChildren().clear();
        bottomVBox.getChildren().clear();
    }

    public void setBottomVBox(List list) {
        this.bottomVBox.getChildren().addAll(list);
    }

    public void setEditButtonVisibility(boolean flag){
        editTransactionBox.setVisible(flag);
    }

    public void onClickEditButton() {
        try {
            Dialog<ButtonType> editEntryWindow2 = new Dialog<>();
            editEntryWindow2.initOwner(this.getScene().getWindow());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXMLFiles/EditEntryDialog2.fxml"));
            editEntryWindow2.getDialogPane().getButtonTypes().add(ButtonType.OK);
            editEntryWindow2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            EditEntryController2 editEntryController2 = new EditEntryController2(this,editEntryWindow2);
            loader.setController(editEntryController2);

            Button okButton = ((Button) editEntryWindow2.getDialogPane().lookupButton(ButtonType.OK));
            okButton.addEventFilter(ActionEvent.ACTION, e->{
                if (!editEntryController2.checkIsEmpty()) {
                    e.consume();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("You forgot something!!!");
                    alert.showAndWait();
                }else {
                    System.out.println("Everything good");
                    editEntryController2.OnClick_OKButton(this);
                }
            });

            editEntryWindow2.getDialogPane().setContent(loader.load());
            Optional<ButtonType> res = editEntryWindow2.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

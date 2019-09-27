package App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;


public class TransactionBox extends VBox {

    private int id;
    private Text dateField,timeField,giveTake,expenses;
    private Button editTransactionBox;

    public TransactionBox(String date, String time, List<AccountEntryBox> accountEntryBoxList,List<AccountEntryBox> expenseList){

        dateField.setText(date);
        timeField.setText(time);
        giveTake.setText("Account Entries: ");
        expenses.setText("Expenses: ");
        editTransactionBox = new Button("Edit");

        this.setPadding(new Insets(10,10,10,10));
        this.setPrefSize(700,150);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->setEditButtonVisibility(true));
        this.addEventHandler(MouseEvent.MOUSE_EXITED,e->setEditButtonVisibility(false));


        HBox header = new HBox(15,editTransactionBox,dateField,timeField);
        header.setPadding(new Insets(5,15,5,10));
        header.fillHeightProperty();
        header.setAlignment(Pos.TOP_RIGHT);

        VBox topVBox = new VBox();
        topVBox.setPadding(new Insets(10,10,10,10));
        topVBox.setSpacing(5);
        topVBox.getChildren().addAll(accountEntryBoxList);

        VBox bottomVBox = new VBox();
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

    public void setEditButtonVisibility(boolean flag){
        editTransactionBox.setVisible(flag);
    }
}

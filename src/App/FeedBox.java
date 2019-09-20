package App;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Optional;


public class FeedBox extends VBox{

    private String id;
    private Text dateField,timeField;
    private TextArea textField;
    private Button editEntry;

    public FeedBox(String feed_id,String date,String time,String text){
        id = new String(feed_id);
        dateField = new Text(date);
        timeField = new Text(time);
        textField = new TextArea(text);
        editEntry = new Button("Edit");

        dateField.setTextAlignment(TextAlignment.LEFT);
        dateField.setFont(Font.font("Times New Roman Italic",14));
        timeField.setTextAlignment(TextAlignment.LEFT);
        timeField.setFont(Font.font("Times New Roman Italic",14));
        textField.setPrefSize(200,200);
        textField.setEditable(false);
        editEntry.setVisible(false);

        editEntry.addEventHandler(MouseEvent.MOUSE_CLICKED,e ->OnClick_editEntry());

        HBox header = new HBox(15,editEntry,dateField,timeField);
        header.setPadding(new Insets(5,15,5,10));
        header.fillHeightProperty();
        header.setAlignment(Pos.TOP_RIGHT);
//        textField.setPrefHeight(100);

//        feedbox = new VBox(10,header,textField);
        this.setPadding(new Insets(10,10,10,10));
        this.setPrefSize(700,150);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED,e->setEditEntryVisibility(true));
        this.addEventHandler(MouseEvent.MOUSE_EXITED,e->setEditEntryVisibility(false));
        this.setId(id);

        getChildren().addAll(header,textField);
    }

    public String getDateField() {
        return dateField.getText();
    }

    public String getTimeField() {
        return timeField.getText();
    }

    public String getTextField() {
        return textField.getText();
    }

    public String get_id(){
        return this.id;
    }

    public String toString(){
        return textField.getText();
    }

    public void setEditEntryVisibility(boolean flag){
        editEntry.setVisible(flag);
    }

    public void OnClick_editEntry(){
        try {
            Dialog<ButtonType> editEntryWindow = new Dialog<>();
            editEntryWindow.initOwner(this.getScene().getWindow());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXMLFiles/EditEntryDialog.fxml"));
            editEntryWindow.getDialogPane().getButtonTypes().add(ButtonType.OK);
            editEntryWindow.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            EditEntryController editEntryController = new EditEntryController(this.id,editEntryWindow);
            loader.setController(editEntryController);
            editEntryWindow.getDialogPane().setContent(loader.load());

            Optional<ButtonType> res = editEntryWindow.showAndWait();
            if(res.isPresent() && res.get()==ButtonType.OK){
                editEntryController.OnClick_OKButton(this);
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void set_Id(String id) {
        this.id = id;
    }

    public void setDateField(String date) {
        this.dateField.setText(date);
    }

    public void setTimeField(String time) {
        this.timeField.setText(time);
    }

    public void setTextField(String text) {
        this.textField.setText(text);
    }
}

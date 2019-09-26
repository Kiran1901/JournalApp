package App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class NewEntryController2 {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Button okButton;

    @FXML
    public Text timeText2, dateText2;
    //    public TextArea textArea;
    @FXML
    public VBox bottomVBox2, outerVBox, internalTopVBox, mainVBox2, internalBottomVBox;
    @FXML
    public CheckBox checkBox1, checkBox2;
    @FXML
    public Button button1, button2;

    public NewEntryController2(Dialog dialog) {
        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
    }

    public void initialize() {
        dateText2.setText(LocalDate.now().toString());
        timeText2.setText(formatter.format(LocalTime.now()));
        checkBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                System.out.println(checkBox1.isSelected());
                if (checkBox1.isSelected()) {
                    button1.setDisable(false);
                } else {
                    button1.setDisable(true);
                }
            }
        });

    }

//    public boolean checkIsEmpty() {
////        while(internalTopVBox.getChildren().iterator().hasNext()){
////            AccountEntryBox abox = ((AccountEntryBox) internalTopVBox.getChildren().iterator().next());
////            TextField person = abox.getPersonName();
////            TextField amt = abox.getAmount();
////            TextField dsc = abox.getDesc();
////            ChoiceBox<String> type = abox.getType();
////            if(person.getText().isEmpty() || amt.getText().isEmpty() || dsc.getText().isEmpty() || type.getText().isEmpty())
////            {
////                okButton.setDisable(true);
////            }
////        }
//    }
}


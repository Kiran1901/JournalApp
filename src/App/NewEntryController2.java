package App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NewEntryController2 {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Button okButton;

    @FXML
    public Text timeText2, dateText2;
    @FXML
    public VBox internalTopVBox, internalBottomVBox;
    @FXML
    public CheckBox checkBox1, checkBox2;
    @FXML
    public Button button1, button2;

    public NewEntryController2(Dialog dialog) {
        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    }

    public void initialize() {
        dateText2.setText(LocalDate.now().toString());
        timeText2.setText(formatter.format(LocalTime.now()));
        button1.setDisable(true);
        button2.setDisable(true);
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> checkIsEmpty());
        checkBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                System.out.println(checkBox1.isSelected());
                if (checkBox1.isSelected()) {
                    button1.setDisable(false);
                } else {
                    button1.setDisable(true);
                    internalTopVBox.getChildren().clear();
                }
            }
        });
        button1.setOnMouseClicked((MouseEvent e)->{
            internalTopVBox.getChildren().add(new AccountEntryBox(0));
        });
        checkBox2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                System.out.println(checkBox2.isSelected());
                if (checkBox2.isSelected()) {
                    button2.setDisable(false);
                } else {
                    button2.setDisable(true);
                    internalBottomVBox.getChildren().clear();
                }
            }
        });
        button2.setOnMouseClicked((MouseEvent e)->{
            internalBottomVBox.getChildren().add(new AccountEntryBox(1));
        });

    }

    public boolean checkIsEmpty() {
        boolean flag=true;
        Iterator iterator1 = internalTopVBox.getChildren().iterator();
        Iterator iterator2 = internalBottomVBox.getChildren().iterator();
        AccountEntryBox abox,bbox;
        while (iterator1.hasNext() && flag) {
            abox = ((AccountEntryBox) iterator1.next());
             flag = flag && abox.checkIsAboxEmpty();
        }
        while (flag && iterator2.hasNext()){
            bbox = ((AccountEntryBox) iterator2.next());
            flag = flag && bbox.checkIsAboxEmpty();
        }
        System.out.println("f value : "+flag);
        return flag;
    }
}


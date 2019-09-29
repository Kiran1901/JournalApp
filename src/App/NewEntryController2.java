package App;

import Bean.AccountEntryBean;
import Bean.DataConversion;
import Bean.TimelineBean;
import Connectivity.AccountEntryDao;
import Connectivity.ConnectionClass;
import Connectivity.TimelineDao;
import javafx.application.Platform;
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
import org.json.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public void OnClick_OKButton(){
        String TABLE_NAME="timeline";
        String USER_NAME="Kiran";
        String DATE= LocalDate.now().toString();
        String TIME = formatter.format(LocalTime.now());
        String ID;
        Iterator iterator1 = internalTopVBox.getChildren().iterator();
        Iterator iterator2 = internalBottomVBox.getChildren().iterator();

        int t_type=0;float amt;int cnt=0;
        AccountEntryBox abox=null;
        String pName,desc,type;

        JSONArray jsonArray = new JSONArray();
        JSONObject finalJsonObject = new JSONObject();
        List<String> personMailList = new ArrayList<>();
        while(iterator1.hasNext())
        {
             abox = (AccountEntryBox) iterator1.next();
            pName = abox.getPersonName().getText();
            amt = Integer.parseInt(abox.getAmount().getText());
            desc = abox.getDesc().getText();
            type = abox.getType().getValue();
            t_type = type.equals("Give")?0:1;
            personMailList.add(pName);
            DataConversion dataConversion = new DataConversion(pName,amt,desc,0,t_type);
            cnt++;
            jsonArray.put(dataConversion.convertToJson());
        }
        try {
            finalJsonObject.put("count",cnt);
            finalJsonObject.put("data",jsonArray);
        }catch (JSONException e){
            System.out.println("exception in building elements of json object");
        }

        System.out.println("yo we are in Onclick");
        AccountEntryBean accountEntryBean = new AccountEntryBean();
        accountEntryBean.setDate(DATE);
        accountEntryBean.setTime(TIME);
        accountEntryBean.setJson(finalJsonObject);
        accountEntryBean.setUser(USER_NAME);
        AccountEntryDao accountEntryDao = new AccountEntryDao();
        accountEntryDao.insertEntry(accountEntryBean);

        Platform.runLater(()->{
            for(String name : personMailList){
                try {
                    Statement statement = ConnectionClass.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT name from mailing_list where LOWER(name)=LOWER('"+name+"');");
                    if (!res.next()){
                        statement.executeUpdate("INSERT into empty_mail (name) value ('"+name+"');");
                        Controller.mailRequiredList.add(name);
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

//        TimelineDao dao = new TimelineDao();
//        dao.insertEntry(timelineBean,TABLE_NAME);
//        ID = Integer.toString(dao.getIdFromAll(timelineBean,TABLE_NAME));
//        Controller.entries.add(0,new FeedBox(ID,DATE,TIME,TEXT_DATA));
//        System.out.println(Controller.entries);
//        System.out.println("NewEntry Window closed with OK button");

    }
}


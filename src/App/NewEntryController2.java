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
import javafx.scene.control.*;
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
        return flag;
    }

    public void OnClick_OKButton(){
        String USER_NAME = "Kiran";

        String DATE= LocalDate.now().toString();
        String TIME = formatter.format(LocalTime.now());
        String ID;
        Iterator iterator1 = internalTopVBox.getChildren().iterator();
        Iterator iterator2 = internalBottomVBox.getChildren().iterator();

        int hbox_type=0;float amt;int cnt=0;
        AccountEntryBox abox=null;
        String pName,desc,t_type;

        JSONArray jsonArray = new JSONArray();
        JSONObject finalJsonObject = new JSONObject();
        List<String> personMailList = new ArrayList<>();
        while(iterator1.hasNext())
        {
             abox = (AccountEntryBox) iterator1.next();
            pName = abox.getPersonName().getText();
            amt = Integer.parseInt(abox.getAmount().getText());
            desc = abox.getDesc().getText();
            t_type = abox.getT_type().getValue();
            hbox_type = t_type.equals("Give")?0:1;
            personMailList.add(pName);
            DataConversion dataConversion = new DataConversion(pName,amt,desc,0,hbox_type);
            cnt++;
            jsonArray.put(dataConversion.convertToJson());
        }
        try {
            finalJsonObject.put("count",cnt);
            finalJsonObject.put("data",jsonArray);
        }catch (JSONException e){
            System.out.println("exception in building elements of json object");
        }
        AccountEntryDao accountEntryDao = new AccountEntryDao();
        if (cnt>0){
            AccountEntryBean accountEntryBean = new AccountEntryBean();
            accountEntryBean.setDate(DATE);
            accountEntryBean.setTime(TIME);
            accountEntryBean.setJson(finalJsonObject.toString());
            accountEntryBean.setUser(USER_NAME);
            accountEntryDao.insertEntry(accountEntryBean,"account_log");
        }


        JSONArray jsonArray2 = new JSONArray();
        JSONObject finalJsonObject2 = new JSONObject();
        AccountEntryBox bbox;
        cnt=0;
        while(iterator2.hasNext())
        {
            bbox = (AccountEntryBox) iterator2.next();
            pName = bbox.getPersonName().getText();
            amt = Integer.parseInt(bbox.getAmount().getText());
            desc = bbox.getDesc().getText();
            DataConversion dataConversion2 = new DataConversion(pName,amt,desc,1,-1);
            cnt++;
            jsonArray2.put(dataConversion2.convertToJson());
        }
        try {
            finalJsonObject2.put("count",cnt);
            finalJsonObject2.put("data",jsonArray2);
        }catch (JSONException e){
            System.out.println("exception in building elements of json object");
        }
        if(cnt>0){
            AccountEntryBean accountEntryBean2 = new AccountEntryBean();
            accountEntryBean2.setDate(DATE);
            accountEntryBean2.setTime(TIME);
            accountEntryBean2.setJson(finalJsonObject2.toString());
            accountEntryBean2.setUser(USER_NAME);
            accountEntryDao.insertEntry(accountEntryBean2,"expenses");
        }

        DataConversion dc = new DataConversion();
        TransactionBox transactionBox = new TransactionBox(DATE,TIME,dc.jsonToAccountEntryBoxList(finalJsonObject.toString()),dc.jsonToAccountEntryBoxList(finalJsonObject2.toString()));
        transactionBox.setAllDisable();
        Controller.transactionBoxeList.add(transactionBox);

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

    }
}


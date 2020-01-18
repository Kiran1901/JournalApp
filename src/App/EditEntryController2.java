package App;

import Bean.AccountEntryBean;
import Bean.DataConversion;
import Connectivity.AccountEntryDao;
import Connectivity.ConnectionClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class EditEntryController2 {

    @FXML
    public Button addAccountEntriesButton,addExpensesButton;
    @FXML
    public VBox topVBox,bottomVBox;
    @FXML
    Text dateText,timeText;
    @FXML
    Button clearAccountEntriesButton,clearExpensesButton;

//    private Button okButton;
    private TransactionBox transactionBox;

    public EditEntryController2(TransactionBox transactionBox,Dialog dialog){
//        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        this.transactionBox=transactionBox;
    }

    public void initialize(){
        dateText.setText(transactionBox.getDateField().getText());
        timeText.setText(transactionBox.getTimeField().getText());

        AccountEntryBean accountEntryBean = new AccountEntryBean();
        accountEntryBean.setDate(dateText.getText());
        accountEntryBean.setTime(timeText.getText());
        AccountEntryDao dao = new AccountEntryDao();
        accountEntryBean=dao.getAccountEntry(accountEntryBean);

        AccountEntryBean expenseEntryBean = new AccountEntryBean();
        expenseEntryBean.setDate(dateText.getText());
        expenseEntryBean.setTime(timeText.getText());
        expenseEntryBean=dao.getExpenseEntry(expenseEntryBean);

        DataConversion dataConversion = new DataConversion();
        if (accountEntryBean.getJson()!=null){
            topVBox.getChildren().addAll(dataConversion.jsonToAccountEntryBoxList(accountEntryBean.getJson()));

        }
        if (expenseEntryBean.getJson()!=null){
            bottomVBox.getChildren().addAll(dataConversion.jsonToAccountEntryBoxList(expenseEntryBean.getJson()));
        }
        addAccountEntriesButton.setOnMouseClicked((MouseEvent e)->{
            topVBox.getChildren().add(new AccountEntryBox(0));
        });
        addExpensesButton.setOnMouseClicked((MouseEvent e)->{
            bottomVBox.getChildren().add(new AccountEntryBox(1));
        });


        //TODO initialize edit entry 2 dialog
    }

    public void OnClick_OKButton(TransactionBox transactionBox) {
        String USER_NAME = "Kiran";

        String DATE= transactionBox.getDateField().getText();
        String TIME = transactionBox.getTimeField().getText();
        String ID;
        Iterator iterator1 = topVBox.getChildren().iterator();
        Iterator iterator2 = bottomVBox.getChildren().iterator();

        int index=Controller.transactionBoxeList.indexOf(transactionBox);
        DataConversion dc = new DataConversion();

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
            accountEntryDao.updateEntry(accountEntryBean,"account_log");
        }
        Controller.transactionBoxeList.get(index).clearAllChildren();
        Controller.transactionBoxeList.get(index).setTopVBox(dc.jsonToAccountEntryBoxList(finalJsonObject.toString()));

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
            accountEntryDao.updateEntry(accountEntryBean2,"expenses");
        }
        Controller.transactionBoxeList.get(index).setBottomVBox(dc.jsonToAccountEntryBoxList(finalJsonObject2.toString()));
        Controller.transactionBoxeList.get(index).setAllDisable();
        System.out.println(Controller.transactionBoxeList.get(index).getChildren());

        Platform.runLater(()->{
            for(String name : personMailList){
                try {
                    Statement statement = ConnectionClass.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT name from mailing_list where LOWER(name)=LOWER('"+name+"');");
                    if (!res.next()){
                        statement.executeUpdate("INSERT into empty_mail (name) value ('"+name+"');");
                        Controller.mailRequiredList.add(name);
                    }
                    //TODO condition when name without mail is replaced with one which has

                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("EditEntry2 Dialog closed with OK button");

    }

    public boolean checkIsEmpty() {
        boolean flag=true;
        Iterator iterator1 = topVBox.getChildren().iterator();
        Iterator iterator2 = bottomVBox.getChildren().iterator();
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

    @FXML
    public void clearBottomVBox(){
        bottomVBox.getChildren().clear();
    }
    @FXML
    public void clearTopVBox(){
        topVBox.getChildren().clear();
    }
}
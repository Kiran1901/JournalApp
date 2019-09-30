package Bean;

import App.AccountEntryBox;
import javafx.collections.FXCollections;
import javafx.print.Collation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataConversion {

    private String personName;
    private float amount;
    private String description;
    private int type;//type=0 => Account;type=1 => Expense
    private int t_type; //0=> give 1=>take

    public DataConversion(String pName, float amt, String desc, int type, int t_type) {
        personName=pName;
        amount=amt;
        description=desc;
        this.type=type;
        this.t_type =t_type;
    }

    public DataConversion(JSONObject jsonObject){
        try {
            personName=jsonObject.getString("name");
            amount= ((float) jsonObject.getDouble("amount"));
            description = jsonObject.getString("desc");
            type=jsonObject.getInt("type");
            if (type==0) t_type=jsonObject.getInt("t_type");
        }catch (JSONException e){
            System.out.println("Exception in conversion: json to object");
        }
    }

    public DataConversion(){

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getT_type() {
        return t_type;
    }

    public void setT_type(int t_type) {
        this.t_type = t_type;
    }

    public JSONObject convertToJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",personName);
            jsonObject.put("amount",amount);
            jsonObject.put("desc",description);
            jsonObject.put("type",type);
            if(type==0) jsonObject.put("t_type",t_type);
            return jsonObject;
        }catch (JSONException e){
            System.out.println("Exception in conversion: object to json");
        }
        return jsonObject;
    }

    public List<AccountEntryBox> jsonToAccountEntryBoxList(String jsonString){
        List<AccountEntryBox> list =new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            AccountEntryBox accountEntryBox;
            if (jsonObject!=null){
                    int cnt = jsonObject.getInt("count");
                    int i=0;
                    while (i<cnt) {
                        accountEntryBox = new AccountEntryBox(((JSONObject) jsonObject.getJSONArray("data").get(i)).getInt("type"));
                        if(accountEntryBox.getType()==0){
                            accountEntryBox.setT_type(((JSONObject) jsonObject.getJSONArray("data").get(i)).getInt("t_type")==0?"Give":"TAKE");
                        }
                        accountEntryBox.setPersonName(((JSONObject) jsonObject.getJSONArray("data").get(i)).getString("name"));
                        accountEntryBox.setAmount(((JSONObject) jsonObject.getJSONArray("data").get(i)).getString("amount"));
                        accountEntryBox.setDesc(((JSONObject) jsonObject.getJSONArray("data").get(i)).getString("desc"));
                        list.add(accountEntryBox);
                        i += 1;
                    }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return list;
    }

}

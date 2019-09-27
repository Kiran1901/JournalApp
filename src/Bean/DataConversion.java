package Bean;

import org.json.*;
import java.util.List;

public class DataConversion {

    private String personName;
    private float amount;
    private String description;
    private int type;//type=0 => Account;type=1 => Expense
    private int ac_type;

    public DataConversion(String pName, float amt, String desc, int type, int ac_type) {
        personName=pName;
        amount=amt;
        description=desc;
        this.type=type;
        this.ac_type =ac_type;
    }

    public DataConversion(JSONObject jsonObject) throws JSONException{
        try {
            personName=jsonObject.getString("name");
            amount= ((float) jsonObject.getDouble("amount"));
            description = jsonObject.getString("desc");
            type=jsonObject.getInt("type");
            if (type==0) ac_type=jsonObject.getInt("ac_type");
        }catch (JSONException e){
            System.out.println("Exception in conversion: json to object");
        }
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

    public int getAc_type() {
        return ac_type;
    }

    public void setAc_type(int ac_type) {
        this.ac_type = ac_type;
    }

    public JSONObject convertToJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",personName);
            jsonObject.put("amount",amount);
            jsonObject.put("desc",description);
            jsonObject.put("type",type);
            if(type==0) jsonObject.put("ac_type",ac_type);
            return jsonObject;
        }catch (JSONException e){
            System.out.println("Exception in conversion: object to json");
        }
        return jsonObject;
    }
}

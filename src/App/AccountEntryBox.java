package App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AccountEntryBox extends HBox {

    private int hboxType;
    private String ac_id;
    private TextField personName,amount,desc;

    public ChoiceBox<String> getType() {
        return type;
    }

    public void setType(ChoiceBox<String> type) {
        this.type = type;
    }

    private ChoiceBox<String> type;
    public String getAc_id() {
        return ac_id;
    }

    public void setAc_id(String ac_id) {
        this.ac_id = ac_id;
    }

    public TextField getPersonName() {
        return personName;
    }

    public void setPersonName(TextField personName) {
        this.personName = personName;
    }

    public TextField getAmount() {
        return amount;
    }

    public void setAmount(TextField amount) {
        this.amount = amount;
    }

    public TextField getDesc() {
        return desc;
    }

    public void setDesc(TextField desc) {
        this.desc = desc;
    }


    public AccountEntryBox(int hboxType)
    {
        this.setPadding(new Insets(0,10,0,10));

//        this.ac_id = new String(id);
        this.setSpacing(15);
        this.personName = new TextField();
        this.amount = new TextField();
        this.desc = new TextField();
        this.hboxType=hboxType;
        this.type = new ChoiceBox<>();

        if(this.hboxType==0) {
            type.getItems().addAll("Give", "Take");
            this.type.setValue(this.type.getItems().get(0));
            this.type.setPadding(new Insets(5,5,5,5));
            this.type.setPrefSize(50,30);
        }else{
//            type.setDisable(true);
            type.setVisible(false);
        }


        this.personName.setPadding(new Insets(5,5,5,5));
        this.personName.setAlignment(Pos.BASELINE_LEFT);
        this.personName.setPrefSize(150,30);
        this.personName.setPromptText("-Name-");
        this.personName.setEditable(true);

//        this.amount.setAlignment(Pos.BASELINE_CENTER);
        this.amount.setPadding(new Insets(5,5,5,5));
        this.amount.setPrefSize(100,30);
        this.amount.setPromptText("-Amount-");
        this.amount.setEditable(true);

//        this.desc.setAlignment(Pos.BASELINE_CENTER);
        this.desc.setPadding(new Insets(5,5,5,5));
        this.desc.setPrefSize(200,30);
        this.desc.setPromptText("-Description-");
        this.desc.setEditable(true);

        getChildren().addAll(this.personName,this.amount,this.desc,this.type);
    }
    public AccountEntryBox(int hboxType, String id,String personName,String amount, String desc,String type)
    {
        this.setPadding(new Insets(0,10,0,10));
        this.ac_id = new String(id);
        this.personName = new TextField(personName);
        this.amount = new TextField(amount);
        this.desc = new TextField(type);

        this.hboxType=hboxType;

        if(this.hboxType==0) {
            this.type = new ChoiceBox<>();
            this.type.getItems().addAll("Give", "Take");
        }else{
            this.type.setDisable(true);
        }
        this.type.setValue(this.type.getItems().get(0));

        this.personName.setPadding(new Insets(5,5,5,5));
        this.personName.setAlignment(Pos.BASELINE_LEFT);
        this.personName.setPrefSize(150,30);
        this.personName.setPromptText("-Name-");
        this.personName.setEditable(true);

//        this.amount.setAlignment(Pos.BASELINE_CENTER);
        this.amount.setPadding(new Insets(5,5,5,5));
        this.amount.setPrefSize(100,30);
        this.amount.setPromptText("-Amount-");
        this.amount.setEditable(true);

//        this.desc.setAlignment(Pos.BASELINE_CENTER);
        this.desc.setPadding(new Insets(5,5,5,5));
        this.desc.setPrefSize(200,30);
        this.desc.setPromptText("-Description-");
        this.desc.setEditable(true);

//        this.type.setAlignment(Pos.BASELINE_CENTER);
        this.type.setPadding(new Insets(5,5,5,5));
        this.type.setPrefSize(50,30);
//        this.type.setEditable(true);
        getChildren().addAll(this.personName,this.amount,this.desc,this.type);
    }

    public boolean checkIsAboxEmpty(){
        return (!personName.getText().isEmpty() && !amount.getText().isEmpty());
    }

}

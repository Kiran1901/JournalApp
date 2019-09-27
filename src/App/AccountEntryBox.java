package App;

import Bean.DataConversion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AccountEntryBox extends HBox {

    private int hboxType;
    private int id;
    private TextField personName,amount,desc;
    private ChoiceBox<String> type;

    public ChoiceBox<String> getType() {
        return type;
    }

    public void setType(ChoiceBox<String> type) {
        this.type = type;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
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


    public AccountEntryBox(int hboxType) {
        this.setPadding(new Insets(0,10,0,10));

        this.setSpacing(15);
        this.personName = new TextField();
        this.amount = new TextField();
        this.desc = new TextField();
        this.hboxType=hboxType;
        this.type = new ChoiceBox<>();

        if(this.hboxType==0) {
            type.getItems().addAll("Give", "Take");
            this.type.setValue(this.type.getItems().get(0));
            this.type.setPrefSize(50,30);
        }else{
            type.setVisible(false);
        }
        init();
    }

    public AccountEntryBox(DataConversion dataConversion){
        this.setPadding(new Insets(0,10,0,10));
        this.id = id;
        this.personName = new TextField(dataConversion.getPersonName());
        this.amount = new TextField((String.valueOf(dataConversion.getAmount())));
        this.desc = new TextField(dataConversion.getDescription());

        this.hboxType=dataConversion.getType();

        if(this.hboxType==0) {
            this.type = new ChoiceBox<>();
            this.type.getItems().addAll("Give", "Take");
        }else{
            this.type.setDisable(true);
        }
        this.type.setValue(this.type.getItems().get(0));
        this.type.setPrefSize(50,30);

        init();
    }


    public boolean checkIsAboxEmpty(){
        return (!personName.getText().isEmpty() && !amount.getText().isEmpty() && !amount.getText().matches("^-\\d?"));
    }

    public void disableAllFields(){
        personName.setDisable(true);
        amount.setDisable(true);
        desc.setDisable(true);
        type.setDisable(true);
    }

    public void init(){
        this.personName.setAlignment(Pos.BASELINE_LEFT);
        this.personName.setPrefSize(150,30);
        this.personName.setPromptText("-Name-");
        this.personName.setEditable(true);

        this.amount.setPrefSize(100,30);
        this.amount.setPromptText("-Amount-");
        this.amount.setEditable(true);

        this.desc.setPrefSize(200,30);
        this.desc.setPromptText("-Description-");
        this.desc.setEditable(true);

        this.setSpacing(10);

        getChildren().addAll(this.personName,this.amount,this.desc,this.type);
    }
}

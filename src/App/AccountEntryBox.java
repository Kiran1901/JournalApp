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
    private ChoiceBox<String> t_type;


    public AccountEntryBox(int hboxType) {
        this.setPadding(new Insets(0,10,0,10));

        this.setSpacing(15);
        this.personName = new TextField();
        this.amount = new TextField();
        this.desc = new TextField();
        this.hboxType=hboxType;
        this.t_type = new ChoiceBox<>();

        if(this.hboxType==0) {
            t_type.getItems().addAll("Give", "Take");
            this.t_type.setValue(this.t_type.getItems().get(0));
            this.t_type.setPrefSize(50,30);
        }else{
            t_type.setVisible(false);
        }
        initHBoxLayout();
    }

    public AccountEntryBox(DataConversion dataConversion){
        this.setPadding(new Insets(0,10,0,10));
        this.id = id;
        this.personName = new TextField(dataConversion.getPersonName());
        this.amount = new TextField((String.valueOf(dataConversion.getAmount())));
        this.desc = new TextField(dataConversion.getDescription());
        this.setId(this.personName.getText());
        this.hboxType=dataConversion.getType();
        this.t_type = new ChoiceBox<>();

        if(this.hboxType==0) {
            this.t_type.getItems().addAll("Give", "Take");
            this.t_type.setValue(dataConversion.getT_type()==0?"Give":"Take");
        }else{
            this.t_type.setVisible(false);
        }
        this.t_type.setPrefSize(50,30);

        initHBoxLayout();
    }


    public boolean checkIsAboxEmpty(){
        return (!personName.getText().isEmpty() && !amount.getText().isEmpty() && amount.getText().matches("^[1-9]\\d*$"));
    }

    public void disableAllFields(){
        personName.setDisable(true);
        amount.setDisable(true);
        desc.setDisable(true);
        t_type.setDisable(true);
    }

    public void initHBoxLayout(){
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

        getChildren().addAll(this.personName,this.amount,this.desc,this.t_type);
    }


    //getters and setters

    public ChoiceBox<String> getT_type() {
        return t_type;
    }

    public void setT_type(String type) {
        if (hboxType==0){
            this.t_type.setValue(type);
        }
    }

    public int getType(){
        return this.hboxType;
    }
    public void setType(int type){
        this.hboxType=type;
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

    public void setPersonName(String personName) {
        this.personName.setText(personName);
    }

    public TextField getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.setText(amount);
    }

    public TextField getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.setText(desc);
    }

}

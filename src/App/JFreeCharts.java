package App;

import Bean.AccountEntryBean;
import Connectivity.AccountEntryDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JFreeCharts extends BarChart<String,Number>{

    Series<String, Number> dataSeries1;
    Series<String, Number> dataSeries2;
    public JFreeCharts(CategoryAxis xAxis, NumberAxis yAxis) {
        super(xAxis, yAxis);
        xAxis.setLabel("Date");
        xAxis.setTickLabelsVisible(true);
        yAxis.setLabel("Amount");
    }

    public  List<Series> createData(String startDate, String endDate) {
        dataSeries1 = new Series<>();
        dataSeries1.setName("Give");
        dataSeries2 = new Series<>();
        dataSeries2.setName("Take");


        AccountEntryDao accountEntryDao = new AccountEntryDao();
        List<AccountEntryBean> list = new ArrayList<>();
        list = accountEntryDao.selectEntryByDate(startDate,endDate,"account_log");
        JSONArray jsonArray = null;
        JSONObject jsonObject2,innerJSon;
        int dateWiseGiveAmount =0;
        int dateWiseTakeAmount =0;
        String prev=list.get(0).getDate();
        for(AccountEntryBean x : list) {

            try{
                jsonObject2 = new JSONObject(x.getJson());
                jsonArray = jsonObject2.getJSONArray("data");
                if(!x.getDate().equals(prev)){
                    dateWiseGiveAmount=0;
                    dateWiseTakeAmount=0;
                }
                for(int i=0;i<jsonArray.length();i++) {
                    innerJSon = (JSONObject) jsonArray.get(i);
                    if(innerJSon.getInt("t_type")==0)//1 take
                        dateWiseGiveAmount += innerJSon.getInt("amount");
                    else
                        dateWiseTakeAmount += innerJSon.getInt("amount");
                }
                Data data1 = new Data(x.getDate(),dateWiseGiveAmount);
                data1.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        if (node != null) {
                            displayLabelForData(data1);
                        }
                    }
                });
                dataSeries1.getData().add(data1);
                Data data2=new Data(x.getDate(), dateWiseTakeAmount);
                data2.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        if (node != null) {
                            displayLabelForData(data2);
                        }
                    }
                });
                dataSeries2.getData().add(data2);
            }catch(JSONException e) {
                e.printStackTrace();
            }
            prev=x.getDate();
        }
        List<Series> listd = new ArrayList<>();
        listd.add(dataSeries1);
        listd.add(dataSeries2);

        return listd;
    }

    public Series createExpenseSeries(String startDate, String endDate){
        Series series = new Series();
        series.setName("Expense");
        AccountEntryDao accountEntryDao = new AccountEntryDao();
        List<AccountEntryBean> list = new ArrayList<>();
        list = accountEntryDao.selectEntryByDate(startDate,endDate,"expenses");
        JSONArray jsonArray = null;
        JSONObject jsonObject2,innerJSon;
        int dateWiseAmount =0;
        String prev=list.get(0).getDate();
        for(AccountEntryBean x : list) {

            try{
                jsonObject2 = new JSONObject(x.getJson());
                jsonArray = jsonObject2.getJSONArray("data");
                if(!x.getDate().equals(prev)){
                    dateWiseAmount=0;
                }
                for(int i=0;i<jsonArray.length();i++) {
                    innerJSon = (JSONObject) jsonArray.get(i);
                    dateWiseAmount += innerJSon.getInt("amount");
                }
                Data data = new Data(x.getDate(),dateWiseAmount);
                data.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        if (node != null) {
                            displayLabelForData(data);
                        }
                    }
                });
                series.getData().add(data);
            }catch(JSONException e) {
                e.printStackTrace();
            }
            prev=x.getDate();
        }
        return series;
    }

    public void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
    }
}

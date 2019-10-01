package App;

import Bean.AccountEntryBean;
import Bean.DataConversion;
import Connectivity.AccountEntryDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
//import org.jfree.data.xy.XYSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JFreeCharts extends BarChart<String,Number>{

    Map<Node, TextFlow> nodeMap;
    Series<String, Number> dataSeries1;
    Series<String, Number> dataSeries2;
    public JFreeCharts(CategoryAxis xAxis, NumberAxis yAxis) {
        super(xAxis, yAxis);
//        xAxis    = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setTickLabelsVisible(true);
         nodeMap= new HashMap<>();
//        yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
    }

//    public BarChart createBarChart()
//    {
////        CategoryAxis xAxis    = new CategoryAxis();
////        xAxis.setLabel("Date");
////
////        NumberAxis yAxis = new NumberAxis();
////        yAxis.setLabel("Amount");
//
////        return new BarChart(xAxis, yAxis);
//    }
    public  List<Series> createData()
    {
        dataSeries1 = new Series<>();
        dataSeries1.setName("Give");
        dataSeries2 = new Series<>();
        dataSeries2.setName("Take");


        AccountEntryDao accountEntryDao = new AccountEntryDao();
        List<AccountEntryBean> list = new ArrayList<>();
        list = accountEntryDao.selectEntryByName();
        DataConversion dataConversion = null;
        JSONArray jsonArray = null;
        JSONObject jsonObject2 = null;
        JSONObject innerJSon = null;
        int dateWiseGiveAmount =0;
        int dateWiseTakeAmount =0;
        for(AccountEntryBean x : list)
        {
            try{
                jsonObject2 = new JSONObject(x.getJson());
                jsonArray = jsonObject2.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++)
                {
                    innerJSon = (JSONObject) jsonArray.get(i);
                    if(innerJSon.getInt("t_type")==0)//1 take
                        dateWiseGiveAmount +=innerJSon.getInt("amount");
                    else
                        dateWiseTakeAmount +=innerJSon.getInt("amount");
                }
//                Data data1 = new Data(x.getTime(),dateWiseGiveAmount);
//                displayLabelForData(data1);
                dataSeries1.getData().add(new Data(x.getTime(), dateWiseGiveAmount));
                dataSeries2.getData().add(new Data(x.getTime(), dateWiseTakeAmount));
                dateWiseGiveAmount=0;
                dateWiseTakeAmount=0;
            }catch(JSONException e)
            {
                e.printStackTrace();
            }

        }
        List<Series> listd = new ArrayList<>();
        listd.add(dataSeries1);
        listd.add(dataSeries2);
        return listd;
    }

    public void setTextFlow(TextFlow textFlow) {
        this.getPlotChildren().add(textFlow);
    }
//    public void layoutPlotChildren() {
//        super.layoutPlotChildren();
//        System.out.println("nodemap: "+nodeMap);
//        for (Node bar : nodeMap.keySet()) {
////          for()
//            TextFlow textFlow = nodeMap.get(bar);
//            System.out.println("textflow "+(Text)textFlow.getChildren().get(0));
//            System.out.println("Bar  "+bar);
//            if (bar.getBoundsInParent().getHeight() > 30) {
//                ((Text) textFlow.getChildren().get(0)).setFill(Color.WHITE);
//                textFlow.resize(bar.getBoundsInParent().getWidth(), 200);
//                textFlow.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() + 10);
//            } else {
//                ((Text) textFlow.getChildren().get(0)).setFill(Color.GRAY);
//                textFlow.resize(bar.getBoundsInParent().getWidth(), 200);
//                textFlow.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() - 20);
//            }
//        }
//    }
//    @Override
//    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
////        super.seriesAdded(series, seriesIndex);
//        for (int j = 0; j < series.getData().size(); j++) {
//
//            System.out.println("series:"+series.getData().get(j));
//            Data<String, Number> item = series.getData().get(j);
//            Text text = new Text(item.getYValue().toString());
//            System.out.println("TExt : "+text.getText());
//            text.setStyle("-fx-font-size: 10pt;");
//            TextFlow textFlow = new TextFlow(text);
//            textFlow.setTextAlignment(TextAlignment.CENTER);
//            nodeMap.put(series.getData().get(j).getNode(), textFlow);
//            this.getPlotChildren().add(textFlow);
//        }
//
//    }

    public void displayLabelForData(Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        System.out.println("Final Text "+dataText.getText());
//        StackPane node2 = new StackPane();
//        Label label = new Label(dataText.getText());
//        label.setRotate(-90);
//        Group group = new Group(label);
//        StackPane.setAlignment(group, Pos.BOTTOM_CENTER);
//        StackPane.setMargin(group, new Insets(0, 0, 5, 0));
//        node2.getChildren().add(group);
//        data.setNode(node);

        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
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
                dataText.setLayoutX(50);
                dataText.setLayoutX(50);
          }
        });
    }
}

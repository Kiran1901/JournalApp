package App;

import Bean.AccountEntryBean;
import Connectivity.AccountEntryDao;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class JFreeCharts{

    public static BarChart createBarChart()
    {
        CategoryAxis xAxis    = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        return new BarChart(xAxis, yAxis);
    }
    public static XYChart.Series createData()
    {
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Account");
        AccountEntryDao accountEntryDao = new AccountEntryDao();
        List<AccountEntryBean> list = new ArrayList<>();
        list = accountEntryDao.selectEntryByName();

        dataSeries1.getData().add(new XYChart.Data("Desktop", 178));
        dataSeries1.getData().add(new XYChart.Data("Desktop2", 128));
        dataSeries1.getData().add(new XYChart.Data("Desktop3", 173));
        dataSeries1.getData().add(new XYChart.Data("Phone"  , 65));
        dataSeries1.getData().add(new XYChart.Data("Tablet"  , 23));
        return dataSeries1;
    }
}

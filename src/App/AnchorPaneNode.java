package App;

import Bean.TimelineBean;
import Connectivity.TimelineDao;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    Controller controller;

    public AnchorPaneNode(Node... children) {
        super(children);

        this.setOnMouseClicked(e -> {
            Controller.date = date;
                TimelineDao dao = new TimelineDao();
                List<TimelineBean> list = dao.selectEntryByNameDate(date);
                Controller.datewiseEntry.clear();
                for(TimelineBean x:list) {
                    Controller.datewiseEntry.add(new FeedBox(Integer.toString(x.getId()), x.getDate(), x.getTime(), x.getText()));
                }
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
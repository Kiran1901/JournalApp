package App;

import Bean.TimelineBean;
import Connectivity.ConnectionClass;
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

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    Controller controller;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            Controller.date = date;
//                ConnectionClass connectionClass = new ConnectionClass();
//                Connection conn = connectionClass.getConnection();
//                Statement statement = conn.createStatement();
//                ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE user='Kiran' and date='"+ date + "' ORDER BY ID DESC;" );
                TimelineDao dao = new TimelineDao();
                List<TimelineBean> list = dao.selectEntryByNameDate(date);
                Controller.datewiseEntry.clear();
                for(TimelineBean x:list) {
//                int i=x.getId();
//                Integer.toString(i);
                    Controller.datewiseEntry.add(new FeedBox(Integer.toString(x.getId()), x.getDate(), x.getTime(), x.getText()));
                }
//                Controller.datewiseEntry.clear();
//                while (list.next()){
//                    Controller.datewiseEntry.add(new FeedBox(list.getString("ID"),list.getString("date"),list.getString("time"),list.getString("text")));
//                }
//                statement.close();
//                conn.close();
//            }catch (SQLException ex){
//                ex.printStackTrace();
//                System.out.println("SQLException");
//            }
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
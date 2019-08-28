package App;

import Connectivity.ConnectionClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class FeedBoxController implements Initializable {

    @FXML
    FeedBox feedBox;

    public void initialize(URL location, ResourceBundle resources){

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.getConnection();
            Statement statement = conn.createStatement();
            ResultSet list = statement.executeQuery("SELECT * FROM timeline WHERE user='Kiran';" );

            list.next();
            //while (list.next()) {
//                System.out.println(list.getString("date"));
//                feedBox.setDateField(list.getString("date"));
//                feedBox.setTimeField(list.getString("time"));
//                feedBox.setTextField(list.getString("text"));
            //}
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException");
        }

        System.out.println("BoxController executed");
    }

}

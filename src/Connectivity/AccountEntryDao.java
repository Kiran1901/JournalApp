package Connectivity;

import Bean.AccountEntryBean;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.sql.*;

public class AccountEntryDao {
    private Connection con=null;
    private String USER= "Kiran";


    public void insertEntry(AccountEntryBean accountEntryBean,String table)
    {
        try{
            con = ConnectionClass.getConnection();
            String sql = "INSERT INTO "+ table +" (user, date, time, data) VALUES ('"+accountEntryBean.getUser()+"','"+accountEntryBean.getDate()+"','"+accountEntryBean.getTime()+"',(CONVERT('"+accountEntryBean.getJson()+"' USING UTF8MB4)))";
            Statement preparedStatement = con.createStatement();
//            preparedStatement.setString(1,accountEntryBean.getUser());
//            preparedStatement.setDate(2, Date.valueOf(accountEntryBean.getDate()));
//            preparedStatement.setTime(3, Time.valueOf(accountEntryBean.getTime()));
//            preparedStatement.setObject(4,accountEntryBean.getJson());
            preparedStatement.execute(sql);
            preparedStatement.close();
            con.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public AccountEntryBean getAccountEntry(AccountEntryBean bean){
        String USER= "Kiran";
        try{
            con = ConnectionClass.getConnection();
            String sql = "SELECT * FROM account_log WHERE date='"+ bean.getDate() + "' and time='"+bean.getTime()+"';";
            Statement statement = con.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if (res.next()){
                bean.setId(res.getInt("id"));
                bean.setUser(USER);
                bean.setJson(res.getString("data"));
            }
            statement.close();
            con.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return bean;
    }

    public AccountEntryBean getExpenseEntry(AccountEntryBean bean){
        try{
            con = ConnectionClass.getConnection();
            String sql = "SELECT * FROM expenses WHERE date='"+ bean.getDate() + "' and time='"+bean.getTime()+"';";
            Statement statement = con.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if (res.next()){
                bean.setId(res.getInt("ID"));
                bean.setUser(USER);
                bean.setJson(res.getString("data"));
            }
            statement.close();
            con.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return bean;
    }

    public void updateEntry(AccountEntryBean accountEntryBean, String table){
        try{
            con = ConnectionClass.getConnection();
            String sql = "UPDATE "+ table +" SET data=(CONVERT('"+new JSONObject(accountEntryBean.getJson())+"' USING UTF8MB4))"+ " WHERE date='"+ accountEntryBean.getDate()+"' AND time='" + accountEntryBean.getTime() + "';";
            Statement preparedStatement = con.createStatement();
            preparedStatement.executeUpdate(sql);
            preparedStatement.close();
            con.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}

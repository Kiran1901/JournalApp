package Connectivity;

import Bean.AccountEntryBean;

import java.sql.*;

public class AccountEntryDao {
    private Connection con=null;

    public void insertEntry(AccountEntryBean accountEntryBean)
    {
        System.out.println("We are in dao");
        try{
            con = ConnectionClass.getConnection();
            String sql = "INSERT INTO account_log (user, date, time, data) VALUES ('"+accountEntryBean.getUser()+"','"+accountEntryBean.getDate()+"','"+accountEntryBean.getTime()+"',(CONVERT('"+accountEntryBean.getJson()+"' USING UTF8MB4)))";
            Statement preparedStatement = con.createStatement();
//            preparedStatement.setString(1,accountEntryBean.getUser());
//            preparedStatement.setDate(2, Date.valueOf(accountEntryBean.getDate()));
//            preparedStatement.setTime(3, Time.valueOf(accountEntryBean.getTime()));
//            preparedStatement.setObject(4,accountEntryBean.getJson());
            boolean b=preparedStatement.execute(sql);
            if(b){
                System.out.println("Inserted");
            }else {
                System.out.println("Not inserted");
            }
            preparedStatement.close();
            con.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

}

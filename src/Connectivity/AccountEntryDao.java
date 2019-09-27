package Connectivity;

import Bean.AccountEntryBean;

import java.sql.*;

public class AccountEntryDao {
    private Connection con=null;

    public void insertEntry(AccountEntryBean accountEntryBean)
    {
        try{
            con = ConnectionClass.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO account_log (user, date, time, data) VALUES (?,?,?,?)");
            preparedStatement.setString(1,accountEntryBean.getUser());
            preparedStatement.setDate(2, Date.valueOf(accountEntryBean.getDate()));
            preparedStatement.setTime(3, Time.valueOf(accountEntryBean.getTime()));
            preparedStatement.setObject(4,accountEntryBean.getJson());
            boolean b=preparedStatement.execute();
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

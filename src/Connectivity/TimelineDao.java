package Connectivity;

import Bean.TimelineBean;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimelineDao {
    private Connection con=null;

    public List<TimelineBean> selectEntryByName(){
        List<TimelineBean> list = new ArrayList<>();
        try {
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM timeline WHERE user=? ORDER BY ID DESC");
            statement.setString(1, "Kiran");
            ResultSet rs = statement.executeQuery();
//            List<TimelineBean> list = null;

            while (rs.next()) {
                TimelineBean bean = new TimelineBean();
                bean.setId(rs.getInt("ID"));
                bean.setDate(rs.getDate("date"));
                bean.setTime(rs.getTime("time"));
                bean.setUser(rs.getString("user"));
                bean.setText(rs.getString("text"));
                list.add(bean);
            }
            statement.close();
            con.close();
        }catch (SQLException e)
        {
            System.out.println("Sql Exception");
        }
        return list;
    }

    public List<TimelineBean> selectEntryByNameDate(LocalDate date){
        List<TimelineBean> list = new ArrayList<>();
        try {
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM timeline WHERE user=? AND date=? ORDER BY ID DESC");
            statement.setString(1, "Kiran");
            statement.setDate(2, Date.valueOf(date));
            ResultSet rs = statement.executeQuery();
//            List<TimelineBean> list = null;

            while (rs.next()) {
                TimelineBean bean = new TimelineBean();
                bean.setId(rs.getInt("ID"));
                bean.setDate(rs.getDate("date"));
                bean.setTime(rs.getTime("time"));
                bean.setUser(rs.getString("user"));
                bean.setText(rs.getString("text"));
                list.add(bean);
            }
            statement.close();
            con.close();
        }catch (SQLException e)
        {
            System.out.println("Sql Exception");
        }
        return list;
    }

    public TimelineBean selectEntryByNameId(int id){
//        List<TimelineBean> list = new ArrayList<>();
        TimelineBean bean = new TimelineBean();
        try {
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM timeline WHERE user=? AND id=? ORDER BY ID DESC");
            statement.setString(1, "Kiran");
            statement.setInt(2,id);
            ResultSet rs = statement.executeQuery();
//            List<TimelineBean> list = null;

            while (rs.next()) {

                bean.setId(rs.getInt("ID"));
                bean.setDate(rs.getDate("date"));
                bean.setTime(rs.getTime("time"));
                bean.setUser(rs.getString("user"));
                bean.setText(rs.getString("text"));
//                list.add(bean);
            }
            statement.close();
            con.close();
        }catch (SQLException e)
        {
            System.out.println("Sql Exception");
        }
        return bean;
    }

    public void updateEntryByIdUser(TimelineBean timelineBean)
    {
        try {
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE timeline SET text=? WHERE ID=? AND user=?");
            statement.setString(1,timelineBean.getText());
            statement.setString(3, timelineBean.getUser());
            statement.setInt(2, timelineBean.getId());
            boolean b=statement.execute();
            if(b)
            {
                System.out.println("Updated");
            }else {
                System.out.println("NOt updated");
            }
            statement.close();
            con.close();
        }catch (SQLException e)
        {
            System.out.println("Sql Exception updateEntryByIdUser()");
        }
    }
    public void insertEntry(TimelineBean timelineBean,String tableName)
    {
        try {
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO timeline (user,date,time,text) VALUES (?,?,?,?);");
//            statement.setString(1,tableName);
            statement.setString(4,timelineBean.getText());
            statement.setString(1, timelineBean.getUser());
            statement.setDate(2, Date.valueOf(timelineBean.getDate()));
            statement.setTime(3,Time.valueOf(timelineBean.getTime()));
            boolean b=statement.execute();
            if(b){
                System.out.println("Inserted");
            }else {
                System.out.println("Not inserted");
            }
            statement.close();
            con.close();
        }catch (SQLException e){
//            System.out.println("Sql Exception insertEntry()");
            e.printStackTrace();
        }
    }
    public int getIdFromAll(TimelineBean timelineBean, String tableName)
    {
        int id = 0;
        try{
            con = ConnectionClass.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT ID FROM timeline WHERE DATE=? AND TIME=? AND USER=?");
            statement.setString(3, timelineBean.getUser());
//            statement.setString(1,tableName);
            statement.setDate(1, Date.valueOf(timelineBean.getDate()));
            statement.setTime(2,Time.valueOf(timelineBean.getTime()));
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            rs.next();
            id = rs.getInt("ID");
            statement.close();
            con.close();
        }catch(SQLException e) {
            System.out.println("Sql Exception getIdfromAll()");
        }
        return id;
    }
}
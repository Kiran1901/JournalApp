package Bean;

import java.sql.Date;
import java.sql.Time;

public class TimelineBean {

    private int id;
    private String user;
    private String date;
    private String time;
    private String text;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date.toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

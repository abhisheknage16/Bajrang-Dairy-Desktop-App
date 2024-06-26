package DB;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By STN21
 */

public class Message
{
    private int d;
    private long id, mobile;
    private String textmsg;
    private LocalDate date;
    private LocalTime time;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getMobile()
    {
        return mobile;
    }

    public void setMobile(long mobile)
    {
        this.mobile = mobile;
    }

    public int getD()
    {
        return d;
    }

    public void setD(int d)
    {
        this.d = d;
    }

    public String getTextmsg()
    {
        return textmsg;
    }

    public void setTextmsg(String textmsg)
    {
        this.textmsg = textmsg;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }
}

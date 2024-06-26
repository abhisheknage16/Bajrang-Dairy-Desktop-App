package VTable;

import java.time.LocalDate;

/**
 * Created By STN21
 */

public class Message
{
    private long srno;
    private long id;
    private String textmsg, mobile;
    private LocalDate date;

    public long getSrno()
    {
        return srno;
    }

    public void setSrno(long srno)
    {
        this.srno = srno;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
}

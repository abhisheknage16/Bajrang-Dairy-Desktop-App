package DB;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By Anil on 21/10/2018
 */
public class MilkSale
{
    private int id, me;
    private String type, cname;
    private double rate, total, ttlmilk;
    private LocalDate date;
    private LocalTime time;
    private int d;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMe() {
        return me;
    }

    public void setMe(int me) {
        this.me = me;
    }

    public double getTtlmilk()
    {
        return ttlmilk;
    }

    public void setTtlmilk(double ttlmilk)
    {
        this.ttlmilk = ttlmilk;
    }

    public int getD()
    {
        return d;
    }

    public void setD(int d)
    {
        this.d = d;
    }
}

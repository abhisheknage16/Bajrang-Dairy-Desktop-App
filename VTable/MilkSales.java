package VTable;

import java.time.LocalDate;

public class MilkSales {

    private long srno, id;
    private String cname, time, type;
    private LocalDate date;
    private Double ttl, rate, ttlamt;


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getTtl() {
        return ttl;
    }

    public void setTtl(Double ttl) {
        this.ttl = ttl;
    }

    public Double getTtlamt() {
        return ttlamt;
    }

    public void setTtlamt(Double ttlamt) {
        this.ttlamt = ttlamt;
    }

    public long getSrno() {
        return srno;
    }

    public void setSrno(long srno) {
        this.srno = srno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

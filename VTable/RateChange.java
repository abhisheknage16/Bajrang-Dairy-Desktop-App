package VTable;

import java.time.LocalDate;

public class RateChange {
    private long id;
    private long srno;
    private LocalDate date;
    private String fat, rate, snf;


    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public long getSrno() {
        return srno;
    }

    public void setSrno(long srno) {
        this.srno = srno;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSnf() {
        return snf;
    }

    public void setSnf(String snf) {
        this.snf = snf;
    }
}

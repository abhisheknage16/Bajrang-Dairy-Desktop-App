package VTable;

import java.time.LocalDate;

/**
 * Created By STN21
 */

public class Bill
{
    private long id;
    private int srno;
    private String cno, cname, hafta, sign;
    private double ttlmilk, ttlgfat, ttlgsnf, ttlamount, paid;
    private double bttlltr, bttlgfat, bttlgsnf, bttlamt;
    private double cttlltr, cttlgfat, cttlgsnf, cttlamt;

    public int getSrno()
    {
        return srno;
    }

    public void setSrno(int srno)
    {
        this.srno = srno;
    }

    public String getCno()
    {
        return cno;
    }

    public void setCno(String cno)
    {
        this.cno = cno;
    }

    public String getCname()
    {
        return cname;
    }

    public void setCname(String cname)
    {
        this.cname = cname;
    }

    public double getTtlmilk()
    {
        return ttlmilk;
    }

    public void setTtlmilk(double ttlmilk)
    {
        this.ttlmilk = ttlmilk;
    }

    public double getTtlgfat()
    {
        return ttlgfat;
    }

    public void setTtlgfat(double ttlgfat)
    {
        this.ttlgfat = ttlgfat;
    }

    public double getTtlamount()
    {
        return ttlamount;
    }

    public void setTtlamount(double ttlamount)
    {
        this.ttlamount = ttlamount;
    }

    public double getPaid()
    {
        return paid;
    }

    public void setPaid(double paid)
    {
        this.paid = paid;
    }

    public String getHafta()
    {
        return hafta;
    }

    public void setHafta(String hafta)
    {
        this.hafta = hafta;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public double getBttlltr()
    {
        return bttlltr;
    }

    public void setBttlltr(double bttlltr)
    {
        this.bttlltr = bttlltr;
    }

    public double getCttlltr()
    {
        return cttlltr;
    }

    public void setCttlltr(double cttlltr)
    {
        this.cttlltr = cttlltr;
    }

    public double getCttlgfat()
    {
        return cttlgfat;
    }

    public void setCttlgfat(double cttlgfat)
    {
        this.cttlgfat = cttlgfat;
    }

    public double getBttlgfat()
    {
        return bttlgfat;
    }

    public void setBttlgfat(double bttlgfat)
    {
        this.bttlgfat = bttlgfat;
    }

    public double getBttlamt()
    {
        return bttlamt;
    }

    public void setBttlamt(double bttlamt)
    {
        this.bttlamt = bttlamt;
    }

    public double getCttlamt()
    {
        return cttlamt;
    }

    public void setCttlamt(double cttlamt)
    {
        this.cttlamt = cttlamt;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public double getTtlgsnf() {
        return ttlgsnf;
    }

    public void setTtlgsnf(double ttlgsnf) {
        this.ttlgsnf = ttlgsnf;
    }

    public double getBttlgsnf() {
        return bttlgsnf;
    }

    public void setBttlgsnf(double bttlgsnf) {
        this.bttlgsnf = bttlgsnf;
    }

    public double getCttlgsnf() {
        return cttlgsnf;
    }

    public void setCttlgsnf(double cttlgsnf) {
        this.cttlgsnf = cttlgsnf;
    }
}

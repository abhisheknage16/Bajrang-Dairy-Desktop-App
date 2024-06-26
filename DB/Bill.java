package DB;

import java.time.LocalDate;

public class Bill
{
    private long id;
    private String bno;
    private LocalDate date, sdate, edate;
    private double bttlltr, bttlgfat, bttlamt;
    private double cttlltr, cttlgfat, cttlamt;
    private double ttlltr, total, hafta, paid;
    private String cno;
    private Customer customer;
    private int d;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public LocalDate getSdate()
    {
        return sdate;
    }

    public void setSdate(LocalDate sdate)
    {
        this.sdate = sdate;
    }

    public LocalDate getEdate()
    {
        return edate;
    }

    public void setEdate(LocalDate edate)
    {
        this.edate = edate;
    }

    public String getBno()
    {
        return bno;
    }

    public void setBno(String bno)
    {
        this.bno = bno;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public double getBttlltr()
    {
        return bttlltr;
    }

    public void setBttlltr(double bttlltr)
    {
        this.bttlltr = bttlltr;
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

    public double getCttlgfat()
    {
        return cttlgfat;
    }

    public void setCttlgfat(double cttlgfat)
    {
        this.cttlgfat = cttlgfat;
    }

    public double getCttlltr()
    {
        return cttlltr;
    }

    public void setCttlltr(double cttlltr)
    {
        this.cttlltr = cttlltr;
    }

    public double getTtlltr()
    {
        return ttlltr;
    }

    public void setTtlltr(double ttlltr)
    {
        this.ttlltr = ttlltr;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getHafta()
    {
        return hafta;
    }

    public void setHafta(double hafta)
    {
        this.hafta = hafta;
    }

    public double getPaid()
    {
        return paid;
    }

    public void setPaid(double paid)
    {
        this.paid = paid;
    }

    public String getCno()
    {
        return cno;
    }

    public void setCno(String cno)
    {
        this.cno = cno;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
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

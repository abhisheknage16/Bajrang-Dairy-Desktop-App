package DB;

import java.time.LocalDate;

public class BillProd
{
    private long id;
    private String bno;
    private int srno;
    private LocalDate date;
    private double bltr, bfat, bgfat, bttl;
    private double cltr, cfat, cgfat, cttl;
    private Bill bill;
    private int d;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getBno()
    {
        return bno;
    }

    public void setBno(String bno)
    {
        this.bno = bno;
    }

    public int getSrno()
    {
        return srno;
    }

    public void setSrno(int srno)
    {
        this.srno = srno;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public double getBltr()
    {
        return bltr;
    }

    public void setBltr(double bltr)
    {
        this.bltr = bltr;
    }

    public double getBfat()
    {
        return bfat;
    }

    public void setBfat(double bfat)
    {
        this.bfat = bfat;
    }

    public double getBgfat()
    {
        return bgfat;
    }

    public void setBgfat(double bgfat)
    {
        this.bgfat = bgfat;
    }

    public double getBttl()
    {
        return bttl;
    }

    public void setBttl(double bttl)
    {
        this.bttl = bttl;
    }

    public double getCttl()
    {
        return cttl;
    }

    public void setCttl(double cttl)
    {
        this.cttl = cttl;
    }

    public double getCgfat()
    {
        return cgfat;
    }

    public void setCgfat(double cgfat)
    {
        this.cgfat = cgfat;
    }

    public double getCfat()
    {
        return cfat;
    }

    public void setCfat(double cfat)
    {
        this.cfat = cfat;
    }

    public double getCltr()
    {
        return cltr;
    }

    public void setCltr(double cltr)
    {
        this.cltr = cltr;
    }

    public Bill getBill()
    {
        return bill;
    }

    public void setBill(Bill bill)
    {
        this.bill = bill;
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

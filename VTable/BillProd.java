package VTable;

import java.time.LocalDate;

public class BillProd
{
    private int srno;
    private long id;
    private LocalDate date;
    private String strdate;
    private String bno;
    private double bltr, bfat, bgfat, bttl;
    private double cltr, cfat,csnf, cgfat, cttl;

    public int getSrno()
    {
        return srno;
    }

    public void setSrno(int srno)
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

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getBno()
    {
        return bno;
    }

    public void setBno(String bno)
    {
        this.bno = bno;
    }

    public double getBltr()
    {
        return bltr;
    }

    public void setBltr(double bltr)
    {
        this.bltr = bltr;
    }

    public double getCltr()
    {
        return cltr;
    }

    public void setCltr(double cltr)
    {
        this.cltr = cltr;
    }

    public double getCfat()
    {
        return cfat;
    }

    public void setCfat(double cfat)
    {
        this.cfat = cfat;
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

    public double getCgfat()
    {
        return cgfat;
    }

    public void setCgfat(double cgfat)
    {
        this.cgfat = cgfat;
    }

    public double getCttl()
    {
        return cttl;
    }

    public void setCttl(double cttl)
    {
        this.cttl = cttl;
    }

    public double getBttl()
    {
        return bttl;
    }

    public void setBttl(double bttl)
    {
        this.bttl = bttl;
    }

    public String getStrdate()
    {
        return strdate;
    }

    public void setStrdate(String strdate)
    {
        this.strdate = strdate;
    }

    public double getCsnf() {
        return csnf;
    }

    public void setCsnf(double csnf) {
        this.csnf = csnf;
    }
}

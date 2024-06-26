package VTable;

public class MasterBillReport
{
    private long cid, srno;
    private int snumber;
    private String cname;
    private double totalmilk, gfat, ttlamt, uchal, hafta, net;

    public long getCid()
    {
        return cid;
    }

    public void setCid(long cid)
    {
        this.cid = cid;
    }

    public long getSrno()
    {
        return srno;
    }

    public void setSrno(long srno)
    {
        this.srno = srno;
    }

    public int getSnumber()
    {
        return snumber;
    }

    public void setSnumber(int snumber)
    {
        this.snumber = snumber;
    }

    public String getCname()
    {
        return cname;
    }

    public void setCname(String cname)
    {
        this.cname = cname;
    }

    public double getTotalmilk()
    {
        return totalmilk;
    }

    public void setTotalmilk(double totalmilk)
    {
        this.totalmilk = totalmilk;
    }

    public double getGfat()
    {
        return gfat;
    }

    public void setGfat(double gfat)
    {
        this.gfat = gfat;
    }

    public double getTtlamt()
    {
        return ttlamt;
    }

    public void setTtlamt(double ttlamt)
    {
        this.ttlamt = ttlamt;
    }

    public double getUchal()
    {
        return uchal;
    }

    public void setUchal(double uchal)
    {
        this.uchal = uchal;
    }

    public double getHafta()
    {
        return hafta;
    }

    public void setHafta(double hafta)
    {
        this.hafta = hafta;
    }

    public double getNet()
    {
        return net;
    }

    public void setNet(double net)
    {
        this.net = net;
    }
}

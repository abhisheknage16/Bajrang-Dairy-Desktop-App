package DB;

public class BillProduct
{
    private long id;
    private int d;
    private Bill bill;
    private BuffaloMilk buffaloMilk;
    private CowMilk cowMilk;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getD()
    {
        return d;
    }

    public void setD(int d)
    {
        this.d = d;
    }

    public Bill getBill()
    {
        return bill;
    }

    public void setBill(Bill bill)
    {
        this.bill = bill;
    }

    public BuffaloMilk getBuffaloMilk()
    {
        return buffaloMilk;
    }

    public void setBuffaloMilk(BuffaloMilk buffaloMilk)
    {
        this.buffaloMilk = buffaloMilk;
    }

    public CowMilk getCowMilk()
    {
        return cowMilk;
    }

    public void setCowMilk(CowMilk cowMilk)
    {
        this.cowMilk = cowMilk;
    }
}

package VTable;

import java.time.LocalDate;

public class CreditDebit
{
    private int srno;
    private long id, cid, billno;
    private LocalDate date;
    private String cno, cname, remark, activity;
    private double amount, pending, deposite;

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

    public long getCid()
    {
        return cid;
    }

    public void setCid(long cid)
    {
        this.cid = cid;
    }

    public long getBillno()
    {
        return billno;
    }

    public void setBillno(long billno)
    {
        this.billno = billno;
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

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getActivity()
    {
        return activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public double getDeposite()
    {
        return deposite;
    }

    public void setDeposite(double deposite)
    {
        this.deposite = deposite;
    }

    public double getPending()
    {
        return pending;
    }

    public void setPending(double pending)
    {
        this.pending = pending;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}

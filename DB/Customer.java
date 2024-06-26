package DB;

import java.time.LocalDate;

public class Customer {
    private long id;
    private int d, status;
    private String name, addr, accno, ifsc, sno, mob;
    private double pending, deposite, hafta;
    private LocalDate date;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddr()
    {
        return addr;
    }

    public void setAddr(String addr)
    {
        this.addr = addr;
    }

    public String getAccno()
    {
        return accno;
    }

    public void setAccno(String accno)
    {
        this.accno = accno;
    }

    public String getIfsc()
    {
        return ifsc;
    }

    public void setIfsc(String ifsc)
    {
        this.ifsc = ifsc;
    }

    public double getPending()
    {
        return pending;
    }

    public void setPending(double pending)
    {
        this.pending = pending;
    }

    public double getDeposite()
    {
        return deposite;
    }

    public void setDeposite(double deposite)
    {
        this.deposite = deposite;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMob()
    {
        return mob;
    }

    public void setMob(String mob)
    {
        this.mob = mob;
    }

    public double getHafta()
    {
        return hafta;
    }

    public void setHafta(double hafta)
    {
        this.hafta = hafta;
    }
}

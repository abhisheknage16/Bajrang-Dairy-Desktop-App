package VTable;

/**
 * Created By Anil on 8/30/2018
 */
public class SalesGstReport {
    private long cid;
    private int snumber, srno;
    private String cname;
    private double totalmilk, avgfat, avgdegree, avgsnf, avgrate, tpayment;
    private double pending , deposited, hafta;

    public double getTotalmilk() {
        return totalmilk;
    }

    public void setTotalmilk(double totalmilk) { this.totalmilk = totalmilk; }

    public double getAvgfat() {
        return avgfat;
    }

    public void setAvgfat(double avgfat) {
        this.avgfat = avgfat;
    }

    public double getAvgdegree() {
        return avgdegree;
    }

    public void setAvgdegree(double avgdegree) {
        this.avgdegree = avgdegree;
    }

    public double getAvgsnf() {
        return avgsnf;
    }

    public void setAvgsnf(double avgsnf) {
        this.avgsnf = avgsnf;
    }

    public double getAvgrate() {
        return avgrate;
    }

    public void setAvgrate(double avgrate) {
        this.avgrate = avgrate;
    }

    public double getTpayment() {
        return tpayment;
    }

    public void setTpayment(double tpayment) {
        this.tpayment = tpayment;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getSnumber() {
        return snumber;
    }

    public void setSnumber(int snumber) {
        this.snumber = snumber;
    }


    public long getCid()
    {
        return cid;
    }

    public void setCid(long cid)
    {
        this.cid = cid;
    }

    public double getPending()
    {
        return pending;
    }

    public void setPending(double pending)
    {
        this.pending = pending;
    }

    public double getDeposited()
    {
        return deposited;
    }

    public void setDeposited(double deposited)
    {
        this.deposited = deposited;
    }

    public double getHafta()
    {
        return hafta;
    }

    public void setHafta(double hafta)
    {
        this.hafta = hafta;
    }

    public int getSrno()
    {
        return srno;
    }

    public void setSrno(int srno)
    {
        this.srno = srno;
    }
}

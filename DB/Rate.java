package DB;

import java.time.LocalDate;

/**
 * Created By Anil on 20/10/2018
 */
public class Rate {

    private long id;
    private int cur, d;
    private String name;
    private double orate, nrate, osrate, nsrate, odegree, ndegree, ofat, nfat;
    private LocalDate sdate, edate;

    public LocalDate getSdate() {
        return sdate;
    }

    public void setSdate(LocalDate sdate) {
        this.sdate = sdate;
    }

    public double getOrate() {
        return orate;
    }

    public void setOrate(double orate) {
        this.orate = orate;
    }

    public double getNrate() {
        return nrate;
    }

    public void setNrate(double nrate) {
        this.nrate = nrate;
    }

    public double getOsrate() {
        return osrate;
    }

    public void setOsrate(double osrate) {
        this.osrate = osrate;
    }

    public double getNsrate() {
        return nsrate;
    }

    public void setNsrate(double nsrate) {
        this.nsrate = nsrate;
    }

    public int getCur() {
        return cur;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    public LocalDate getEdate()
    {
        return edate;
    }

    public void setEdate(LocalDate edate)
    {
        this.edate = edate;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

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

    public double getOfat()
    {
        return ofat;
    }

    public void setOfat(double ofat)
    {
        this.ofat = ofat;
    }

    public double getNfat()
    {
        return nfat;
    }

    public void setNfat(double nfat)
    {
        this.nfat = nfat;
    }

    public double getOdegree()
    {
        return odegree;
    }

    public void setOdegree(double odegree)
    {
        this.odegree = odegree;
    }

    public double getNdegree()
    {
        return ndegree;
    }

    public void setNdegree(double ndegree)
    {
        this.ndegree = ndegree;
    }
}

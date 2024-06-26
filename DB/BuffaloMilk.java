package DB;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By Anil on 21/10/2018
 */
public class BuffaloMilk
{
        private long id;
        private int me, d;
        private Customer customer;
        private LocalDate date;
        private LocalTime time;
        private double cdegree, cfat, crate, degree, fat, gfat, rate, totalmilk, totalamt;

        public double getDegree() {
            return degree;
        }

        public void setDegree(double degree) {
            this.degree = degree;
        }

        public double getFat() {
            return fat;
        }

        public void setFat(double fat) {
            this.fat = fat;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public LocalTime getTime() {
            return time;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public double getCdegree() {
            return cdegree;
        }

        public void setCdegree(double cdegree) {
            this.cdegree = cdegree;
        }

        public double getCfat() {
            return cfat;
        }

        public void setCfat(double cfat) {
            this.cfat = cfat;
        }

        public double getCrate() {
            return crate;
        }

        public void setCrate(double crate) {
            this.crate = crate;
        }

        public double getTotalmilk() {
            return totalmilk;
        }

        public void setTotalmilk(double totalmilk) {
            this.totalmilk = totalmilk;
        }

        public double getTotalamt() {
            return totalamt;
        }

        public void setTotalamt(double totalamt) {
            this.totalamt = totalamt;
        }

    public int getMe() {
        return me;
    }

    public void setMe(int me) {
        this.me = me;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public double getGfat()
    {
        return gfat;
    }

    public void setGfat(double gfat)
    {
        this.gfat = gfat;
    }
}

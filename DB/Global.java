package DB;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Anil on 28/01/2018
 */
public class Global {
    public static String uname, llogin, client;
    public static int flag;
    public static long custid, cowfat, buffalofat, messageid, expenseid, cowmilkid, buffalomilkid;
    private static final SessionFactory ourSessionFactory;
    public static String left, top, center, labelColor, labelColor1;
    public static boolean lang = true;

    static
    {
        client = "बजरंग डेअरी";

        try {
            Configuration configuration=new Configuration();
            configuration.configure();
            ourSessionFactory=configuration.buildSessionFactory();
        }catch (Throwable e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() throws HibernateException
    {
        try {
            return ourSessionFactory.getCurrentSession();
        }
        catch (Exception e)
        {
            return ourSessionFactory.openSession();
        }
    }

    public static void closeFactory() throws Exception
    {
        ourSessionFactory.close();
    }

    public static double round(double value)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static double roundjkl(double value)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.UP);
        return bd.doubleValue();
    }
    public static double round1(double value)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static double round2(double d) {
        return Math.floor(d * 1e1) / 1e1;
    }

    public static boolean isDouble(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static boolean isMobile(String s)
    {
        try {
            if(s.equals(""))
                return true;
            long l = Long.parseLong(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
//        if(s.equals(""))
//            return true;
//        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
//        Matcher m = p.matcher(s);
//        return (m.find() && m.group().equals(s));
    }

    public static boolean isEmail(String email)
    {
        if(email == null)
            return true;
        if(email.equals(""))
            return true;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static int getRandomInt()
    {
        Random random = new Random();
        return random.ints(Integer.MIN_VALUE,(Integer.MAX_VALUE)).findFirst().getAsInt();
    }

    public static String getBillDate(LocalDate bdate)
    {
        int dom = bdate.getDayOfMonth();
        Month m = bdate.getMonth();
        int y = bdate.getYear();
        return ""+ dom +"-"+ m +"-"+ y;
    }
}

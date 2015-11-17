package donuseiei.test.com.authen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pai on 11/6/2015.
 */
public class Bill {
    private String name;
    private String month;
    private String rate;
    private String arrears;
    private String total;
    private String cpu,mem,network,storage;

    public Bill(){}
    public Bill(String name,String month,String rate,String arrears,String cpu,String mem,String network,String storage){
        this.name = name;
        this.month = getMonthString(month);
        this.rate = rate;
        this.arrears = arrears;
        double sum = Double.parseDouble(rate) + Double.parseDouble(arrears);
        this.total = sum+"";
        this.cpu = cpu;
        this.mem = mem;
        this.network = network;
        this.storage = storage;
    }

    private String getMonthString(String date){
        long time = Long.parseLong(date);
        Date d = new Date(time);
        SimpleDateFormat fm = new SimpleDateFormat("MMMM");
        return fm.format(d).toString();
    }

    public String getName(){return name;}
    public String getRate(){return rate;}
    public String getMonth(){return month;}
    public String getArrears(){return arrears;}
    public String getTotal(){return total;}
    public String getCpu(){return cpu;}
    public String getMem(){return mem;}
    public String getNetwork(){return network;}
    public String getStorage(){return storage;}
}

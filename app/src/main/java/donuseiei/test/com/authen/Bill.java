package donuseiei.test.com.authen;

/**
 * Created by Pai on 11/6/2015.
 */
public class Bill {
    private String name;
    private String month;
    private String sum;
    private String arrears;
    private String total;

    public Bill(){}
    public Bill(String name,String month,String sum,String arrears,String total){
        this.name = name;
        this.month = month;
        this.sum = sum;
        this.arrears = arrears;
        this.total = total;
    }

    public String getName(){return name;}
    public String getMonth(){return month;}
    public String getSum(){return sum;}
    public String getArrears(){return arrears;}
    public String getTotal(){return total;}

}

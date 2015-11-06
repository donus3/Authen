package donuseiei.test.com.authen;

/**
 * Created by Pai on 11/6/2015.
 */
public class ListBill {
    private String service;
    private String price;
    public ListBill(String service,String price){
        this.service = service;
        this.price = price;
    }
    public String getService() { return service; }
    public String getPrice() {
        return price;
    }

}

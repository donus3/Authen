package donuseiei.test.com.authen.page;

/**
 * Created by Pongpayak on 11/17/2015.
 */
public class Mail {
    private String title;
    private String mwssage;

    public Mail(String title, String mwssage) {
        this.title = title;
        this.mwssage = mwssage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMwssage() {
        return mwssage;
    }

    public void setMwssage(String mwssage) {
        this.mwssage = mwssage;
    }
}

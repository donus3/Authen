package donuseiei.test.com.authen;

import android.graphics.drawable.Drawable;

public class ListItemPlan {
    private String title;
    private String detail;
    private Integer icon;

    public ListItemPlan(String title, String detail, Integer icon) {
        this.title = title;
        this.detail = detail;
        this.icon = icon;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

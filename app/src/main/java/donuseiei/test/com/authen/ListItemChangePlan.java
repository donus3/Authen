package donuseiei.test.com.authen;

public class ListItemChangePlan {
    private String title;
    private String detailNew;
    private String detailOld;
    private Integer icon;

    public ListItemChangePlan(String title, String detailNew, String detailOld, Integer icon) {
        this.detailNew = detailNew;
        this.title = title;
        this.detailOld = detailOld;
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

    public String getDetailNew() {
        return detailNew;
    }

    public void setDetailNew(String detailNew) {
        this.detailNew = detailNew;
    }

    public String getDetailOld() {
        return detailOld;
    }

    public void setDetailOld(String detailOld) {
        this.detailOld = detailOld;
    }

    @Override
    public String
    toString() {
        return "ListItemChangePlan{" +
                "title='" + title + '\'' +
                ", detailNew='" + detailNew + '\'' +
                ", detailOld='" + detailOld + '\'' +
                '}';
    }
}

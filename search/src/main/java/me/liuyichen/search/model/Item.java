package me.liuyichen.search.model;

/**
 * Created by liuchen on 15/12/4.
 * and ...
 */
public class Item {

    private String ID;
    private String Title;
    private String BingUrl;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setBingUrl(String BingUrl) {
        this.BingUrl = BingUrl;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getBingUrl() {
        return BingUrl;
    }
}

package cn.linxi.iu.com.model;
/**
 * Created by BuZhiheng on 2016/5/17.
 */
public class Shared {
    private String share_title;
    private String share_describe;
    private String share_url;
    private String imgUrl;
    private int wxType;
    public String getTitle() {
        return share_title;
    }
    public void setTitle(String title) {
        this.share_title = title;
    }
    public String getDesc() {
        return share_describe;
    }
    public void setDesc(String desc) {
        this.share_describe = desc;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public int getWxType() {
        return wxType;
    }
    public void setWxType(int wxType) {
        this.wxType = wxType;
    }
    public String getUrl() {
        return share_url;
    }
    public void setUrl(String url) {
        this.share_url = url;
    }
}
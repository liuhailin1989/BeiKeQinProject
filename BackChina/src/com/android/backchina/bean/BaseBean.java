
package com.android.backchina.bean;

public abstract class BaseBean extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -3799845398300475504L;

    private String title;

    /**
     * 封面
     */
    private String pic;

    /**
     * 高清封面
     */
    private String bigpic;

    /**
     * 
     */
    private String video;

    /**
     * 
     */
    private String isad;

    /**
     * 
     */
    private String todayhot;

    /**
     * 来源
     */
    private String from;

    /**
     * 
     */
    private String redirect;

    /**
     * 
     */
    private String allowcomment;

    /**
     * 简介
     */
    private String summary;

    /**
     * 分类名称
     */
    private String catname;

    /**
     * 分类网址
     */
    private String caturl;

    /**
     * 网址
     */
    private String url;

    /**
     * api接口
     */
    private String urlapi;

    /**
     * 评论或者回帖数
     */
    private int comments = 0;

    /**
     * 点击数量
     */
    private String views;

    /**
     * 评论网址
     */
    private String commurl;

    /**
     * 评论API接口
     */
    private String commurlapi;

    /**
     * 作者ID
     */
    private String authorid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像（小）
     */
    private String avatar;

    /**
     * 用户头像（中）
     */
    private String avatar_middle;

    /**
     * 用户头像（大）
     */
    private String avatar_big;

    /**
     * 用户头像（默认）
     */
    private String avatar_default;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getIsad() {
        return isad;
    }

    public void setIsad(String isad) {
        this.isad = isad;
    }

    public String getTodayhot() {
        return todayhot;
    }

    public void setTodayhot(String todayhot) {
        this.todayhot = todayhot;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getAllowcomment() {
        return allowcomment;
    }

    public void setAllowcomment(String allowcomment) {
        this.allowcomment = allowcomment;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCaturl() {
        return caturl;
    }

    public void setCaturl(String caturl) {
        this.caturl = caturl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlapi() {
        return urlapi;
    }

    public void setUrlapi(String urlapi) {
        this.urlapi = urlapi;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getCommurl() {
        return commurl;
    }

    public void setCommurl(String commurl) {
        this.commurl = commurl;
    }

    public String getCommurlapi() {
        return commurlapi;
    }

    public void setCommurlapi(String commurlapi) {
        this.commurlapi = commurlapi;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    public void setAvatar_middle(String avatar_middle) {
        this.avatar_middle = avatar_middle;
    }

    public String getAvatar_big() {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big) {
        this.avatar_big = avatar_big;
    }

    public String getAvatar_default() {
        return avatar_default;
    }

    public void setAvatar_default(String avatar_default) {
        this.avatar_default = avatar_default;
    }
}

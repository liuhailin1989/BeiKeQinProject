package com.android.backchina.bean;

public class Blog extends Entity{
    
    public final static int DOC_TYPE_REPASTE = 0;//转帖
    public final static int DOC_TYPE_ORIGINAL = 1;//原创
    
    /**
     * 标题
     */
    private String title;

    /**
     * 链接
     */
    private String url;

    /**
     * 来源
     */
    private String where;

    private int commentCount;

    /**
     * 
     */
    private String body;

    /**
     * 作者名称
     */
    private String authorname;

    /**
     * 作者uid
     */
    private int authoruid;

    /**
     * 文章类型
     */
    private int documentType;

    private String pubDate;

    private int favorite;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return authorname;
    }

    public void setAuthor(String author) {
        this.authorname = author;
    }

    public int getAuthorId() {
        return authoruid;
    }

    public void setAuthorId(int authorId) {
        this.authoruid = authorId;
    }

    public int getDocumenttype() {
        return documentType;
    }

    public void setDocumenttype(int documenttype) {
        this.documentType = documenttype;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}

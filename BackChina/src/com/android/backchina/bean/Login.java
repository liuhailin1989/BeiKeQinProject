
package com.android.backchina.bean;

public class Login extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 2805083295300347308L;
    
    private int status;

    private int uid;

    private String username;

    private int groupid;

    private String resendemailurl;

    private String regdate;

    private int newprompt;

    private String hash;

    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getResendemailurl() {
        return resendemailurl;
    }

    public void setResendemailurl(String resendemailurl) {
        this.resendemailurl = resendemailurl;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public int getNewprompt() {
        return newprompt;
    }

    public void setNewprompt(int newprompt) {
        this.newprompt = newprompt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
    
}

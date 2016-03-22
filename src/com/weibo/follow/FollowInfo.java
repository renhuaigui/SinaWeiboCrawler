package com.weibo.follow;

public class FollowInfo {
    /** 本人UID. */
    private String uid = "";
    /** 关注或粉丝UID. */
    private String followUid= "";
    /** 关注或粉丝昵称. */
    private String followNickname= "";
    /** 关注或粉丝关注数. */
    private String followFollow= "";
    /** 关注或粉丝粉丝数. */
    private String followFans= "";
    /** 关注或粉丝微博数. */
    private String followWeibo= "";
    /** 关注或粉丝地址. */
    private String followAddr= "";
    /** 关注或粉丝认证. */
    private String followCert= "";
    /** 通过什么关注. */
    private String followFrom= "";
    
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getFollowUid() {
        return followUid;
    }
    public void setFollowUid(String followUid) {
        this.followUid = followUid;
    }
    public String getFollowNickname() {
        return followNickname;
    }
    public void setFollowNickname(String followNickname) {
        this.followNickname = followNickname;
    }
    public String getFollowFollow() {
        return followFollow;
    }
    public void setFollowFollow(String followFollow) {
        this.followFollow = followFollow;
    }
    public String getFollowFans() {
        return followFans;
    }
    public void setFollowFans(String followFans) {
        this.followFans = followFans;
    }
    public String getFollowWeibo() {
        return followWeibo;
    }
    public void setFollowWeibo(String followWeibo) {
        this.followWeibo = followWeibo;
    }
    public void setFollowAddr(String followAdd) {
        this.followAddr = followAdd;
    }
    public String getFollowAddr() {
        return followAddr;
    }
    public void setFollowCert(String followCert) {
        this.followCert = followCert;
    }
    public String getFollowCert() {
        return followCert;
    }
    public void setFollowFrom(String followFrom) {
        this.followFrom = followFrom;
    }
    public String getFollowFrom() {
        return followFrom;
    }
    
}

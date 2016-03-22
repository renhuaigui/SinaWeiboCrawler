package com.weibo.content;

public class MsgInfo {
    /** 作者ID. */
    private String userId = "";
    /** 微博内容. */
    private String content="";
    /** 微博Mid. */
    private String mid="";
    /** 是否是转发微博： 0：不是，1：是. */
    private String isForward="0";
    /** 微博发布者名称. */
    private String authorName="";
    /** 微博认证. */
    private String identity="";
    /** 微博转发数. */
    private String forward="";
    /** 微博收藏数. */
    private String favorite="";
    /** 微博评论数. */
    private String comment="";
    /** 微博发布时间. */
    private String date="";
    /** 微博发布时间. */
    private String dateString="";
    /** 微博发布来源. */
    private String comeFrom="";
    /** 地图信息. */
    private String mapData="";
    /** 赞的数量. */
    private String heart="";
    /** 微博访问地址. */
    private String href="";
    /** 转发UID. */
    private String forwardUid="";
    /** 转发作者. */
    private String forwardAuthor="";
    /** 转发内容. */
    private String forwardContent="";
    /** pictureNum : 图片数量*/
    private String pictureNum="";
    /** pictureHerf : 图片地址*/
    private String pictureHerf="";

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getIsforward() {
        return isForward;
    }
    public void setIsforward(String isforward) {
        this.isForward = isforward;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public String getForward() {
        return forward;
    }
    public void setForward(String forward) {
        this.forward = forward;
    }
    public String getFavorite() {
        return favorite;
    }
    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDateString() {
        return dateString;
    }
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
    public String getComeFrom() {
        return comeFrom;
    }
    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }
    public String getMapData() {
        return mapData;
    }
    public void setMapData(String mapData) {
        this.mapData = mapData;
    }
    public String getHeart() {
        return heart;
    }
    public void setHeart(String heart) {
        this.heart = heart;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public String getForwardUid() {
        return forwardUid;
    }
    public void setForwardUid(String forwardUid) {
        this.forwardUid = forwardUid;
    }
    public String getForwardAuthor() {
        return forwardAuthor;
    }
    public void setForwardAuthor(String forwardAuthor) {
        this.forwardAuthor = forwardAuthor;
    }
    public String getForwardContent() {
        return forwardContent;
    }
    public void setForwardContent(String forwardContent) {
        this.forwardContent = forwardContent;
    }
	/**  @return pictureHerf */ 
	public String getPictureHerf() {
		return pictureHerf;
	}
	/** @param pictureHerf 要设置的 pictureHerf */ 
	public void setPictureHerf(String Herf) {
		if(this.pictureHerf.equals(""))
			this.pictureHerf = Herf;
		else 
			this.pictureHerf = this.pictureHerf+","+Herf;
	}
	/**  @return pictureNum */ 
	public String getPictureNum() {
		return pictureNum;
	}
	/** @param pictureNum 要设置的 pictureNum */ 
	public void setPictureNum(String pictureNum) {
		this.pictureNum = pictureNum;
	}
}

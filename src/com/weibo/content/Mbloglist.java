package com.weibo.content;

public class Mbloglist {
    /** _wv=5，版本是5. */
    private int _wv = 5;
    /** page=1，第1页. */
    private int page = 1;
    /** count=15，默认返回15个item   第二次和第二次访问时都是15，第一次访问时是50. */
    private int count = 15;
    /** pre_page=1，来自1页  第二次和第三次都是本页页码，第一次访问是上页页码. */
    private int pre_page = 1;
    /** 需要读取用户的ID. */
    private String uid = "1729332983";
    /** 固定值, 16位貌似的随机参数   本次登录第一次访问此微薄的时间，16位整数. */
    private long _k = 136774190205537L;
    /** 13位随机数字     访问这一页面的时间，以秒表示的13位整数. */
    private long __rnd = 1367741949547L;
    /** 最新的这一项微博的mid. */
    private String end_id = "3574218661218704";
    /** 已经访问到的，也就是lazyload上面的这一项最旧的微博的mid. */
    private String max_id = "3573570770923927";
    /** 起始日期. */
    private String startDate = "2014-06-01";
    /** 终止日期. */
    private String endDate = "2014-07-01";
    /** 第二次是0，第三次是1，第一次没有这项. */
    private int pagebar = 0;
    /** 0.*/
    private int _t = 0;
    public int get_wv() {
        return _wv;
    }
    public void set_wv(int _wv) {
        this._wv = _wv;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getPre_page() {
        return pre_page;
    }
    public void setPre_page(int pre_page) {
        this.pre_page = pre_page;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public long get_k() {
        return _k;
    }
    public void set_k(long _k) {
        this._k = _k;
    }
    public long get__rnd() {
        return __rnd;
    }
    public void set__rnd(long __rnd) {
        this.__rnd = __rnd;
    }
    public String getEnd_id() {
        return end_id;
    }
    public void setEnd_id(String end_id) {
        this.end_id = end_id;
    }
    public String getMax_id() {
        return max_id;
    }
    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }
    public int getPagebar() {
        return pagebar;
    }
    public void setPagebar(int pagebar) {
        this.pagebar = pagebar;
    }
    public int get_t() {
        return _t;
    }
    public void set_t(int _t) {
        this._t = _t;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("_wv=" + _wv);
        buffer.append("&pre_page=" + pre_page + "?opener");
        buffer.append("&page=" + page + "?opener");
        buffer.append("&max_id=" + max_id);
        buffer.append("&end_id=" + end_id);
        buffer.append("&count=" + count);
        buffer.append("&pagebar=" + pagebar);
        buffer.append("&_k=" + _k);
        buffer.append("&uid=" + uid);
        buffer.append("&_t=" + _t);
        buffer.append("&__rnd=" + __rnd);
        buffer.append("&is_search=1&visible=0&is_ori=1&is_pic=1&is_tag=0");
        buffer.append("&start_time=" + startDate);
        buffer.append("&end_time=" + endDate);
        return buffer.toString();
    }
}
//buffer.append("&is_search=1&visible=0&is_ori=1&is_pic=1&is_video=1&is_music=1&is_forward=1&is_text=1&is_tag=0");
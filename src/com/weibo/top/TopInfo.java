package com.weibo.top;

public class TopInfo {
    private String cat;
    private String area;
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    private int month;
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    private int top;
    private String uid;
    private String nick;
    private String influ;
    private String descrip;
    public String getCat() {
        return cat;
    }
    public void setCat(String cat) {
        this.cat = cat;
    }
    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getInflu() {
        return influ;
    }
    public void setInflu(String influ) {
        this.influ = influ;
    }
    public String getDescrip() {
        return descrip;
    }
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}

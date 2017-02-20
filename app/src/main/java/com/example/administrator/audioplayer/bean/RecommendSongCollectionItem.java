package com.example.administrator.audioplayer.bean;


/**
 * Created by on 2017/2/17 0017.
 * 热门歌单
 * jsonbean里面RecommendSongCollection内的内容部分
 */

public class RecommendSongCollectionItem {
    /**
     * listid : 7259
     * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_5f2ecb9b02e287b82e12b84a6c93bbdf.jpg
     * listenum : 26775
     * collectnum : 337
     * title : 东海音乐节，去海边沙滩听音乐
     * tag : 流行,摇滚,民谣
     * type : gedan
     */

    private String listid;
    private String pic;
    private String listenum;
    private String collectnum;
    private String title;
    private String tag;
    private String type;

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getListenum() {
        return listenum;
    }

    public void setListenum(String listenum) {
        this.listenum = listenum;
    }

    public String getCollectnum() {
        return collectnum;
    }

    public void setCollectnum(String collectnum) {
        this.collectnum = collectnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


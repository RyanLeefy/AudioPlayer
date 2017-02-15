package com.example.administrator.audioplayer.jsonbean;

import java.util.List;

/**
 * Created by on 2017/2/15.
 */


public class Lru {

    /**
     * error_code : 22000
     * songinfo : [{"lrclink":"http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":7311823,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"992549366f8ca58d783f7ecb75f99d94","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":399328,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_500","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/1688095/1688095.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":328051,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"992549366f8ca58d783f7ecb75f99d94","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":1688094,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/40080522/40080522.jpg@s_0,w_180","pic_s500":"","title":"勇","artist_id":102},{"lrclink":"","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":276907819,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":276920078,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/842afdba24eadb777b81ec1562610398/276907820/276907820.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/842afdba24eadb777b81ec1562610398/276907820/276907820.jpg@s_0,w_500","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/13758172/13758172.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":13757322,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"992549366f8ca58d783f7ecb75f99d94","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":13757366,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"","pic_s500":"","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/34117179/34117179.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":14733373,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":14733453,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"http://musicdata.baidu.com/data2/pic/4ae0f5ae35892f98fe3f453f74f572c3/264911404/264911404.jpg","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/4ae0f5ae35892f98fe3f453f74f572c3/264911404/264911404.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/4ae0f5ae35892f98fe3f453f74f572c3/264911404/264911404.jpg@s_0,w_500","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/910569/910569.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":794925,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":910568,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/d106e968789886616e86cd06457c7ab9/262033130/262033130.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/d106e968789886616e86cd06457c7ab9/262033130/262033130.jpg","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/c830d31b5e2b1bba7fa8272379527dd1/260738675/260738675.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":116464653,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"f197e750d46720a285217412f609ca9f","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":116465116,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"","pic_s500":"","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/60cbc858209ef0084c07202996f7d6c3/251694355/251694355.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":15896218,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":15896500,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/123342173/123342173.jpeg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/123342173/123342173.jpeg@s_0,w_500","title":"勇","artist_id":102},{"lrclink":"http://musicdata.baidu.com/data2/lrc/65454718/65454718.lrc","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":53142694,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":53145567,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/fbb573e756cb17c103b787e3a1b0b125/262017490/262017490.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/fbb573e756cb17c103b787e3a1b0b125/262017490/262017490.jpg","title":"勇","artist_id":102},{"lrclink":"","artist_480_800":"http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg","avatar_s500":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500","album_id":241898350,"author":"杨千嬅","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg","song_title":"Overture / 勇","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180","song_id":241898209,"artist_640_1136":"http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg","pic_s1000":"http://musicdata.baidu.com/data2/pic/241898292/241898292.jpg@s_0,w_1000","pic_type":2,"pic_s180":"http://musicdata.baidu.com/data2/pic/241898292/241898292.jpg@s_0,w_180","pic_s500":"http://musicdata.baidu.com/data2/pic/241898292/241898292.jpg@s_0,w_500","title":"Overture / 勇","artist_id":102}]
     */

    private int error_code;
    private List<SonginfoBean> songinfo;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SonginfoBean> getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(List<SonginfoBean> songinfo) {
        this.songinfo = songinfo;
    }

    public static class SonginfoBean {
        /**
         * lrclink : http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc
         * artist_480_800 : http://musicdata.baidu.com/data2/pic/105448554/105448554.jpg
         * avatar_s500 : http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_500
         * album_id : 7311823
         * author : 杨千嬅
         * artist_1000_1000 : http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg
         * song_title : 勇
         * lrc_md5 : 992549366f8ca58d783f7ecb75f99d94
         * avatar_s180 : http://musicdata.baidu.com/data2/pic/246586408/246586408.jpg@s_0,w_180
         * song_id : 399328
         * artist_640_1136 : http://musicdata.baidu.com/data2/pic/105448545/105448545.jpg
         * pic_s1000 :
         * pic_type : 2
         * pic_s180 : http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_180
         * pic_s500 : http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_500
         * title : 勇
         * artist_id : 102
         */

        private String lrclink;
        private String artist_480_800;
        private String avatar_s500;
        private int album_id;
        private String author;
        private String artist_1000_1000;
        private String song_title;
        private String lrc_md5;
        private String avatar_s180;
        private int song_id;
        private String artist_640_1136;
        private String pic_s1000;
        private int pic_type;
        private String pic_s180;
        private String pic_s500;
        private String title;
        private int artist_id;

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getArtist_480_800() {
            return artist_480_800;
        }

        public void setArtist_480_800(String artist_480_800) {
            this.artist_480_800 = artist_480_800;
        }

        public String getAvatar_s500() {
            return avatar_s500;
        }

        public void setAvatar_s500(String avatar_s500) {
            this.avatar_s500 = avatar_s500;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getArtist_1000_1000() {
            return artist_1000_1000;
        }

        public void setArtist_1000_1000(String artist_1000_1000) {
            this.artist_1000_1000 = artist_1000_1000;
        }

        public String getSong_title() {
            return song_title;
        }

        public void setSong_title(String song_title) {
            this.song_title = song_title;
        }

        public String getLrc_md5() {
            return lrc_md5;
        }

        public void setLrc_md5(String lrc_md5) {
            this.lrc_md5 = lrc_md5;
        }

        public String getAvatar_s180() {
            return avatar_s180;
        }

        public void setAvatar_s180(String avatar_s180) {
            this.avatar_s180 = avatar_s180;
        }

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public String getArtist_640_1136() {
            return artist_640_1136;
        }

        public void setArtist_640_1136(String artist_640_1136) {
            this.artist_640_1136 = artist_640_1136;
        }

        public String getPic_s1000() {
            return pic_s1000;
        }

        public void setPic_s1000(String pic_s1000) {
            this.pic_s1000 = pic_s1000;
        }

        public int getPic_type() {
            return pic_type;
        }

        public void setPic_type(int pic_type) {
            this.pic_type = pic_type;
        }

        public String getPic_s180() {
            return pic_s180;
        }

        public void setPic_s180(String pic_s180) {
            this.pic_s180 = pic_s180;
        }

        public String getPic_s500() {
            return pic_s500;
        }

        public void setPic_s500(String pic_s500) {
            this.pic_s500 = pic_s500;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(int artist_id) {
            this.artist_id = artist_id;
        }
    }
}

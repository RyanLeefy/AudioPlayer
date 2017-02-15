package com.example.administrator.audioplayer.jsonbean;

import java.util.List;

/**
 * Created by on 2017/2/15.
 * 音乐榜单
 * http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.billboard.billList&type=1&offset=0&size=3&field=song_id%2Ctitle%2Cauthor%2Calbum_title%2Cpic_big%2Cpic_small%2Chavehigh%2Call_rate%2Ccharge%2Chas_mv_mobile%2Clearn%2Csong_source%2Ckorean_bb_song
 */

public class BillBoard {

    /**
     * song_list : [{"artist_id":"466","language":"国语","pic_big":"http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_90","country":"内地","area":"0","publishtime":"2017-01-27","album_no":"10","lrclink":"http://musicdata.baidu.com/data2/lrc/b3f9563cfbdfa3a444b2f62e418a0398/533366602/533366602.lrc","copy_type":"1","hot":"106492","all_artist_ting_uid":"1287,1035","resource_type":"0","is_new":"1","rank_change":"2","rank":"1","all_artist_id":"466,14","style":"","del_status":"0","relate_status":"0","toneid":"0","all_rate":"64,128","file_duration":253,"has_mv_mobile":0,"versions":"现场","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_id":"526077428","title":"满城烟花","ting_uid":"1287","author":"毛阿敏,张杰","album_id":"525872694","album_title":"2017中央电视台春节联欢晚会","is_first_publish":0,"havehigh":0,"charge":0,"has_mv":0,"learn":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"毛阿敏,张杰"},{"artist_id":"57297","language":"国语","pic_big":"http://musicdata.baidu.com/data2/pic/522767550/f35e3b11b1a8b14afe8c02688e48502c/522767550.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/522767550/f35e3b11b1a8b14afe8c02688e48502c/522767550.jpg@s_0,w_90","country":"内地","area":"0","publishtime":"2017-01-11","album_no":"5","lrclink":"http://musicdata.baidu.com/data2/lrc/84d58dd3489dbb1c21ed445ef3edd6cb/277389542/277389542.lrc","copy_type":"1","hot":"522239","all_artist_ting_uid":"245815","resource_type":"0","is_new":"0","rank_change":"0","rank":"2","all_artist_id":"57297","style":"流行","del_status":"0","relate_status":"0","toneid":"0","all_rate":"flac,320,256,128,64","file_duration":205,"has_mv_mobile":0,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_id":"277389316","title":"一生为你感动","ting_uid":"245815","author":"祁隆","album_id":"256028619","album_title":"老父亲","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":0,"learn":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"祁隆"},{"artist_id":"86767985","language":"国语","pic_big":"http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_90","country":"内地","area":"0","publishtime":"2017-01-27","album_no":"1","lrclink":"http://musicdata.baidu.com/data2/lrc/eff717a54c3a6462c2bef791b6869eb4/533366505/533366505.lrc","copy_type":"1","hot":"96428","all_artist_ting_uid":"81381913,239558924,49076918,239560687,99828566,49996675","resource_type":"0","is_new":"1","rank_change":"1","rank":"3","all_artist_id":"86767985,262710489,55371,264865152,34186563,35368971","style":"","del_status":"0","relate_status":"0","toneid":"0","all_rate":"64,128,256,320","file_duration":247,"has_mv_mobile":0,"versions":"现场","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_id":"526046037","title":"美丽中国年","ting_uid":"81381913","author":"TFBOYS,乔欣,杨紫,王子文,蒋欣,刘涛","album_id":"525872694","album_title":"2017中央电视台春节联欢晚会","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":0,"learn":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"TFBOYS,乔欣,杨紫,王子文,蒋欣,刘涛"}]
     * billboard : {"billboard_type":"1","billboard_no":"2106","update_date":"2017-02-15","billboard_songnum":"190","havemore":1,"name":"新歌榜","comment":"该榜单是根据百度音乐平台歌曲每日播放量自动生成的数据榜单，统计范围为近期发行的歌曲，每日更新一次","pic_s640":"http://c.hiphotos.baidu.com/ting/pic/item/f7246b600c33874495c4d089530fd9f9d62aa0c6.jpg","pic_s444":"http://d.hiphotos.baidu.com/ting/pic/item/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg","pic_s260":"http://b.hiphotos.baidu.com/ting/pic/item/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg","pic_s210":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_c49310115801d43d42a98fdc357f6057.jpg","web_url":"http://music.baidu.com/top/new"}
     * error_code : 22000
     */

    private BillboardBean billboard;
    private int error_code;
    private List<SongListBean> song_list;

    public BillboardBean getBillboard() {
        return billboard;
    }

    public void setBillboard(BillboardBean billboard) {
        this.billboard = billboard;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SongListBean> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongListBean> song_list) {
        this.song_list = song_list;
    }

    public static class BillboardBean {
        /**
         * billboard_type : 1
         * billboard_no : 2106
         * update_date : 2017-02-15
         * billboard_songnum : 190
         * havemore : 1
         * name : 新歌榜
         * comment : 该榜单是根据百度音乐平台歌曲每日播放量自动生成的数据榜单，统计范围为近期发行的歌曲，每日更新一次
         * pic_s640 : http://c.hiphotos.baidu.com/ting/pic/item/f7246b600c33874495c4d089530fd9f9d62aa0c6.jpg
         * pic_s444 : http://d.hiphotos.baidu.com/ting/pic/item/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg
         * pic_s260 : http://b.hiphotos.baidu.com/ting/pic/item/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg
         * pic_s210 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_c49310115801d43d42a98fdc357f6057.jpg
         * web_url : http://music.baidu.com/top/new
         */

        private String billboard_type;
        private String billboard_no;
        private String update_date;
        private String billboard_songnum;
        private int havemore;
        private String name;
        private String comment;
        private String pic_s640;
        private String pic_s444;
        private String pic_s260;
        private String pic_s210;
        private String web_url;

        public String getBillboard_type() {
            return billboard_type;
        }

        public void setBillboard_type(String billboard_type) {
            this.billboard_type = billboard_type;
        }

        public String getBillboard_no() {
            return billboard_no;
        }

        public void setBillboard_no(String billboard_no) {
            this.billboard_no = billboard_no;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public String getBillboard_songnum() {
            return billboard_songnum;
        }

        public void setBillboard_songnum(String billboard_songnum) {
            this.billboard_songnum = billboard_songnum;
        }

        public int getHavemore() {
            return havemore;
        }

        public void setHavemore(int havemore) {
            this.havemore = havemore;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPic_s640() {
            return pic_s640;
        }

        public void setPic_s640(String pic_s640) {
            this.pic_s640 = pic_s640;
        }

        public String getPic_s444() {
            return pic_s444;
        }

        public void setPic_s444(String pic_s444) {
            this.pic_s444 = pic_s444;
        }

        public String getPic_s260() {
            return pic_s260;
        }

        public void setPic_s260(String pic_s260) {
            this.pic_s260 = pic_s260;
        }

        public String getPic_s210() {
            return pic_s210;
        }

        public void setPic_s210(String pic_s210) {
            this.pic_s210 = pic_s210;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }

    public static class SongListBean {
        /**
         * artist_id : 466
         * language : 国语
         * pic_big : http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_150
         * pic_small : http://musicdata.baidu.com/data2/pic/d869b6a2a29dfae6b3d189f459e23c36/531688058/531688058.jpg@s_0,w_90
         * country : 内地
         * area : 0
         * publishtime : 2017-01-27
         * album_no : 10
         * lrclink : http://musicdata.baidu.com/data2/lrc/b3f9563cfbdfa3a444b2f62e418a0398/533366602/533366602.lrc
         * copy_type : 1
         * hot : 106492
         * all_artist_ting_uid : 1287,1035
         * resource_type : 0
         * is_new : 1
         * rank_change : 2
         * rank : 1
         * all_artist_id : 466,14
         * style :
         * del_status : 0
         * relate_status : 0
         * toneid : 0
         * all_rate : 64,128
         * file_duration : 253
         * has_mv_mobile : 0
         * versions : 现场
         * bitrate_fee : {"0":"0|0","1":"0|0"}
         * song_id : 526077428
         * title : 满城烟花
         * ting_uid : 1287
         * author : 毛阿敏,张杰
         * album_id : 525872694
         * album_title : 2017中央电视台春节联欢晚会
         * is_first_publish : 0
         * havehigh : 0
         * charge : 0
         * has_mv : 0
         * learn : 0
         * song_source : web
         * piao_id : 0
         * korean_bb_song : 0
         * resource_type_ext : 0
         * mv_provider : 0000000000
         * artist_name : 毛阿敏,张杰
         */

        private String artist_id;
        private String language;
        private String pic_big;
        private String pic_small;
        private String country;
        private String area;
        private String publishtime;
        private String album_no;
        private String lrclink;
        private String copy_type;
        private String hot;
        private String all_artist_ting_uid;
        private String resource_type;
        private String is_new;
        private String rank_change;
        private String rank;
        private String all_artist_id;
        private String style;
        private String del_status;
        private String relate_status;
        private String toneid;
        private String all_rate;
        private int file_duration;
        private int has_mv_mobile;
        private String versions;
        private String bitrate_fee;
        private String song_id;
        private String title;
        private String ting_uid;
        private String author;
        private String album_id;
        private String album_title;
        private int is_first_publish;
        private int havehigh;
        private int charge;
        private int has_mv;
        private int learn;
        private String song_source;
        private String piao_id;
        private String korean_bb_song;
        private String resource_type_ext;
        private String mv_provider;
        private String artist_name;

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getAlbum_no() {
            return album_no;
        }

        public void setAlbum_no(String album_no) {
            this.album_no = album_no;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getCopy_type() {
            return copy_type;
        }

        public void setCopy_type(String copy_type) {
            this.copy_type = copy_type;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getAll_artist_ting_uid() {
            return all_artist_ting_uid;
        }

        public void setAll_artist_ting_uid(String all_artist_ting_uid) {
            this.all_artist_ting_uid = all_artist_ting_uid;
        }

        public String getResource_type() {
            return resource_type;
        }

        public void setResource_type(String resource_type) {
            this.resource_type = resource_type;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getRank_change() {
            return rank_change;
        }

        public void setRank_change(String rank_change) {
            this.rank_change = rank_change;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getAll_artist_id() {
            return all_artist_id;
        }

        public void setAll_artist_id(String all_artist_id) {
            this.all_artist_id = all_artist_id;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getRelate_status() {
            return relate_status;
        }

        public void setRelate_status(String relate_status) {
            this.relate_status = relate_status;
        }

        public String getToneid() {
            return toneid;
        }

        public void setToneid(String toneid) {
            this.toneid = toneid;
        }

        public String getAll_rate() {
            return all_rate;
        }

        public void setAll_rate(String all_rate) {
            this.all_rate = all_rate;
        }

        public int getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(int file_duration) {
            this.file_duration = file_duration;
        }

        public int getHas_mv_mobile() {
            return has_mv_mobile;
        }

        public void setHas_mv_mobile(int has_mv_mobile) {
            this.has_mv_mobile = has_mv_mobile;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public int getIs_first_publish() {
            return is_first_publish;
        }

        public void setIs_first_publish(int is_first_publish) {
            this.is_first_publish = is_first_publish;
        }

        public int getHavehigh() {
            return havehigh;
        }

        public void setHavehigh(int havehigh) {
            this.havehigh = havehigh;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public int getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(int has_mv) {
            this.has_mv = has_mv;
        }

        public int getLearn() {
            return learn;
        }

        public void setLearn(int learn) {
            this.learn = learn;
        }

        public String getSong_source() {
            return song_source;
        }

        public void setSong_source(String song_source) {
            this.song_source = song_source;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }

        public String getKorean_bb_song() {
            return korean_bb_song;
        }

        public void setKorean_bb_song(String korean_bb_song) {
            this.korean_bb_song = korean_bb_song;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getMv_provider() {
            return mv_provider;
        }

        public void setMv_provider(String mv_provider) {
            this.mv_provider = mv_provider;
        }

        public String getArtist_name() {
            return artist_name;
        }

        public void setArtist_name(String artist_name) {
            this.artist_name = artist_name;
        }
    }
}

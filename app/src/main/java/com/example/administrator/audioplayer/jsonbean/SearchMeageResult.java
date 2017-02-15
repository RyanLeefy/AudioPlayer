package com.example.administrator.audioplayer.jsonbean;

/**
 * Created by on 2017/2/15.
 */

import java.util.List;

/**
 * 综合搜索，从歌手，唱片，歌曲中搜索
 */
public class SearchMeageResult {

    /**
     * error_code : 22000
     * result : {"artist_info":{"artist_list":[{"ting_uid":"60430974","song_num":2,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/2FAFAB3D2CBFD2C96876DEE61D9AF5C2/254195147/254195147.jpg@s_0,w_120","album_num":0,"artist_desc":"","author":"<em>勇<\/em>钢","artist_source":"web","artist_id":"43697687"},{"ting_uid":"7144","song_num":20,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/B7540C7798319D894499E89B413ABEF4/252306869/252306869.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"路<em>勇<\/em>","artist_source":"web","artist_id":"1989755"},{"ting_uid":"1591","song_num":27,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/4EC863156EBB42895A7F0331D19E81FA/252057741/252057741.jpg@s_0,w_120","album_num":8,"artist_desc":"","author":"黄<em>勇<\/em>","artist_source":"web","artist_id":"1682"},{"ting_uid":"65954136","song_num":14,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/pic/86478269/86478269.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"秦<em>勇<\/em>","artist_source":"web","artist_id":"14384918"},{"ting_uid":"5055","song_num":6,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/0A87679B211FB8202275CD896E212881/252416151/252416151.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"曾<em>勇<\/em>","artist_source":"web","artist_id":"2000943"},{"ting_uid":"1303","song_num":1,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/7666DB7A3712885A5E4066632E660BCB/252053278/252053278.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"何<em>勇<\/em>","artist_source":"web","artist_id":"512"},{"ting_uid":"78005625","song_num":9,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/69E005EC156C0E80DCA322990682C51A/254355635/254355635.jpg@s_0,w_120","album_num":3,"artist_desc":"","author":"李<em>勇<\/em>","artist_source":"web","artist_id":"71849826"},{"ting_uid":"129620706","song_num":19,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/pic/117362177/117362177.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"刘<em>勇<\/em>","artist_source":"web","artist_id":"117362196"},{"ting_uid":"7367","song_num":7,"country":"中国","avatar_middle":"","album_num":3,"artist_desc":"","author":"梁<em>勇<\/em>","artist_source":"web","artist_id":"1990448"},{"ting_uid":"1935","song_num":23,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/66E7587D680BF9E4ABEC5015DAD4705C/252067466/252067466.jpg@s_0,w_120","album_num":5,"artist_desc":"","author":"王<em>勇<\/em>","artist_source":"web","artist_id":"4680"}],"total":37},"album_info":{"album_list":[{"all_artist_id":"19064521","publishtime":"2012-03-26","company":"","album_desc":"","title":"<em>勇<\/em>敢的家伙们","album_id":"19078721","pic_small":"http://musicdata.baidu.com/data2/pic/41321786/41321786.jpg@s_0,w_90","hot":0,"author":"<em>勇<\/em>敢的家伙们","artist_id":"19064521"},{"all_artist_id":"14437870","publishtime":"2015-09-12","company":"欧乐文化","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"247095785","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","hot":1,"author":"丁<em>勇<\/em>","artist_id":"14437870"},{"all_artist_id":"1898","publishtime":"2014-03-03","company":"世纪博英","album_desc":"","title":"<em>勇<\/em>敢向前冲","album_id":"115165779","pic_small":"http://musicdata.baidu.com/data2/pic/115165761/115165761.jpg@s_0,w_90","hot":31,"author":"顾峰","artist_id":"1898"},{"all_artist_id":"256271373","publishtime":"2015-10-22","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"256272707","pic_small":"http://musicdata.baidu.com/data2/pic/256272297/256272297.jpg@s_0,w_90","hot":0,"author":"刘东允","artist_id":"256271373"},{"all_artist_id":"265490365","publishtime":"2016-05-11","company":"希望无限","album_desc":"","title":"<em>勇<\/em>闯新境界","album_id":"265490391","pic_small":"http://musicdata.baidu.com/data2/pic/265490302/265490302.jpg@s_0,w_90","hot":0,"author":"傅超华","artist_id":"265490365"},{"all_artist_id":"59913362","publishtime":"2016-05-21","company":"金丝美文化","album_desc":"","title":"<em>勇<\/em>敢吧(708090之深圳恋歌主题曲)","album_id":"265754096","pic_small":"http://musicdata.baidu.com/data2/pic/265753874/265753874.jpg@s_0,w_90","hot":8,"author":"万里","artist_id":"59913362"},{"all_artist_id":"1562,116534293","publishtime":"2014-03-03","company":"SONY MUSIC","album_desc":"","title":"<em>勇<\/em>者的浪漫","album_id":"118658497","pic_small":"http://musicdata.baidu.com/data2/pic/118658506/118658506.jpg@s_0,w_90","hot":27,"author":"陈柏宇,VnP","artist_id":"1562"},{"all_artist_id":"242747896","publishtime":"2015-11-26","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢厦门人","album_id":"259104616","pic_small":"http://musicdata.baidu.com/data2/pic/259104017/259104017.jpg@s_0,w_90","hot":0,"author":"方贵明","artist_id":"242747896"},{"all_artist_id":"106262503","publishtime":"2014-11-06","company":"凤凰涅盘","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"124374459","pic_small":"http://musicdata.baidu.com/data2/pic/124374432/124374432.jpg@s_0,w_90","hot":3,"author":"张添艺","artist_id":"106262503"},{"all_artist_id":"14458230","publishtime":"2016-02-19","company":"华月文化","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"262702980","pic_small":"http://musicdata.baidu.com/data2/pic/262699130/262699130.jpg@s_0,w_90","hot":7,"author":"齐航","artist_id":"14458230"}],"total":164},"query":"勇","song_info":{"song_list":[{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"千嬅盛放","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"7311823","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_90","song_id":"399328","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"C1","content":"","piao_id":"0","artist_id":"44","lrclink":"http://musicdata.baidu.com/data2/lrc/270389/270389.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"7311916","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":0,"author":"张柏芝","pic_small":"http://musicdata.baidu.com/data2/pic/12f74d8f1b045a34464cde277c57e3d7/262031891/262031891.jpg@s_0,w_90","song_id":"270388","all_artist_id":"44","ting_uid":"8545"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千嬅万紫千红演唱会","content":"","piao_id":"0","artist_id":"102","lrclink":"","data_source":0,"relate_status":0,"learn":0,"album_id":"276907819","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/842afdba24eadb777b81ec1562610398/276907820/276907820.jpg@s_0,w_90","song_id":"276920078","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":1,"album_title":"广东经典101 Vol.2","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/c830d31b5e2b1bba7fa8272379527dd1/260738675/260738675.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"116464653","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"116465116","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"Y Best","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13758172/13758172.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"13757322","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"13757366","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千桦对杨千桦","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/1688095/1688095.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"328051","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/40080522/40080522.jpg@s_0,w_90","song_id":"1688094","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"THE IDOLM@STER STATION!!! SECOND TRAVEL ～Seaside Date～","content":"","piao_id":"0","artist_id":"19428108","lrclink":"http://musicdata.baidu.com/data2/lrc/6185fbd9f1098e7bfab99e573a4a73b9/277344005/277344005.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26079200","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64","title":"勇気りんりん","has_mv_mobile":0,"author":"日本ACG","pic_small":"http://musicdata.baidu.com/data2/music/671EB039806857C0C3F0C0510F5BDA84/253601662/253601662.jpg@s_0,w_90","song_id":"26079686","all_artist_id":"19428108","ting_uid":"90654830"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125553/247125553.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095786","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"伴奏","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125561/247125561.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095787","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"现场","toneid":"0","info":"","has_mv":0,"album_title":"魅力音乐会","content":"","piao_id":"0","artist_id":"385","lrclink":"http://musicdata.baidu.com/data2/lrc/202cf2b688c5cea5d0c969c8d2184e90/261926928/261926928.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26695624","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,96,128,192,256,320,flac","title":"勇往直前","has_mv_mobile":0,"author":"女子十二乐坊","pic_small":"http://musicdata.baidu.com/data2/music/E64EF61D835319E9D8A1994F512153BE/253812738/253812738.jpg@s_0,w_90","song_id":"26695912","all_artist_id":"385","ting_uid":"245608"}],"total":1197},"rqt_type":1,"syn_words":""}
     */

    private int error_code;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * artist_info : {"artist_list":[{"ting_uid":"60430974","song_num":2,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/2FAFAB3D2CBFD2C96876DEE61D9AF5C2/254195147/254195147.jpg@s_0,w_120","album_num":0,"artist_desc":"","author":"<em>勇<\/em>钢","artist_source":"web","artist_id":"43697687"},{"ting_uid":"7144","song_num":20,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/B7540C7798319D894499E89B413ABEF4/252306869/252306869.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"路<em>勇<\/em>","artist_source":"web","artist_id":"1989755"},{"ting_uid":"1591","song_num":27,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/4EC863156EBB42895A7F0331D19E81FA/252057741/252057741.jpg@s_0,w_120","album_num":8,"artist_desc":"","author":"黄<em>勇<\/em>","artist_source":"web","artist_id":"1682"},{"ting_uid":"65954136","song_num":14,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/pic/86478269/86478269.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"秦<em>勇<\/em>","artist_source":"web","artist_id":"14384918"},{"ting_uid":"5055","song_num":6,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/0A87679B211FB8202275CD896E212881/252416151/252416151.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"曾<em>勇<\/em>","artist_source":"web","artist_id":"2000943"},{"ting_uid":"1303","song_num":1,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/7666DB7A3712885A5E4066632E660BCB/252053278/252053278.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"何<em>勇<\/em>","artist_source":"web","artist_id":"512"},{"ting_uid":"78005625","song_num":9,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/69E005EC156C0E80DCA322990682C51A/254355635/254355635.jpg@s_0,w_120","album_num":3,"artist_desc":"","author":"李<em>勇<\/em>","artist_source":"web","artist_id":"71849826"},{"ting_uid":"129620706","song_num":19,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/pic/117362177/117362177.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"刘<em>勇<\/em>","artist_source":"web","artist_id":"117362196"},{"ting_uid":"7367","song_num":7,"country":"中国","avatar_middle":"","album_num":3,"artist_desc":"","author":"梁<em>勇<\/em>","artist_source":"web","artist_id":"1990448"},{"ting_uid":"1935","song_num":23,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/66E7587D680BF9E4ABEC5015DAD4705C/252067466/252067466.jpg@s_0,w_120","album_num":5,"artist_desc":"","author":"王<em>勇<\/em>","artist_source":"web","artist_id":"4680"}],"total":37}
         * album_info : {"album_list":[{"all_artist_id":"19064521","publishtime":"2012-03-26","company":"","album_desc":"","title":"<em>勇<\/em>敢的家伙们","album_id":"19078721","pic_small":"http://musicdata.baidu.com/data2/pic/41321786/41321786.jpg@s_0,w_90","hot":0,"author":"<em>勇<\/em>敢的家伙们","artist_id":"19064521"},{"all_artist_id":"14437870","publishtime":"2015-09-12","company":"欧乐文化","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"247095785","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","hot":1,"author":"丁<em>勇<\/em>","artist_id":"14437870"},{"all_artist_id":"1898","publishtime":"2014-03-03","company":"世纪博英","album_desc":"","title":"<em>勇<\/em>敢向前冲","album_id":"115165779","pic_small":"http://musicdata.baidu.com/data2/pic/115165761/115165761.jpg@s_0,w_90","hot":31,"author":"顾峰","artist_id":"1898"},{"all_artist_id":"256271373","publishtime":"2015-10-22","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"256272707","pic_small":"http://musicdata.baidu.com/data2/pic/256272297/256272297.jpg@s_0,w_90","hot":0,"author":"刘东允","artist_id":"256271373"},{"all_artist_id":"265490365","publishtime":"2016-05-11","company":"希望无限","album_desc":"","title":"<em>勇<\/em>闯新境界","album_id":"265490391","pic_small":"http://musicdata.baidu.com/data2/pic/265490302/265490302.jpg@s_0,w_90","hot":0,"author":"傅超华","artist_id":"265490365"},{"all_artist_id":"59913362","publishtime":"2016-05-21","company":"金丝美文化","album_desc":"","title":"<em>勇<\/em>敢吧(708090之深圳恋歌主题曲)","album_id":"265754096","pic_small":"http://musicdata.baidu.com/data2/pic/265753874/265753874.jpg@s_0,w_90","hot":8,"author":"万里","artist_id":"59913362"},{"all_artist_id":"1562,116534293","publishtime":"2014-03-03","company":"SONY MUSIC","album_desc":"","title":"<em>勇<\/em>者的浪漫","album_id":"118658497","pic_small":"http://musicdata.baidu.com/data2/pic/118658506/118658506.jpg@s_0,w_90","hot":27,"author":"陈柏宇,VnP","artist_id":"1562"},{"all_artist_id":"242747896","publishtime":"2015-11-26","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢厦门人","album_id":"259104616","pic_small":"http://musicdata.baidu.com/data2/pic/259104017/259104017.jpg@s_0,w_90","hot":0,"author":"方贵明","artist_id":"242747896"},{"all_artist_id":"106262503","publishtime":"2014-11-06","company":"凤凰涅盘","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"124374459","pic_small":"http://musicdata.baidu.com/data2/pic/124374432/124374432.jpg@s_0,w_90","hot":3,"author":"张添艺","artist_id":"106262503"},{"all_artist_id":"14458230","publishtime":"2016-02-19","company":"华月文化","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"262702980","pic_small":"http://musicdata.baidu.com/data2/pic/262699130/262699130.jpg@s_0,w_90","hot":7,"author":"齐航","artist_id":"14458230"}],"total":164}
         * query : 勇
         * song_info : {"song_list":[{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"千嬅盛放","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"7311823","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_90","song_id":"399328","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"C1","content":"","piao_id":"0","artist_id":"44","lrclink":"http://musicdata.baidu.com/data2/lrc/270389/270389.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"7311916","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":0,"author":"张柏芝","pic_small":"http://musicdata.baidu.com/data2/pic/12f74d8f1b045a34464cde277c57e3d7/262031891/262031891.jpg@s_0,w_90","song_id":"270388","all_artist_id":"44","ting_uid":"8545"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千嬅万紫千红演唱会","content":"","piao_id":"0","artist_id":"102","lrclink":"","data_source":0,"relate_status":0,"learn":0,"album_id":"276907819","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/842afdba24eadb777b81ec1562610398/276907820/276907820.jpg@s_0,w_90","song_id":"276920078","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":1,"album_title":"广东经典101 Vol.2","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/c830d31b5e2b1bba7fa8272379527dd1/260738675/260738675.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"116464653","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"116465116","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"Y Best","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13758172/13758172.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"13757322","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"13757366","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千桦对杨千桦","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/1688095/1688095.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"328051","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/40080522/40080522.jpg@s_0,w_90","song_id":"1688094","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"THE IDOLM@STER STATION!!! SECOND TRAVEL ～Seaside Date～","content":"","piao_id":"0","artist_id":"19428108","lrclink":"http://musicdata.baidu.com/data2/lrc/6185fbd9f1098e7bfab99e573a4a73b9/277344005/277344005.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26079200","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64","title":"勇気りんりん","has_mv_mobile":0,"author":"日本ACG","pic_small":"http://musicdata.baidu.com/data2/music/671EB039806857C0C3F0C0510F5BDA84/253601662/253601662.jpg@s_0,w_90","song_id":"26079686","all_artist_id":"19428108","ting_uid":"90654830"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125553/247125553.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095786","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"伴奏","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125561/247125561.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095787","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"现场","toneid":"0","info":"","has_mv":0,"album_title":"魅力音乐会","content":"","piao_id":"0","artist_id":"385","lrclink":"http://musicdata.baidu.com/data2/lrc/202cf2b688c5cea5d0c969c8d2184e90/261926928/261926928.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26695624","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,96,128,192,256,320,flac","title":"勇往直前","has_mv_mobile":0,"author":"女子十二乐坊","pic_small":"http://musicdata.baidu.com/data2/music/E64EF61D835319E9D8A1994F512153BE/253812738/253812738.jpg@s_0,w_90","song_id":"26695912","all_artist_id":"385","ting_uid":"245608"}],"total":1197}
         * rqt_type : 1
         * syn_words :
         */

        private ArtistInfoBean artist_info;
        private AlbumInfoBean album_info;
        private String query;
        private SongInfoBean song_info;
        private int rqt_type;
        private String syn_words;

        public ArtistInfoBean getArtist_info() {
            return artist_info;
        }

        public void setArtist_info(ArtistInfoBean artist_info) {
            this.artist_info = artist_info;
        }

        public AlbumInfoBean getAlbum_info() {
            return album_info;
        }

        public void setAlbum_info(AlbumInfoBean album_info) {
            this.album_info = album_info;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public SongInfoBean getSong_info() {
            return song_info;
        }

        public void setSong_info(SongInfoBean song_info) {
            this.song_info = song_info;
        }

        public int getRqt_type() {
            return rqt_type;
        }

        public void setRqt_type(int rqt_type) {
            this.rqt_type = rqt_type;
        }

        public String getSyn_words() {
            return syn_words;
        }

        public void setSyn_words(String syn_words) {
            this.syn_words = syn_words;
        }

        public static class ArtistInfoBean {
            /**
             * artist_list : [{"ting_uid":"60430974","song_num":2,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/2FAFAB3D2CBFD2C96876DEE61D9AF5C2/254195147/254195147.jpg@s_0,w_120","album_num":0,"artist_desc":"","author":"<em>勇<\/em>钢","artist_source":"web","artist_id":"43697687"},{"ting_uid":"7144","song_num":20,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/B7540C7798319D894499E89B413ABEF4/252306869/252306869.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"路<em>勇<\/em>","artist_source":"web","artist_id":"1989755"},{"ting_uid":"1591","song_num":27,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/4EC863156EBB42895A7F0331D19E81FA/252057741/252057741.jpg@s_0,w_120","album_num":8,"artist_desc":"","author":"黄<em>勇<\/em>","artist_source":"web","artist_id":"1682"},{"ting_uid":"65954136","song_num":14,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/pic/86478269/86478269.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"秦<em>勇<\/em>","artist_source":"web","artist_id":"14384918"},{"ting_uid":"5055","song_num":6,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/0A87679B211FB8202275CD896E212881/252416151/252416151.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"曾<em>勇<\/em>","artist_source":"web","artist_id":"2000943"},{"ting_uid":"1303","song_num":1,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/7666DB7A3712885A5E4066632E660BCB/252053278/252053278.jpg@s_0,w_120","album_num":1,"artist_desc":"","author":"何<em>勇<\/em>","artist_source":"web","artist_id":"512"},{"ting_uid":"78005625","song_num":9,"country":"","avatar_middle":"http://musicdata.baidu.com/data2/music/69E005EC156C0E80DCA322990682C51A/254355635/254355635.jpg@s_0,w_120","album_num":3,"artist_desc":"","author":"李<em>勇<\/em>","artist_source":"web","artist_id":"71849826"},{"ting_uid":"129620706","song_num":19,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/pic/117362177/117362177.jpg@s_0,w_120","album_num":2,"artist_desc":"","author":"刘<em>勇<\/em>","artist_source":"web","artist_id":"117362196"},{"ting_uid":"7367","song_num":7,"country":"中国","avatar_middle":"","album_num":3,"artist_desc":"","author":"梁<em>勇<\/em>","artist_source":"web","artist_id":"1990448"},{"ting_uid":"1935","song_num":23,"country":"中国","avatar_middle":"http://musicdata.baidu.com/data2/music/66E7587D680BF9E4ABEC5015DAD4705C/252067466/252067466.jpg@s_0,w_120","album_num":5,"artist_desc":"","author":"王<em>勇<\/em>","artist_source":"web","artist_id":"4680"}]
             * total : 37
             */

            private int total;
            private List<ArtistListBean> artist_list;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<ArtistListBean> getArtist_list() {
                return artist_list;
            }

            public void setArtist_list(List<ArtistListBean> artist_list) {
                this.artist_list = artist_list;
            }

            public static class ArtistListBean {
                /**
                 * ting_uid : 60430974
                 * song_num : 2
                 * country :
                 * avatar_middle : http://musicdata.baidu.com/data2/music/2FAFAB3D2CBFD2C96876DEE61D9AF5C2/254195147/254195147.jpg@s_0,w_120
                 * album_num : 0
                 * artist_desc :
                 * author : <em>勇</em>钢
                 * artist_source : web
                 * artist_id : 43697687
                 */

                private String ting_uid;
                private int song_num;
                private String country;
                private String avatar_middle;
                private int album_num;
                private String artist_desc;
                private String author;
                private String artist_source;
                private String artist_id;

                public String getTing_uid() {
                    return ting_uid;
                }

                public void setTing_uid(String ting_uid) {
                    this.ting_uid = ting_uid;
                }

                public int getSong_num() {
                    return song_num;
                }

                public void setSong_num(int song_num) {
                    this.song_num = song_num;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getAvatar_middle() {
                    return avatar_middle;
                }

                public void setAvatar_middle(String avatar_middle) {
                    this.avatar_middle = avatar_middle;
                }

                public int getAlbum_num() {
                    return album_num;
                }

                public void setAlbum_num(int album_num) {
                    this.album_num = album_num;
                }

                public String getArtist_desc() {
                    return artist_desc;
                }

                public void setArtist_desc(String artist_desc) {
                    this.artist_desc = artist_desc;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getArtist_source() {
                    return artist_source;
                }

                public void setArtist_source(String artist_source) {
                    this.artist_source = artist_source;
                }

                public String getArtist_id() {
                    return artist_id;
                }

                public void setArtist_id(String artist_id) {
                    this.artist_id = artist_id;
                }
            }
        }

        public static class AlbumInfoBean {
            /**
             * album_list : [{"all_artist_id":"19064521","publishtime":"2012-03-26","company":"","album_desc":"","title":"<em>勇<\/em>敢的家伙们","album_id":"19078721","pic_small":"http://musicdata.baidu.com/data2/pic/41321786/41321786.jpg@s_0,w_90","hot":0,"author":"<em>勇<\/em>敢的家伙们","artist_id":"19064521"},{"all_artist_id":"14437870","publishtime":"2015-09-12","company":"欧乐文化","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"247095785","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","hot":1,"author":"丁<em>勇<\/em>","artist_id":"14437870"},{"all_artist_id":"1898","publishtime":"2014-03-03","company":"世纪博英","album_desc":"","title":"<em>勇<\/em>敢向前冲","album_id":"115165779","pic_small":"http://musicdata.baidu.com/data2/pic/115165761/115165761.jpg@s_0,w_90","hot":31,"author":"顾峰","artist_id":"1898"},{"all_artist_id":"256271373","publishtime":"2015-10-22","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢的梦","album_id":"256272707","pic_small":"http://musicdata.baidu.com/data2/pic/256272297/256272297.jpg@s_0,w_90","hot":0,"author":"刘东允","artist_id":"256271373"},{"all_artist_id":"265490365","publishtime":"2016-05-11","company":"希望无限","album_desc":"","title":"<em>勇<\/em>闯新境界","album_id":"265490391","pic_small":"http://musicdata.baidu.com/data2/pic/265490302/265490302.jpg@s_0,w_90","hot":0,"author":"傅超华","artist_id":"265490365"},{"all_artist_id":"59913362","publishtime":"2016-05-21","company":"金丝美文化","album_desc":"","title":"<em>勇<\/em>敢吧(708090之深圳恋歌主题曲)","album_id":"265754096","pic_small":"http://musicdata.baidu.com/data2/pic/265753874/265753874.jpg@s_0,w_90","hot":8,"author":"万里","artist_id":"59913362"},{"all_artist_id":"1562,116534293","publishtime":"2014-03-03","company":"SONY MUSIC","album_desc":"","title":"<em>勇<\/em>者的浪漫","album_id":"118658497","pic_small":"http://musicdata.baidu.com/data2/pic/118658506/118658506.jpg@s_0,w_90","hot":27,"author":"陈柏宇,VnP","artist_id":"1562"},{"all_artist_id":"242747896","publishtime":"2015-11-26","company":"魔音唱片","album_desc":"","title":"<em>勇<\/em>敢厦门人","album_id":"259104616","pic_small":"http://musicdata.baidu.com/data2/pic/259104017/259104017.jpg@s_0,w_90","hot":0,"author":"方贵明","artist_id":"242747896"},{"all_artist_id":"106262503","publishtime":"2014-11-06","company":"凤凰涅盘","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"124374459","pic_small":"http://musicdata.baidu.com/data2/pic/124374432/124374432.jpg@s_0,w_90","hot":3,"author":"张添艺","artist_id":"106262503"},{"all_artist_id":"14458230","publishtime":"2016-02-19","company":"华月文化","album_desc":"","title":"<em>勇<\/em>敢去闯","album_id":"262702980","pic_small":"http://musicdata.baidu.com/data2/pic/262699130/262699130.jpg@s_0,w_90","hot":7,"author":"齐航","artist_id":"14458230"}]
             * total : 164
             */

            private int total;
            private List<AlbumListBean> album_list;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<AlbumListBean> getAlbum_list() {
                return album_list;
            }

            public void setAlbum_list(List<AlbumListBean> album_list) {
                this.album_list = album_list;
            }

            public static class AlbumListBean {
                /**
                 * all_artist_id : 19064521
                 * publishtime : 2012-03-26
                 * company :
                 * album_desc :
                 * title : <em>勇</em>敢的家伙们
                 * album_id : 19078721
                 * pic_small : http://musicdata.baidu.com/data2/pic/41321786/41321786.jpg@s_0,w_90
                 * hot : 0
                 * author : <em>勇</em>敢的家伙们
                 * artist_id : 19064521
                 */

                private String all_artist_id;
                private String publishtime;
                private String company;
                private String album_desc;
                private String title;
                private String album_id;
                private String pic_small;
                private int hot;
                private String author;
                private String artist_id;

                public String getAll_artist_id() {
                    return all_artist_id;
                }

                public void setAll_artist_id(String all_artist_id) {
                    this.all_artist_id = all_artist_id;
                }

                public String getPublishtime() {
                    return publishtime;
                }

                public void setPublishtime(String publishtime) {
                    this.publishtime = publishtime;
                }

                public String getCompany() {
                    return company;
                }

                public void setCompany(String company) {
                    this.company = company;
                }

                public String getAlbum_desc() {
                    return album_desc;
                }

                public void setAlbum_desc(String album_desc) {
                    this.album_desc = album_desc;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getAlbum_id() {
                    return album_id;
                }

                public void setAlbum_id(String album_id) {
                    this.album_id = album_id;
                }

                public String getPic_small() {
                    return pic_small;
                }

                public void setPic_small(String pic_small) {
                    this.pic_small = pic_small;
                }

                public int getHot() {
                    return hot;
                }

                public void setHot(int hot) {
                    this.hot = hot;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getArtist_id() {
                    return artist_id;
                }

                public void setArtist_id(String artist_id) {
                    this.artist_id = artist_id;
                }
            }
        }

        public static class SongInfoBean {
            /**
             * song_list : [{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"千嬅盛放","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"7311823","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_90","song_id":"399328","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"C1","content":"","piao_id":"0","artist_id":"44","lrclink":"http://musicdata.baidu.com/data2/lrc/270389/270389.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"7311916","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":0,"author":"张柏芝","pic_small":"http://musicdata.baidu.com/data2/pic/12f74d8f1b045a34464cde277c57e3d7/262031891/262031891.jpg@s_0,w_90","song_id":"270388","all_artist_id":"44","ting_uid":"8545"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千嬅万紫千红演唱会","content":"","piao_id":"0","artist_id":"102","lrclink":"","data_source":0,"relate_status":0,"learn":0,"album_id":"276907819","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,128,256,320","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/842afdba24eadb777b81ec1562610398/276907820/276907820.jpg@s_0,w_90","song_id":"276920078","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":1,"album_title":"广东经典101 Vol.2","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/c830d31b5e2b1bba7fa8272379527dd1/260738675/260738675.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"116464653","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"116465116","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0100000000","del_status":"0","havehigh":2,"versions":"","toneid":"600902000005366066","info":"","has_mv":1,"album_title":"Y Best","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/13758172/13758172.lrc","data_source":0,"relate_status":0,"learn":1,"album_id":"13757322","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320","title":"勇","has_mv_mobile":1,"author":"杨千嬅","pic_small":"","song_id":"13757366","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"杨千桦对杨千桦","content":"","piao_id":"0","artist_id":"102","lrclink":"http://musicdata.baidu.com/data2/lrc/1688095/1688095.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"328051","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256","title":"勇","has_mv_mobile":0,"author":"杨千嬅","pic_small":"http://musicdata.baidu.com/data2/pic/40080522/40080522.jpg@s_0,w_90","song_id":"1688094","all_artist_id":"102","ting_uid":"1085"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":0,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"THE IDOLM@STER STATION!!! SECOND TRAVEL ～Seaside Date～","content":"","piao_id":"0","artist_id":"19428108","lrclink":"http://musicdata.baidu.com/data2/lrc/6185fbd9f1098e7bfab99e573a4a73b9/277344005/277344005.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26079200","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64","title":"勇気りんりん","has_mv_mobile":0,"author":"日本ACG","pic_small":"http://musicdata.baidu.com/data2/music/671EB039806857C0C3F0C0510F5BDA84/253601662/253601662.jpg@s_0,w_90","song_id":"26079686","all_artist_id":"19428108","ting_uid":"90654830"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125553/247125553.lrc","data_source":0,"relate_status":1,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095786","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"伴奏","toneid":"0","info":"","has_mv":0,"album_title":"勇敢的梦","content":"","piao_id":"0","artist_id":"14437870","lrclink":"http://musicdata.baidu.com/data2/lrc/247125561/247125561.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"247095785","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"24,64,128,192,256,320,flac","title":"勇敢的梦","has_mv_mobile":0,"author":"丁勇","pic_small":"http://musicdata.baidu.com/data2/pic/247095760/247095760.jpg@s_0,w_90","song_id":"247095787","all_artist_id":"14437870","ting_uid":"211210483"},{"resource_type_ext":"0","resource_type":0,"mv_provider":"0000000000","del_status":"0","havehigh":2,"versions":"现场","toneid":"0","info":"","has_mv":0,"album_title":"魅力音乐会","content":"","piao_id":"0","artist_id":"385","lrclink":"http://musicdata.baidu.com/data2/lrc/202cf2b688c5cea5d0c969c8d2184e90/261926928/261926928.lrc","data_source":0,"relate_status":0,"learn":0,"album_id":"26695624","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_source":"web","is_first_publish":0,"cluster_id":0,"charge":0,"copy_type":"1","korean_bb_song":"0","all_rate":"64,96,128,192,256,320,flac","title":"勇往直前","has_mv_mobile":0,"author":"女子十二乐坊","pic_small":"http://musicdata.baidu.com/data2/music/E64EF61D835319E9D8A1994F512153BE/253812738/253812738.jpg@s_0,w_90","song_id":"26695912","all_artist_id":"385","ting_uid":"245608"}]
             * total : 1197
             */

            private int total;
            private List<SongListBean> song_list;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongListBean> getSong_list() {
                return song_list;
            }

            public void setSong_list(List<SongListBean> song_list) {
                this.song_list = song_list;
            }

            public static class SongListBean {
                /**
                 * resource_type_ext : 0
                 * resource_type : 0
                 * mv_provider : 0100000000
                 * del_status : 0
                 * havehigh : 2
                 * versions :
                 * toneid : 600902000005366066
                 * info :
                 * has_mv : 1
                 * album_title : 千嬅盛放
                 * content :
                 * piao_id : 0
                 * artist_id : 102
                 * lrclink : http://musicdata.baidu.com/data2/lrc/13932819/13932819.lrc
                 * data_source : 0
                 * relate_status : 0
                 * learn : 1
                 * album_id : 7311823
                 * bitrate_fee : {"0":"0|0","1":"0|0"}
                 * song_source : web
                 * is_first_publish : 0
                 * cluster_id : 0
                 * charge : 0
                 * copy_type : 1
                 * korean_bb_song : 0
                 * all_rate : 64,128,256,320
                 * title : 勇
                 * has_mv_mobile : 1
                 * author : 杨千嬅
                 * pic_small : http://musicdata.baidu.com/data2/pic/89844866/89844866.jpg@s_0,w_90
                 * song_id : 399328
                 * all_artist_id : 102
                 * ting_uid : 1085
                 */

                private String resource_type_ext;
                private int resource_type;
                private String mv_provider;
                private String del_status;
                private int havehigh;
                private String versions;
                private String toneid;
                private String info;
                private int has_mv;
                private String album_title;
                private String content;
                private String piao_id;
                private String artist_id;
                private String lrclink;
                private int data_source;
                private int relate_status;
                private int learn;
                private String album_id;
                private String bitrate_fee;
                private String song_source;
                private int is_first_publish;
                private int cluster_id;
                private int charge;
                private String copy_type;
                private String korean_bb_song;
                private String all_rate;
                private String title;
                private int has_mv_mobile;
                private String author;
                private String pic_small;
                private String song_id;
                private String all_artist_id;
                private String ting_uid;

                public String getResource_type_ext() {
                    return resource_type_ext;
                }

                public void setResource_type_ext(String resource_type_ext) {
                    this.resource_type_ext = resource_type_ext;
                }

                public int getResource_type() {
                    return resource_type;
                }

                public void setResource_type(int resource_type) {
                    this.resource_type = resource_type;
                }

                public String getMv_provider() {
                    return mv_provider;
                }

                public void setMv_provider(String mv_provider) {
                    this.mv_provider = mv_provider;
                }

                public String getDel_status() {
                    return del_status;
                }

                public void setDel_status(String del_status) {
                    this.del_status = del_status;
                }

                public int getHavehigh() {
                    return havehigh;
                }

                public void setHavehigh(int havehigh) {
                    this.havehigh = havehigh;
                }

                public String getVersions() {
                    return versions;
                }

                public void setVersions(String versions) {
                    this.versions = versions;
                }

                public String getToneid() {
                    return toneid;
                }

                public void setToneid(String toneid) {
                    this.toneid = toneid;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public int getHas_mv() {
                    return has_mv;
                }

                public void setHas_mv(int has_mv) {
                    this.has_mv = has_mv;
                }

                public String getAlbum_title() {
                    return album_title;
                }

                public void setAlbum_title(String album_title) {
                    this.album_title = album_title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getPiao_id() {
                    return piao_id;
                }

                public void setPiao_id(String piao_id) {
                    this.piao_id = piao_id;
                }

                public String getArtist_id() {
                    return artist_id;
                }

                public void setArtist_id(String artist_id) {
                    this.artist_id = artist_id;
                }

                public String getLrclink() {
                    return lrclink;
                }

                public void setLrclink(String lrclink) {
                    this.lrclink = lrclink;
                }

                public int getData_source() {
                    return data_source;
                }

                public void setData_source(int data_source) {
                    this.data_source = data_source;
                }

                public int getRelate_status() {
                    return relate_status;
                }

                public void setRelate_status(int relate_status) {
                    this.relate_status = relate_status;
                }

                public int getLearn() {
                    return learn;
                }

                public void setLearn(int learn) {
                    this.learn = learn;
                }

                public String getAlbum_id() {
                    return album_id;
                }

                public void setAlbum_id(String album_id) {
                    this.album_id = album_id;
                }

                public String getBitrate_fee() {
                    return bitrate_fee;
                }

                public void setBitrate_fee(String bitrate_fee) {
                    this.bitrate_fee = bitrate_fee;
                }

                public String getSong_source() {
                    return song_source;
                }

                public void setSong_source(String song_source) {
                    this.song_source = song_source;
                }

                public int getIs_first_publish() {
                    return is_first_publish;
                }

                public void setIs_first_publish(int is_first_publish) {
                    this.is_first_publish = is_first_publish;
                }

                public int getCluster_id() {
                    return cluster_id;
                }

                public void setCluster_id(int cluster_id) {
                    this.cluster_id = cluster_id;
                }

                public int getCharge() {
                    return charge;
                }

                public void setCharge(int charge) {
                    this.charge = charge;
                }

                public String getCopy_type() {
                    return copy_type;
                }

                public void setCopy_type(String copy_type) {
                    this.copy_type = copy_type;
                }

                public String getKorean_bb_song() {
                    return korean_bb_song;
                }

                public void setKorean_bb_song(String korean_bb_song) {
                    this.korean_bb_song = korean_bb_song;
                }

                public String getAll_rate() {
                    return all_rate;
                }

                public void setAll_rate(String all_rate) {
                    this.all_rate = all_rate;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getHas_mv_mobile() {
                    return has_mv_mobile;
                }

                public void setHas_mv_mobile(int has_mv_mobile) {
                    this.has_mv_mobile = has_mv_mobile;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getPic_small() {
                    return pic_small;
                }

                public void setPic_small(String pic_small) {
                    this.pic_small = pic_small;
                }

                public String getSong_id() {
                    return song_id;
                }

                public void setSong_id(String song_id) {
                    this.song_id = song_id;
                }

                public String getAll_artist_id() {
                    return all_artist_id;
                }

                public void setAll_artist_id(String all_artist_id) {
                    this.all_artist_id = all_artist_id;
                }

                public String getTing_uid() {
                    return ting_uid;
                }

                public void setTing_uid(String ting_uid) {
                    this.ting_uid = ting_uid;
                }
            }
        }
    }
}

package com.example.administrator.audioplayer.jsonbean;

import java.util.List;

/**
 * Created by on 2017/2/17 0017.
 * 新专辑推荐
 * http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.plaza.getRecommendAlbum&offset=0&limit=7
 */

public class RecommendNewAlbum {

    /**
     * error_code : 22000
     * plaze_album_list : {"RM":{"album_list":{"list":[{"album_id":"533429003","title":"有佳人兮","publishcompany":"世纪丰羽","country":"内地","songs_total":"2","pic_small":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_300","artist_id":"1087","all_artist_id":"1087","author":"哈辉","publishtime":"2017-02-17","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"1"},{"album_id":"533400364","title":"前方的世界","publishcompany":"乐华圆娱","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_300","artist_id":"315817924","all_artist_id":"315817924","author":"YHBOYS","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533402092","title":"夙念","publishcompany":"谭璇工作室","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_300","artist_id":"1656","all_artist_id":"1656","author":"郁可唯","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315749329","title":"美女与野兽","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_300","artist_id":"2006745","all_artist_id":"2006745,10","author":"田馥甄,井柏然","publishtime":"2017-02-13","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533391615","title":"T榜力量9","publishcompany":"华宇世博","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_300","artist_id":"266602769","all_artist_id":"266602769","author":"T榜","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315796013","title":"Cold","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_300","artist_id":"807","all_artist_id":"807,10562437","author":"Maroon 5,Future","publishtime":"2017-02-14","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533370111","title":"爱又爱","publishcompany":"海蝶音乐","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_300","artist_id":"903","all_artist_id":"903","author":"By2","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"}],"havemore":1}}}
     */

    private int error_code;
    private PlazeAlbumListBean plaze_album_list;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public PlazeAlbumListBean getPlaze_album_list() {
        return plaze_album_list;
    }

    public void setPlaze_album_list(PlazeAlbumListBean plaze_album_list) {
        this.plaze_album_list = plaze_album_list;
    }

    public static class PlazeAlbumListBean {
        /**
         * RM : {"album_list":{"list":[{"album_id":"533429003","title":"有佳人兮","publishcompany":"世纪丰羽","country":"内地","songs_total":"2","pic_small":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_300","artist_id":"1087","all_artist_id":"1087","author":"哈辉","publishtime":"2017-02-17","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"1"},{"album_id":"533400364","title":"前方的世界","publishcompany":"乐华圆娱","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_300","artist_id":"315817924","all_artist_id":"315817924","author":"YHBOYS","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533402092","title":"夙念","publishcompany":"谭璇工作室","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_300","artist_id":"1656","all_artist_id":"1656","author":"郁可唯","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315749329","title":"美女与野兽","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_300","artist_id":"2006745","all_artist_id":"2006745,10","author":"田馥甄,井柏然","publishtime":"2017-02-13","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533391615","title":"T榜力量9","publishcompany":"华宇世博","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_300","artist_id":"266602769","all_artist_id":"266602769","author":"T榜","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315796013","title":"Cold","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_300","artist_id":"807","all_artist_id":"807,10562437","author":"Maroon 5,Future","publishtime":"2017-02-14","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533370111","title":"爱又爱","publishcompany":"海蝶音乐","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_300","artist_id":"903","all_artist_id":"903","author":"By2","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"}],"havemore":1}}
         */

        private RMBean RM;

        public RMBean getRM() {
            return RM;
        }

        public void setRM(RMBean RM) {
            this.RM = RM;
        }

        public static class RMBean {
            /**
             * album_list : {"list":[{"album_id":"533429003","title":"有佳人兮","publishcompany":"世纪丰羽","country":"内地","songs_total":"2","pic_small":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_300","artist_id":"1087","all_artist_id":"1087","author":"哈辉","publishtime":"2017-02-17","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"1"},{"album_id":"533400364","title":"前方的世界","publishcompany":"乐华圆娱","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_300","artist_id":"315817924","all_artist_id":"315817924","author":"YHBOYS","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533402092","title":"夙念","publishcompany":"谭璇工作室","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_300","artist_id":"1656","all_artist_id":"1656","author":"郁可唯","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315749329","title":"美女与野兽","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_300","artist_id":"2006745","all_artist_id":"2006745,10","author":"田馥甄,井柏然","publishtime":"2017-02-13","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533391615","title":"T榜力量9","publishcompany":"华宇世博","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_300","artist_id":"266602769","all_artist_id":"266602769","author":"T榜","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315796013","title":"Cold","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_300","artist_id":"807","all_artist_id":"807,10562437","author":"Maroon 5,Future","publishtime":"2017-02-14","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533370111","title":"爱又爱","publishcompany":"海蝶音乐","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_300","artist_id":"903","all_artist_id":"903","author":"By2","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"}],"havemore":1}
             */

            private AlbumListBean album_list;

            public AlbumListBean getAlbum_list() {
                return album_list;
            }

            public void setAlbum_list(AlbumListBean album_list) {
                this.album_list = album_list;
            }

            public static class AlbumListBean {
                /**
                 * list : [{"album_id":"533429003","title":"有佳人兮","publishcompany":"世纪丰羽","country":"内地","songs_total":"2","pic_small":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_300","artist_id":"1087","all_artist_id":"1087","author":"哈辉","publishtime":"2017-02-17","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"1"},{"album_id":"533400364","title":"前方的世界","publishcompany":"乐华圆娱","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/874808ffae6f8d15d989804186c20506/533400353/533400353.jpg@s_0,w_300","artist_id":"315817924","all_artist_id":"315817924","author":"YHBOYS","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533402092","title":"夙念","publishcompany":"谭璇工作室","country":"内地","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/5a8495f9e6c8fcf460da81c8abaacd72/533402084/533402084.jpg@s_0,w_300","artist_id":"1656","all_artist_id":"1656","author":"郁可唯","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315749329","title":"美女与野兽","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/6565856ec3a56290d0e44259d872ab09/315749330/315749330.jpg@s_0,w_300","artist_id":"2006745","all_artist_id":"2006745,10","author":"田馥甄,井柏然","publishtime":"2017-02-13","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533391615","title":"T榜力量9","publishcompany":"华宇世博","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/8290b7a082fe51a819b78000e934784e/533391610/533391610.jpg@s_0,w_300","artist_id":"266602769","all_artist_id":"266602769","author":"T榜","publishtime":"2017-02-16","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"315796013","title":"Cold","publishcompany":"环球唱片","country":"其他","songs_total":"1","pic_small":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/f23e2b988a2da3e93f72f7dbec68d056/315796014/315796014.jpg@s_0,w_300","artist_id":"807","all_artist_id":"807,10562437","author":"Maroon 5,Future","publishtime":"2017-02-14","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"},{"album_id":"533370111","title":"爱又爱","publishcompany":"海蝶音乐","country":"内地","songs_total":"0","pic_small":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_90","pic_big":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_150","pic_radio":"http://qukufile2.qianqian.com/data2/pic/50a933a5280f82b11c295876664ca4a6/533370093/533370093.jpg@s_0,w_300","artist_id":"903","all_artist_id":"903","author":"By2","publishtime":"2017-02-15","resource_type_ext":"0","price":"0.00","is_recommend_mis":"0","is_first_publish":"0","is_exclusive":"0"}]
                 * havemore : 1
                 */

                private int havemore;
                private List<ListBean> list;

                public int getHavemore() {
                    return havemore;
                }

                public void setHavemore(int havemore) {
                    this.havemore = havemore;
                }

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public static class ListBean {
                    /**
                     * album_id : 533429003
                     * title : 有佳人兮
                     * publishcompany : 世纪丰羽
                     * country : 内地
                     * songs_total : 2
                     * pic_small : http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_90
                     * pic_big : http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_150
                     * pic_radio : http://qukufile2.qianqian.com/data2/pic/cad226a056c6e5fccf6d8cee98af1512/533428994/533428994.jpg@s_0,w_300
                     * artist_id : 1087
                     * all_artist_id : 1087
                     * author : 哈辉
                     * publishtime : 2017-02-17
                     * resource_type_ext : 0
                     * price : 0.00
                     * is_recommend_mis : 0
                     * is_first_publish : 0
                     * is_exclusive : 1
                     */

                    private String album_id;
                    private String title;
                    private String publishcompany;
                    private String country;
                    private String songs_total;
                    private String pic_small;
                    private String pic_big;
                    private String pic_radio;
                    private String artist_id;
                    private String all_artist_id;
                    private String author;
                    private String publishtime;
                    private String resource_type_ext;
                    private String price;
                    private String is_recommend_mis;
                    private String is_first_publish;
                    private String is_exclusive;

                    public String getAlbum_id() {
                        return album_id;
                    }

                    public void setAlbum_id(String album_id) {
                        this.album_id = album_id;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getPublishcompany() {
                        return publishcompany;
                    }

                    public void setPublishcompany(String publishcompany) {
                        this.publishcompany = publishcompany;
                    }

                    public String getCountry() {
                        return country;
                    }

                    public void setCountry(String country) {
                        this.country = country;
                    }

                    public String getSongs_total() {
                        return songs_total;
                    }

                    public void setSongs_total(String songs_total) {
                        this.songs_total = songs_total;
                    }

                    public String getPic_small() {
                        return pic_small;
                    }

                    public void setPic_small(String pic_small) {
                        this.pic_small = pic_small;
                    }

                    public String getPic_big() {
                        return pic_big;
                    }

                    public void setPic_big(String pic_big) {
                        this.pic_big = pic_big;
                    }

                    public String getPic_radio() {
                        return pic_radio;
                    }

                    public void setPic_radio(String pic_radio) {
                        this.pic_radio = pic_radio;
                    }

                    public String getArtist_id() {
                        return artist_id;
                    }

                    public void setArtist_id(String artist_id) {
                        this.artist_id = artist_id;
                    }

                    public String getAll_artist_id() {
                        return all_artist_id;
                    }

                    public void setAll_artist_id(String all_artist_id) {
                        this.all_artist_id = all_artist_id;
                    }

                    public String getAuthor() {
                        return author;
                    }

                    public void setAuthor(String author) {
                        this.author = author;
                    }

                    public String getPublishtime() {
                        return publishtime;
                    }

                    public void setPublishtime(String publishtime) {
                        this.publishtime = publishtime;
                    }

                    public String getResource_type_ext() {
                        return resource_type_ext;
                    }

                    public void setResource_type_ext(String resource_type_ext) {
                        this.resource_type_ext = resource_type_ext;
                    }

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }

                    public String getIs_recommend_mis() {
                        return is_recommend_mis;
                    }

                    public void setIs_recommend_mis(String is_recommend_mis) {
                        this.is_recommend_mis = is_recommend_mis;
                    }

                    public String getIs_first_publish() {
                        return is_first_publish;
                    }

                    public void setIs_first_publish(String is_first_publish) {
                        this.is_first_publish = is_first_publish;
                    }

                    public String getIs_exclusive() {
                        return is_exclusive;
                    }

                    public void setIs_exclusive(String is_exclusive) {
                        this.is_exclusive = is_exclusive;
                    }
                }
            }
        }
    }
}

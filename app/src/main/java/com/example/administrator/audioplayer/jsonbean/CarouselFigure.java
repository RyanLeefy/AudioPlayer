package com.example.administrator.audioplayer.jsonbean;

import java.util.List;

/**
 * Created by on 2017/2/16 0016.
 * 轮播图片
 * http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.plaza.getFocusPic&num=6
 */

public class CarouselFigure {

    /**
     * pic : [{"type":6,"mo_type":4,"code":"http://music.baidu.com/cms/webview/topic_activity/nmepre/","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14871651726b2764cbbbe4284ee810079e5b395917.jpg","randpic_ios5":"","randpic_desc":"百度音乐独家策划","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_148716512430c946bf0e0ce802b53b0e3c71dd11a9","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_148716512430c946bf0e0ce802b53b0e3c71dd11a9.jpg","special_type":0,"ipad_desc":"百度音乐独家策划","is_publish":"111111"},{"type":6,"mo_type":4,"code":"http://music.baidu.com/cms/webview/bigwig/lina/index.html","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_148716121548117ff81ff504514f99a98824e064c9.jpg","randpic_ios5":"","randpic_desc":"厉娜生日粉丝征集","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_1487161219baaa489d73ee3a319f7ba30e13f192ea","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487161219baaa489d73ee3a319f7ba30e13f192ea.jpg","special_type":0,"ipad_desc":"","is_publish":"111111"},{"type":6,"mo_type":4,"code":"http://music.baidu.com/cms/webview/topic_activity/youdai3/","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14871492610b7ca06dac293584058326bb7cb33e9e.jpg","randpic_ios5":"","randpic_desc":"第59届格莱美最佳爵士演唱专辑","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_1487149264f15c6de75c52c25bf3eb2e63726a3738","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487149264f15c6de75c52c25bf3eb2e63726a3738.jpg","special_type":0,"ipad_desc":"","is_publish":"111111"},{"type":2,"mo_type":2,"code":"533370111","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_148713282565e44202b9c610d3fcb68bd64723d0e9.jpg","randpic_ios5":"","randpic_desc":"爱又爱","randpic_ipad":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487132857cae167694bc840eeceb4f27f897b6dc5.jpg","randpic_qq":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487132871847def0df86d1346266eb10ecb0a67f9.jpg","randpic_2":"bos_client_14871328285c85371e0e42c1256112126096033c17","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14871328285c85371e0e42c1256112126096033c17.jpg","special_type":0,"ipad_desc":"爱又爱","is_publish":"111111"},{"type":6,"mo_type":4,"code":"http://music.baidu.com/cms/webview/topic_activity/mxsfgqjj/","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487105444c16bebe159e2c5ddc3810b0fa358a836.jpg","randpic_ios5":"","randpic_desc":"《明星私房歌》之秦俊杰陪你听歌聊故事","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_14871054503f4ffbbf8943d6608049a6791541155e","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14871054503f4ffbbf8943d6608049a6791541155e.jpg","special_type":0,"ipad_desc":"","is_publish":"111111"},{"type":6,"mo_type":4,"code":"http://music.baidu.com/cms/webview/topic_activity/MUSICHOT66/","randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487061134a9df656c2e4671fb7dda1ec89f6b6391.jpg","randpic_ios5":"","randpic_desc":"百度音乐独家策划","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_1487061139b8c5668e146d5c83e317c63a7845d711","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1487061139b8c5668e146d5c83e317c63a7845d711.jpg","special_type":0,"ipad_desc":"百度音乐独家策划","is_publish":"111111"}]
     * error_code : 22000
     */

    private int error_code;
    private List<PicBean> pic;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public static class PicBean {
        /**
         * type : 6
         * mo_type : 4
         * code : http://music.baidu.com/cms/webview/topic_activity/nmepre/
         * randpic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_14871651726b2764cbbbe4284ee810079e5b395917.jpg
         * randpic_ios5 :
         * randpic_desc : 百度音乐独家策划
         * randpic_ipad :
         * randpic_qq :
         * randpic_2 : bos_client_148716512430c946bf0e0ce802b53b0e3c71dd11a9
         * randpic_iphone6 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_148716512430c946bf0e0ce802b53b0e3c71dd11a9.jpg
         * special_type : 0
         * ipad_desc : 百度音乐独家策划
         * is_publish : 111111
         */

        private int type;
        private int mo_type;
        private String code;
        private String randpic;
        private String randpic_ios5;
        private String randpic_desc;
        private String randpic_ipad;
        private String randpic_qq;
        private String randpic_2;
        private String randpic_iphone6;
        private int special_type;
        private String ipad_desc;
        private String is_publish;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMo_type() {
            return mo_type;
        }

        public void setMo_type(int mo_type) {
            this.mo_type = mo_type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getRandpic() {
            return randpic;
        }

        public void setRandpic(String randpic) {
            this.randpic = randpic;
        }

        public String getRandpic_ios5() {
            return randpic_ios5;
        }

        public void setRandpic_ios5(String randpic_ios5) {
            this.randpic_ios5 = randpic_ios5;
        }

        public String getRandpic_desc() {
            return randpic_desc;
        }

        public void setRandpic_desc(String randpic_desc) {
            this.randpic_desc = randpic_desc;
        }

        public String getRandpic_ipad() {
            return randpic_ipad;
        }

        public void setRandpic_ipad(String randpic_ipad) {
            this.randpic_ipad = randpic_ipad;
        }

        public String getRandpic_qq() {
            return randpic_qq;
        }

        public void setRandpic_qq(String randpic_qq) {
            this.randpic_qq = randpic_qq;
        }

        public String getRandpic_2() {
            return randpic_2;
        }

        public void setRandpic_2(String randpic_2) {
            this.randpic_2 = randpic_2;
        }

        public String getRandpic_iphone6() {
            return randpic_iphone6;
        }

        public void setRandpic_iphone6(String randpic_iphone6) {
            this.randpic_iphone6 = randpic_iphone6;
        }

        public int getSpecial_type() {
            return special_type;
        }

        public void setSpecial_type(int special_type) {
            this.special_type = special_type;
        }

        public String getIpad_desc() {
            return ipad_desc;
        }

        public void setIpad_desc(String ipad_desc) {
            this.ipad_desc = ipad_desc;
        }

        public String getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(String is_publish) {
            this.is_publish = is_publish;
        }
    }
}

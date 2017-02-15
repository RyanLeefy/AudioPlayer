package com.example.administrator.audioplayer.jsonbean;

import java.util.List;

/**
 * Created by on 2017/2/15.
 */

public class HotWord {

    /**
     * error_code : 22000
     * result : [{"strong":1,"word":"成都","linktype":0,"linkurl":""},{"strong":0,"word":"孤芳不自赏","linktype":0,"linkurl":""},{"strong":0,"word":"薛之谦","linktype":0,"linkurl":""},{"strong":0,"word":"张杰","linktype":0,"linkurl":""},{"strong":0,"word":"周杰伦","linktype":0,"linkurl":""},{"strong":0,"word":"儿歌","linktype":0,"linkurl":""},{"strong":0,"word":"刚好遇见你","linktype":0,"linkurl":""},{"strong":0,"word":"张信哲《夏夜星空海》","linktype":0,"linkurl":""},{"strong":0,"word":"古巨基《最爱》","linktype":0,"linkurl":""},{"strong":0,"word":"虫儿飞","linktype":0,"linkurl":""},{"strong":0,"word":"TFBOYS","linktype":0,"linkurl":""},{"strong":0,"word":"轻音乐","linktype":0,"linkurl":""},{"strong":0,"word":"告白气球","linktype":0,"linkurl":""},{"strong":0,"word":"铃声","linktype":0,"linkurl":""},{"strong":0,"word":"演员","linktype":0,"linkurl":""}]
     */

    private int error_code;
    private List<ResultBean> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * strong : 1
         * word : 成都
         * linktype : 0
         * linkurl :
         */

        private int strong;
        private String word;
        private int linktype;
        private String linkurl;

        public int getStrong() {
            return strong;
        }

        public void setStrong(int strong) {
            this.strong = strong;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getLinktype() {
            return linktype;
        }

        public void setLinktype(int linktype) {
            this.linktype = linktype;
        }

        public String getLinkurl() {
            return linkurl;
        }

        public void setLinkurl(String linkurl) {
            this.linkurl = linkurl;
        }
    }
}

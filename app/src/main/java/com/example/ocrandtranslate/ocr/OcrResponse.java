package com.example.ocrandtranslate.ocr;

import java.util.List;

public class OcrResponse {


    /**
     * log_id : 4298760294740728722
     * direction : 0
     * words_result_num : 2
     * words_result : [{"words":"下面就来调酱汁:2勺蚝油,1勺生抽,1勺老抽,1勺白糖,加少许清水揣拌成酱汁。因为生抽、老"},{"words":"抽和蚝油里面都有盐分,所以一会就可以不用加盐了,别吃锝太咸"}]
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : 下面就来调酱汁:2勺蚝油,1勺生抽,1勺老抽,1勺白糖,加少许清水揣拌成酱汁。因为生抽、老
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}

package com.example.ocrandtranslate.ocr;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryResponse {

    @Id(autoincrement = true)
    private Long id;

    private String imagePath;

    private String text;

    @Generated(hash = 1411624089)
    public HistoryResponse(Long id, String imagePath, String text) {
        this.id = id;
        this.imagePath = imagePath;
        this.text = text;
    }

    @Generated(hash = 94967540)
    public HistoryResponse() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

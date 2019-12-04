package com.zxc.walk.entity;

import java.io.Serializable;

public class BannerModel implements Serializable {
    private int image;
    private int type;

    public BannerModel(int image, int type) {
        this.image = image;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

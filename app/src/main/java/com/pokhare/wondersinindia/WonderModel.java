package com.pokhare.wondersinindia;

import java.util.UUID;

public class WonderModel {
    String id;
    String cardName;
    String imageResourceUrl;
    String imageResourceId;
    int isfav;
    int isturned;
    byte[] imageArray;

    public WonderModel() {
        this.id = UUID.randomUUID().toString();
    }

    public WonderModel(String name, String url, String imageId, int isFav, int isTurned) {
        this.id = UUID.randomUUID().toString();
        this.cardName = name;
        this.imageResourceUrl = url;
        this.imageResourceId = imageId;
        this.isfav = isFav;
        this.isturned = isTurned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsturned() {
        return isturned;
    }

    public void setIsturned(int isturned) {
        this.isturned = isturned;
    }

    public int getIsfav() {
        return isfav;
    }

    public void setIsfav(int isfav) {
        this.isfav = isfav;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getImageResourceUrl() {
        return imageResourceUrl;
    }

    public void setImageResourceUrl(String url) {
        this.imageResourceUrl = url;
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }
}
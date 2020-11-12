package com.example.autorequester;

/*
外出事项
 */
public class RequestInfo {
    private String place;
    private String matter;
    private int imageId;

    public RequestInfo(String place, String matter, int imageId) {
        this.place = place;
        this.matter = matter;
        this.imageId = imageId;
    }

    public String getPlace() {
        return place;
    }

    public String getMatter() {
        return matter;
    }

    public int getImageId() {
        return imageId;
    }
}

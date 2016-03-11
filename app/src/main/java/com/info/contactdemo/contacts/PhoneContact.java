package com.info.contactdemo.contacts;

import android.graphics.Bitmap;

/**
 * Created by tejalpar on 2/15/16.
 */
public class PhoneContact {
    private String id;
    private String name;
    private String phoneNumber;
    private String nameInitial;
    private Bitmap photoThumbnail;
    private int defaultPhotoBgColor = -1;


    //id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Phone
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //Name initials
    public String getNameInitial() {
        return nameInitial;
    }

    public void setNameInitial(String nameInitial) {
        this.nameInitial = nameInitial;
    }

    //Photo
    public Bitmap getPhotoThumbnail() {
        return photoThumbnail;
    }
    public void setPhotoThumbnail(Bitmap photoThumbnail) {
        this.photoThumbnail = photoThumbnail;
    }

    //ID
    public int getDefaultPhotoBgColor() {
        return defaultPhotoBgColor;
    }
    public void setDefaultPhotoBgColor(int photoBgColor) {
        this.defaultPhotoBgColor = photoBgColor;
    }

    //Helper methods
    public boolean hasThumbnailPhoto() {
        if(photoThumbnail != null)
            return true;
        return false;
    }
}

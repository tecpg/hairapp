package com.hairstyle.hairstyleapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PASCHAL GREEN on 11/15/2016.
 */
public class DataObject {
    @SerializedName("name")
    private String name;
    public DataObject(){}
    public DataObject(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

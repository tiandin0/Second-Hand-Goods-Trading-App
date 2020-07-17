package com.example.backend.Activity;
public class Tag {
    private String tagName;
    private int imageId;
    public Tag(String tagName, int imageId){
        this.tagName = tagName;
        this.imageId = imageId;
    }
    public String getName() {
        return tagName;
    }
    public int getImageId() {
        return imageId;
    }
}

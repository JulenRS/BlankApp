package com.jross.blankapp.domains;

public class Post {

    private String title;
    private String picUrl;

    public Post(){}

    public Post(String title, String picUrl){
         this.title = title;
         this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl(){return picUrl;}

    public void setPicUrl(String picUrl){this.picUrl = picUrl;}
}

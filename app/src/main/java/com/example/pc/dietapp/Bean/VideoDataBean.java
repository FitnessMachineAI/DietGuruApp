package com.example.pc.dietapp.Bean;

import java.io.Serializable;

/**
 * Created by jikur on 2017-07-27.
 */

public class VideoDataBean implements Serializable{

    public int photo; //영상 시작 화면
    public String title;
    public String part;
    public String videoUrl;


    public VideoDataBean(int photo, String title, String part)
    {
        this.photo=photo;
        this.title=title;
        this.part=part;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}

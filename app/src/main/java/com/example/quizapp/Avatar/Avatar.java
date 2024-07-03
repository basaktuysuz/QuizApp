package com.example.quizapp.Avatar;

public class Avatar {
    private String imageUrl;

    public Avatar() {
        // Default constructor
    }

    public Avatar(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

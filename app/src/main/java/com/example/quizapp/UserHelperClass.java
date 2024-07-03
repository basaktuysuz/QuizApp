package com.example.quizapp;

public class UserHelperClass {
    //input olarak girilen verileri user ınfosu olarak kullanmak için bu class
    //to create users we use userhelperclass
    String name,username,email,password ;
    int highestScore;

    public UserHelperClass(String name, String username, String email, String password,int highestScore) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.highestScore = highestScore;
    }

    public UserHelperClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

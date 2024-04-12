package org.project.reddit.content;

import org.project.reddit.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment implements Serializable {
    private final Post post;
    private final User user;
    private final String createDateTime;
    private String text;
    private int karma;

    public Comment(String text, Post post, User user) {
        this.text = text;
        this.post = post;
        this.user = user;
        this.karma = 0;
        this.createDateTime = formatDateTime(LocalDateTime.now());
    }

    // format date and time of creating subreddit and return a string
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

    // getter methods
    public Post getPost() {
        return this.post;
    }

    public User getUser() {
        return this.user;
    }

    public String getCreateDateTime() {
        return this.createDateTime;
    }

    public String getText() {
        return this.text;
    }

    public int getKarma() {
        return this.karma;
    }

    // edit text of the comment
    public void changeText(String newText) {
        this.text = newText;
        System.out.println("> comment's text was changed");
    }

    // increase or decrease the karma of comment
    public void increaseKarma() {
        this.karma++;
        System.out.println("> comment's karma was increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> comment's karma was decreased");
    }
}

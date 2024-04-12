package org.project.reddit.content;

import org.project.reddit.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment implements Serializable {
    private static int commentCount = 0;
    private final Post post;
    private final User user;
    private final String createDateTime;
    private String text;
    private int karma;

    public Comment(String text, Post post, User user) {
        commentCount++;
        this.text = text;
        this.post = post;
        this.user = user;
        this.karma = 0;
        this.createDateTime = formatDateTime(LocalDateTime.now());
    }

    public static int getCommentCount() {
        System.out.println("> comment count refreshed");
        return commentCount;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

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

    public void changeText(String newText) {
        this.text = newText;
        System.out.println("> comment's text was changed");
    }

    public void increaseKarma() {
        this.karma++;
        System.out.println("> comment's karma was increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> comment's karma was decreased");
    }
}

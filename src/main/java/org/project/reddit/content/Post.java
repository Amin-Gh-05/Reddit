package org.project.reddit.content;

import org.project.reddit.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Post implements Serializable {
    // static list to store posts and their count
    public static List<Post> postList = new ArrayList<>();
    public static int postCount = 0;

    private final List<Comment> commentList = new ArrayList<>();
    private final List<String> tagList = new ArrayList<>();
    private final SubReddit subReddit;
    private final User user;
    private final String createDateTime;
    private final String title;
    private String text;
    private int karma;

    // tag-free version constructor
    public Post(String title, String text, SubReddit subReddit, User user) {
        this.title = title;
        this.text = text;
        this.subReddit = subReddit;
        this.user = user;
        this.karma = 0;
        postList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
        postCount = postList.size();
    }

    // tag version constructor
    public Post(List<String> tagList, String title, String text, SubReddit subReddit, User user) {
        this.tagList.addAll(tagList);
        this.title = title;
        this.text = text;
        this.subReddit = subReddit;
        this.user = user;
        this.karma = 0;
        postList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
        postCount = postList.size();
    }

    // get an array of posts with best karma
    public static String[] getTrendingPosts() {
        // sort posts considering karma
        postList.sort(Comparator.comparingInt(Post::getKarma).reversed());
        int size = postList.size();
        String[] trendingPostTitle = new String[size];
        // return the top 10
        if (size >= 10) {
            for (int i = 0; i < 5; i++) {
                trendingPostTitle[i] = postList.get(i).title;
            }
        } else {
            for (int i = 0; i < size; i++) {
                trendingPostTitle[i] = postList.get(i).title;
            }
        }
        return trendingPostTitle;
    }

    // format date and time of creating subreddit and return a string
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

    // getter methods
    public List<Comment> getCommentList() {
        return new ArrayList<>(commentList);
    }

    public List<String> getTagList() {
        return new ArrayList<>(tagList);
    }

    public SubReddit getSubReddit() {
        return subReddit;
    }

    public User getUser() {
        return user;
    }

    public String getCreateDateTime() {
        return this.createDateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getKarma() {
        return this.karma;
    }

    // add a new comment to the post
    public void addComment(Comment comment) {
        this.commentList.add(comment);
        System.out.println("> comment was added");
    }

    // remove a comment from the post
    public void removeComment(Comment comment) {
        this.commentList.remove(comment);
        System.out.println("> comment was removed");
    }

    // increase or decrease the karma of post
    public void increaseKarma() {
        this.karma++;
        System.out.println("> post's karma was increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> post's karma was decreased");
    }

    // edit text of post
    public void changeText(String newText) {
        this.text = newText;
        System.out.println("> text was changed");
    }
}

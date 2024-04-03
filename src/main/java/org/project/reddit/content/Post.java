package org.project.reddit.content;

import org.project.reddit.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Post {
    private static final List<Post> postList = new ArrayList<>();
    private static int postCount = 0;
    private final List<Comment> commentList = new ArrayList<>();
    private final List<String> tagList = new ArrayList<>();
    private final SubReddit subReddit;
    private final User user;
    private final String createDateTime;
    private String title;
    private String text;
    private int karma;

    public Post(String title, String text, SubReddit subReddit, User user) {
        postCount++;
        this.title = title;
        this.text = text;
        this.subReddit = subReddit;
        this.user = user;
        this.karma = 0;
        postList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
    }

    public Post(List<String> tagList, String title, String text, SubReddit subReddit, User user) {
        postCount++;
        this.tagList.addAll(tagList);
        this.title = title;
        this.text = text;
        this.subReddit = subReddit;
        this.user = user;
        this.karma = 0;
        postList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
    }

    public static int getPostCount() {
        System.out.println("> post count refreshed");
        return postCount;
    }

    public static String[] getTrendingPosts() {
        postList.sort(Comparator.comparingInt(Post::getKarma).reversed());
        int size = postList.size();
        String[] trendingPostTitle = new String[size];
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

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

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

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        System.out.println("> comment was added");
    }

    public void removeComment(Comment comment) {
        this.commentList.remove(comment);
        System.out.println("> comment was removed");
    }

    public void increaseKarma() {
        this.karma++;
        System.out.println("> post's karma was increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> post's karma was decreased");
    }

    public void changeTitle(String newTitle) {
        this.title = newTitle;
        System.out.println("> title was changed");
    }

    public void changeText(String newText) {
        this.text = newText;
        System.out.println("> text was changed");
    }
}

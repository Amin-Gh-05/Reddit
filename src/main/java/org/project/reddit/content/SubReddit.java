package org.project.reddit.content;

import org.project.reddit.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubReddit {
    private static final List<SubReddit> subRedditList = new ArrayList<>();
    private static int subRedditCount = 0;
    private final List<User> memberList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private final List<User> adminList = new ArrayList<>();
    private final String topic;
    private final String createDateTime;
    private int memberCount;

    public SubReddit(String topic, User admin) {
        subRedditCount++;
        this.topic = topic;
        this.adminList.add(admin);
        this.memberCount = 0;
        subRedditList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
    }

    public static List<SubReddit> getSubRedditList() {
        return new ArrayList<>(subRedditList);
    }

    public static int getSubRedditCount() {
        System.out.println("> subreddit count refreshed");
        return subRedditCount;
    }

    public static SubReddit findSubReddit(String topic) {
        for (SubReddit subReddit : subRedditList) {
            if (subReddit.topic.equals(topic)) {
                System.out.println("> subreddit found");
                return subReddit;
            }
        }
        System.out.println("> subreddit not found");
        return null;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

    public List<User> getMemberList() {
        return new ArrayList<>(this.memberList);
    }

    public List<Post> getPostList() {
        return new ArrayList<>(this.postList);
    }

    public List<User> getAdminList() {
        return new ArrayList<>(this.adminList);
    }

    public String getTopic() {
        return this.topic;
    }

    public String getCreateDateTime() {
        return this.createDateTime;
    }

    public int getMemberCount() {
        return this.memberCount;
    }

    public void addMember(User user) {
        this.memberList.add(user);
        this.memberCount++;
        System.out.println("> member was added");
    }

    public void removeMember(User user) {
        this.memberList.remove(user);
        this.adminList.remove(user);
        this.memberCount--;
        System.out.println("> member was removed");
    }

    public void addPost(Post post) {
        this.postList.add(post);
        for (User user : this.memberList) {
            user.addPostToTimeline(post);
        }
        System.out.println("> post was created");
    }

    public void removePost(Post post) {
        this.postList.remove(post);
        for (User user : this.memberList) {
            user.removePostFromTimeline(post);
        }
        System.out.println("> post was removed");
    }

    public void addAdmin(User admin) {
        if (adminList.contains(admin)) {
            System.out.println("> admin already exists");
            return;
        }
        this.adminList.add(admin);
        System.out.println("> admin was added");
    }
}

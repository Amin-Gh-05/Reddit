package org.project.reddit.content;

import org.project.reddit.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubReddit implements Serializable {
    // static list to store subreddits and their count
    public static List<SubReddit> subRedditList = new ArrayList<>();
    public static int subRedditCount = 0;

    private final List<UUID> memberList = new ArrayList<>();
    private final List<UUID> adminList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private final String topic;
    private final String createDateTime;
    private int memberCount;

    public SubReddit(String topic) {
        this.topic = topic;
        this.memberCount = 0;
        subRedditList.add(this);
        this.createDateTime = formatDateTime(LocalDateTime.now());
        subRedditCount = subRedditList.size();
    }

    // find subreddit using its unique topic
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

    // format date and time of creating subreddit and return a string
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(myFormatObj);
    }

    // getter methods
    public List<UUID> getMemberList() {
        return new ArrayList<>(this.memberList);
    }

    public List<Post> getPostList() {
        return new ArrayList<>(this.postList);
    }

    public List<UUID> getAdminList() {
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

    // add a member to subreddit
    public void addMember(User user) {
        this.memberList.add(user.getId());
        this.memberCount++;
        // if he/she is the only member, set him/her as admin
        if (memberCount == 1) {
            addAdmin(user);
        }
        System.out.println("> member was added");
    }

    // remove member from subreddit
    public void removeMember(User user) {
        this.memberList.remove(user.getId());
        this.memberCount--;
        this.adminList.remove(user.getId());
        System.out.println("> member was removed");
    }

    // add a post to subreddit
    public void addPost(Post post) {
        this.postList.add(post);
        // add post to timeline of every member of subreddit
        for (UUID id : this.memberList) {
            User user = User.findUserViaId(id);
            if (user != null) {
                user.addPostToTimeline(post);
            }
        }
        System.out.println("> post was created");
    }

    // remove a post from subreddit
    public void removePost(Post post) {
        this.postList.remove(post);
        // remove post from timeline of every member of subreddit
        for (UUID id : this.memberList) {
            User user = User.findUserViaId(id);
            if (user != null) {
                user.removePostFromTimeline(post);
            }
        }
        System.out.println("> post was removed");
    }

    // set member admin if possible
    public void addAdmin(User admin) {
        // checks if member is already an admin
        if (adminList.contains(admin.getId())) {
            System.out.println("> admin already exists");
            return;
        }
        this.adminList.add(admin.getId());
        System.out.println("> admin was added");
    }
}

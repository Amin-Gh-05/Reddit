package org.project.reddit.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    // static list to store every user created and its size
    public static List<User> userList = new ArrayList<>();
    public static int userCount = 0;

    private final List<SubReddit> subRedditList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private final List<Post> savedPostList = new ArrayList<>();
    private final List<Post> timelinePostList = new ArrayList<>();
    private final List<Comment> commentList = new ArrayList<>();
    private final List<Post> upVotedPostList = new ArrayList<>();
    private final List<Post> downVotedPostList = new ArrayList<>();
    private final List<Comment> upVotedCommentList = new ArrayList<>();
    private final List<Comment> downVotedCommentList = new ArrayList<>();
    private final UUID id;
    private String email;
    private String username;
    private String password;
    private int karma;

    public User(String email, String username, String password) {
        this.id = generateId();
        this.email = email;
        this.username = username;
        this.password = DigestUtils.sha256Hex(password);
        this.karma = 0;
        userList.add(this);
        userCount = userList.size();
    }

    // find user using unique id
    public static User findUserViaId(UUID id) {
        for (User user : userList) {
            if (user.id.equals(id)) {
                System.out.println("> id found");
                return user;
            }
        }
        System.out.println("> id not found");
        return null;
    }

    // find user using email address
    public static User findUserViaEmail(String email) {
        for (User user : userList) {
            if (user.email.equals(email)) {
                System.out.println("> email found");
                return user;
            }
        }
        System.out.println("> email not found");
        return null;
    }

    // find user using username
    public static User findUserViaUsername(String username) {
        for (User user : userList) {
            if (user.username.equals(username)) {
                System.out.println("> username found");
                return user;
            }
        }
        System.out.println("> username not found");
        return null;
    }

    // generate unique id
    private static UUID generateId() {
        UUID id = UUID.randomUUID();
        // generate new random id until it's unique
        while (findUserViaId(id) != null) {
            id = UUID.randomUUID();
        }
        System.out.println("> user id was generated");
        return id;
    }

    // checks if email is valid while signing up
    public static boolean validateEmail(String email) {
        // checks if email is repeated
        if (findUserViaEmail(email) != null) {
            System.out.println("> email already used");
            return false;
        }
        // regex pattern of email
        Pattern pattern = Pattern.compile("[\\w-.]+@[\\w-.]+\\.[\\w-]{2,4}");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    // checks if username is valid
    public static boolean validateUsername(String username) {
        // checks if username is repeated
        if (findUserViaUsername(username) != null) {
            System.out.println("> username already taken");
            return false;
        }
        // regex pattern of username
        Pattern pattern = Pattern.compile("(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    public static boolean validatePassword(String password) {
        // regex pattern of password
        Pattern pattern = Pattern.compile("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static void signUp(String email, String username, String password) {
        // checks if email is valid
        if (!validateEmail(email)) {
            System.out.println("> email not valid");
            return;
        }
        // checks if username is valid
        if (!validateUsername(username)) {
            System.out.println("> username not valid");
            return;
        }
        // checks if password is valid
        if (!validatePassword(password)) {
            System.out.println("> password not valid");
            return;
        }
        // create an instance of User class
        User user = new User(email, username, password);
        System.out.println("> user was signed up");
    }

    // getter methods
    public List<SubReddit> getSubRedditList() {
        return new ArrayList<>(this.subRedditList);
    }

    public List<Post> getPostList() {
        return new ArrayList<>(this.postList);
    }

    public List<Post> getSavedPostList() {
        return new ArrayList<>(savedPostList);
    }

    public List<Post> getTimelinePostList() {
        return new ArrayList<>(this.timelinePostList);
    }

    public List<Comment> getCommentList() {
        return new ArrayList<>(this.commentList);
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public int getKarma() {
        return this.karma;
    }

    // password checker while signing in
    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password));
    }

    // change email in profile panel
    public void changeEmail(String newEmail) {
        if (validateEmail(newEmail)) {
            this.email = newEmail;
            System.out.println("> email was updated");
        }
    }

    // change username in profile panel
    public void changeUsername(String newUsername) {
        if (validateUsername(newUsername)) {
            this.username = newUsername;
            System.out.println("> username was updated");
        }
    }

    // change password in profile panel
    public void changePassword(String newPassword) {
        if (validatePassword(newPassword)) {
            this.password = DigestUtils.sha256Hex(newPassword);
            System.out.println("> password was updated");
        }
    }

    // join subreddit
    public void joinSubReddit(SubReddit subReddit) {
        this.subRedditList.add(subReddit);
        subReddit.addMember(this);
        this.timelinePostList.addAll(subReddit.getPostList());
        System.out.println("> user joined subreddit");
    }

    // leave subreddit
    public void leaveSubReddit(SubReddit subReddit) {
        this.subRedditList.remove(subReddit);
        subReddit.removeMember(this);
        this.timelinePostList.removeAll(subReddit.getPostList());
        System.out.println("> user left subreddit");
    }

    // create a new subreddit
    public void createSubReddit(String topic) {
        SubReddit subReddit = new SubReddit(topic);
        this.subRedditList.add(subReddit);
        subReddit.addMember(this);
        subReddit.addAdmin(this);
        System.out.println("> subreddit was created");
    }

    // remove a member from subreddit if user is admin and signed in
    public void removeMember(User user, SubReddit subReddit) {
        // checks if selected user is a member of subreddit
        if (!subReddit.getMemberList().contains(user)) {
            System.out.println("> member doesn't exist for this subreddit");
            return;
        }
        // checks if user is admin of subreddit
        if (!subReddit.getAdminList().contains(this)) {
            System.out.println("> access not granted");
            return;
        }
        // checks if user wants to remove him/her self
        if (user == this) {
            System.out.println("> you can't remove yourself");
            return;
        }
        user.leaveSubReddit(subReddit);
        System.out.println("> user was removed by admin");
    }

    // set the selected user admin if possible
    public void addAdmin(User user, SubReddit subReddit) {
        // checks if selected user is a member of subreddit
        if (!subReddit.getMemberList().contains(user)) {
            System.out.println("> member doesn't exist for this subreddit");
            return;
        }
        // checks if user is admin of subreddit
        if (!subReddit.getAdminList().contains(this)) {
            System.out.println("> access not granted");
            return;
        }
        // checks if user wants to set him/her self admin
        if (user == this) {
            System.out.println("> you can't make yourself admin");
            return;
        }
        // checks if user is already admin
        if (subReddit.getAdminList().contains(user)) {
            System.out.println("> user is already an admin");
            return;
        }
        subReddit.addAdmin(user);
        System.out.println("> user was promoted to admin");
    }

    // create a new post in subreddit without tag
    public void createPost(String tite, String text, SubReddit subReddit) {
        Post post = new Post(tite, text, subReddit, this);
        this.postList.add(post);
        subReddit.addPost(post);
    }

    // create a new post in subreddit with tag
    public void createPost(List<String> tags, String tite, String text, SubReddit subReddit) {
        Post post = new Post(tags, tite, text, subReddit, this);
        this.postList.add(post);
        subReddit.addPost(post);
    }

    // remove a post if possible
    public void removePost(Post post, SubReddit subReddit) {
        // checks if post is available in subreddit
        if (!subReddit.getPostList().contains(post)) {
            System.out.println("> post doesn't exist in this subreddit");
            return;
        }
        // checks if user is admin or the publisher of post
        if (!subReddit.getAdminList().contains(this) && !this.postList.contains(post)) {
            System.out.println("> access not granted");
            return;
        }
        this.postList.remove(post);
        this.savedPostList.remove(post);
        subReddit.removePost(post);
    }

    // edit post text if possible
    public void changePostText(Post post, String newText) {
        // checks if user is the publisher of post
        if (!this.postList.contains(post)) {
            System.out.println("> access not granted");
            return;
        }
        post.changeText(newText);
        System.out.println("> post's text was edited");
    }

    public void savePost(Post post) {
        // checks if post is already saved
        if (this.savedPostList.contains(post)) {
            System.out.println("> post is already saved");
            return;
        }
        this.savedPostList.add(post);
        System.out.println("> post was saved");
    }

    public void unsavePost(Post post) {
        // checks if post is already saved
        if (!this.savedPostList.contains(post)) {
            System.out.println("> post is not saved");
            return;
        }
        this.savedPostList.remove(post);
        System.out.println("> post was unsaved");
    }

    // add post to timeline or remove it
    public void addPostToTimeline(Post post) {
        this.timelinePostList.add(post);
    }

    public void removePostFromTimeline(Post post) {
        this.timelinePostList.remove(post);
    }

    // create a new comment for the post
    public void createComment(String text, Post post) {
        Comment comment = new Comment(text, post, this);
        this.commentList.add(comment);
        post.addComment(comment);
    }

    // remove comment from the post if possible
    public void removeComment(Comment comment, Post post) {
        // checks if post is available for the post
        if (!post.getCommentList().contains(comment)) {
            System.out.println("> comment doesn't exist for this post");
            return;
        }
        // checks if user is admin of subreddit or publisher of comment
        if (!post.getSubReddit().getAdminList().contains(this) && !commentList.contains(comment)) {
            System.out.println("> access not granted");
            return;
        }
        this.commentList.remove(comment);
        post.removeComment(comment);
    }

    // edit comment text if possible
    public void changeCommentText(Comment comment, String newText) {
        // checks if comment is published by user
        if (!this.commentList.contains(comment)) {
            System.out.println("> access not granted");
            return;
        }
        comment.changeText(newText);
    }

    // increase or decrease total comment of user
    public void increaseKarma() {
        this.karma++;
        System.out.println("> user's karma increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> user's karma decreased");
    }

    // upvote/downvote post/comment
    public void upVote(Post post) {
        if (this.upVotedPostList.contains(post)) {
            System.out.println("> post is already upvoted by user");
            return;
        }
        if (this.downVotedPostList.contains(post)) {
            this.downVotedPostList.remove(post);
            post.increaseKarma();
            post.getUser().increaseKarma();
        }
        this.upVotedPostList.add(post);
        post.increaseKarma();
        post.getUser().increaseKarma();
    }

    public void upVote(Comment comment) {
        if (this.upVotedCommentList.contains(comment)) {
            System.out.println("> comment is already upvoted by user");
            return;
        }
        if (this.downVotedCommentList.contains(comment)) {
            this.downVotedCommentList.remove(comment);
            comment.increaseKarma();
            comment.getUser().increaseKarma();
        }
        this.upVotedCommentList.add(comment);
        comment.increaseKarma();
        comment.getUser().increaseKarma();
    }

    public void downVote(Post post) {
        if (this.downVotedPostList.contains(post)) {
            System.out.println("> post is already downvoted by user");
            return;
        }
        if (this.upVotedPostList.contains(post)) {
            this.upVotedPostList.remove(post);
            post.decreaseKarma();
            post.getUser().decreaseKarma();
        }
        this.downVotedPostList.add(post);
        post.decreaseKarma();
        post.getUser().decreaseKarma();
    }

    public void downVote(Comment comment) {
        if (this.downVotedCommentList.contains(comment)) {
            System.out.println("> comment is already downvoted by user");
            return;
        }
        if (this.upVotedCommentList.contains(comment)) {
            this.upVotedCommentList.remove(comment);
            comment.decreaseKarma();
            comment.getUser().decreaseKarma();
        }
        this.downVotedCommentList.add(comment);
        comment.decreaseKarma();
        comment.getUser().decreaseKarma();
    }
}

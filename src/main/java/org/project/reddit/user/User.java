package org.project.reddit.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private static final List<User> userList = new ArrayList<>();
    private static int userCount = 0;
    private final UUID id;
    private final List<SubReddit> subRedditList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private final List<Post> savedPostList = new ArrayList<>();
    private final List<Comment> commentList = new ArrayList<>();
    private final List<Post> upVotedPostList = new ArrayList<>();
    private final List<Post> downVotedPostList = new ArrayList<>();
    private final List<Comment> upVotedCommentList = new ArrayList<>();
    private final List<Comment> downVotedCommentList = new ArrayList<>();
    private String email;
    private String username;
    private String password;
    private int karma;

    public User(String email, String username, String password) {
        userCount++;
        this.id = generateId();
        this.email = email;
        this.username = username;
        this.password = DigestUtils.sha256Hex(password);
        this.karma = 0;
    }

    public static int getUserCount() {
        System.out.println("> user count refreshed");
        return userCount;
    }

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

    private static UUID generateId() {
        UUID id = UUID.randomUUID();
        while (findUserViaId(id) != null) {
            id = UUID.randomUUID();
        }
        System.out.println("> user id was generated");
        return id;
    }

    public static boolean validateEmail(String email) {
        if (findUserViaEmail(email) != null) {
            System.out.println("> email already used");
            return false;
        }
        Pattern pattern = Pattern.compile("[\\w-.]+@[\\w-.]+\\.[\\w-]{2,4}");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static boolean validateUsername(String username) {
        if (findUserViaUsername(username) != null) {
            System.out.println("> username already taken");
            return false;
        }
        Pattern pattern = Pattern.compile("(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static void signUp(String email, String username, String password) {
        if (!validateEmail(email)) {
            System.out.println("> email not valid");
            return;
        }
        if (!validateUsername(username)) {
            System.out.println("> username not valid");
            return;
        }
        if (!validatePassword(password)) {
            System.out.println("> password not valid");
            return;
        }
        User user = new User(email, username, password);
        userList.add(user);
        System.out.println("> user was signed up");
    }

    public List<SubReddit> getSubRedditList() {
        return new ArrayList<>(this.subRedditList);
    }

    public List<Post> getPostList() {
        return new ArrayList<>(this.postList);
    }

    public List<Post> getSavedPostList() {
        return new ArrayList<>(savedPostList);
    }

    public List<Comment> getCommentList() {
        return new ArrayList<>(this.commentList);
    }

    public int getKarma() {
        return this.karma;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password));
    }

    public void changeEmail(String newEmail) {
        if (validateEmail(newEmail)) {
            this.email = newEmail;
            System.out.println("> email was updated");
        }
    }

    public void changeUsername(String newUsername) {
        if (validateUsername(newUsername)) {
            this.username = newUsername;
            System.out.println("> username was updated");
        }
    }

    public void changePassword(String newPassword) {
        if (validatePassword(newPassword)) {
            this.password = DigestUtils.sha256Hex(newPassword);
            System.out.println("> password was updated");
        }
    }

    public void joinSubReddit(SubReddit subReddit) {
        this.subRedditList.add(subReddit);
        subReddit.addMember(this);
        System.out.println("> user joined subreddit");
    }

    public void leaveSubReddit(SubReddit subReddit) {
        this.subRedditList.remove(subReddit);
        subReddit.removeMember(this);
        System.out.println("> user left subreddit");
    }

    public void createSubReddit(String topic) {
        SubReddit subReddit = new SubReddit(topic, this);
        this.subRedditList.add(subReddit);
        subReddit.addMember(this);
        subReddit.addAdmin(this);
        System.out.println("> subreddit was created");
    }

    public void removeMember(User user, SubReddit subReddit) {
        if (!subReddit.getMemberList().contains(user)) {
            System.out.println("> member doesn't exist for this subreddit");
            return;
        }
        if (!subReddit.getAdminList().contains(user)) {
            System.out.println("> access not granted");
            return;
        }
        user.leaveSubReddit(subReddit);
        System.out.println("> user was removed by admin");
    }

    public void createPost(String tite, String text, SubReddit subReddit) {
        Post post = new Post(tite, text, subReddit, this);
        this.postList.add(post);
        subReddit.addPost(post);
    }

    public void createPost(List<String> tags, String tite, String text, SubReddit subReddit) {
        Post post = new Post(tags, tite, text, subReddit, this);
        this.postList.add(post);
        subReddit.addPost(post);
    }

    public void removePost(Post post, SubReddit subReddit) {
        if (!subReddit.getPostList().contains(post)) {
            System.out.println("> post doesn't exist in this subreddit");
            return;
        }
        if (!subReddit.getAdminList().contains(this) && !this.postList.contains(post)) {
            System.out.println("> access not granted");
            return;
        }
        this.postList.remove(post);
        subReddit.removePost(post);
    }

    public void changePostTitle(Post post, String newTitle) {
        if (!this.getPostList().contains(post)) {
            System.out.println("> access not granted");
            return;
        }
        post.changeTitle(newTitle);
    }

    public void changePostText(Post post, String newText) {
        if (!this.postList.contains(post)) {
            System.out.println("> access not granted");
            return;
        }
        post.changeText(newText);
        System.out.println("> post's text was edited");
    }

    public void savePost(Post post) {
        this.savedPostList.add(post);
        System.out.println("> post was saved");
    }

    public void createComment(String text, Post post) {
        Comment comment = new Comment(text, post, this);
        this.commentList.add(comment);
        post.addComment(comment);
    }

    public void removeComment(Comment comment, Post post) {
        if (!post.getCommentList().contains(comment)) {
            System.out.println("> comment doesn't exist for this post");
            return;
        }
        if (!post.getSubReddit().getAdminList().contains(this) && !commentList.contains(comment)) {
            System.out.println("> access not granted");
            return;
        }
        this.commentList.remove(comment);
        post.removeComment(comment);
    }

    public void changeCommentText(Comment comment, String newText) {
        if (!this.commentList.contains(comment)) {
            System.out.println("> access not granted");
            return;
        }
        comment.changeText(newText);
    }

    public void replyComment(Comment comment, String text) {
        Comment reply = new Comment(text, comment.getPost(), comment.getUser());
        comment.replyComment(reply);
    }

    public void increaseKarma() {
        this.karma++;
        System.out.println("> user's karma increased");
    }

    public void decreaseKarma() {
        this.karma--;
        System.out.println("> user's karma decreased");
    }

    public void upVote(Post post) {
        if (this.upVotedPostList.contains(post)) {
            return;
        }
        if (this.downVotedPostList.contains(post)) {
            this.downVotedPostList.remove(post);
            post.getUser().increaseKarma();
        }
        this.upVotedPostList.add(post);
        post.increaseKarma();
        post.getUser().increaseKarma();
    }

    public void upVote(Comment comment) {
        if (this.upVotedCommentList.contains(comment)) {
            return;
        }
        if (this.downVotedCommentList.contains(comment)) {
            this.downVotedCommentList.remove(comment);
            comment.getUser().increaseKarma();
        }
        this.upVotedCommentList.add(comment);
        comment.increaseKarma();
        comment.getUser().increaseKarma();
    }

    public void downVote(Post post) {
        if (this.downVotedPostList.contains(post)) {
            return;
        }
        if (this.upVotedPostList.contains(post)) {
            this.upVotedPostList.remove(post);
            post.getUser().decreaseKarma();
        }
        this.downVotedPostList.add(post);
        post.decreaseKarma();
        post.getUser().decreaseKarma();
    }

    public void downVote(Comment comment) {
        if (this.downVotedCommentList.contains(comment)) {
            return;
        }
        if (this.upVotedCommentList.contains(comment)) {
            this.upVotedCommentList.remove(comment);
            comment.getUser().decreaseKarma();
        }
        this.downVotedCommentList.add(comment);
        comment.decreaseKarma();
        comment.getUser().decreaseKarma();
    }
}

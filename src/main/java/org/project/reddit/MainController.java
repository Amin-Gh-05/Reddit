package org.project.reddit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

public class MainController {

    @FXML
    private Label userCount;
    @FXML
    private Label subredditCount;
    @FXML
    private Label postCount;
    @FXML
    private Label commentCount;
    @FXML
    private TextField searchText;

    public void setUserCount() {
        userCount.setText(String.valueOf(User.getUserCount()));
    }

    public void setSubredditCount() {
        subredditCount.setText(String.valueOf(SubReddit.getSubRedditCount()));
    }

    public void setPostCount() {
        postCount.setText(String.valueOf(Post.getPostCount()));
    }

    public void setCommentCount() {
        commentCount.setText(String.valueOf(Comment.getCommentCount()));
    }

    @FXML
    public void searchAll(ActionEvent event) {

    }

    @FXML
    public void openSignUpPage(ActionEvent event) {

    }

    @FXML
    public void openLogInPage(ActionEvent event) {

    }

    @FXML
    public void refreshTrendingPosts(ActionEvent event) {

    }
}

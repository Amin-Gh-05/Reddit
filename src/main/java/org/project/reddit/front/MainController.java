package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

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

    @FXML
    private ListView<String> trendingPostList;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        userCount.setText(String.valueOf(User.getUserCount()));
        subredditCount.setText(String.valueOf(SubReddit.getSubRedditCount()));
        postCount.setText(String.valueOf(Post.getPostCount()));
        commentCount.setText(String.valueOf(Comment.getCommentCount()));
        trendingPostList.getItems().addAll(Post.getTrendingPosts());
        System.out.println("> main panel got initialized");
    }

    @FXML
    void setUserCount(MouseEvent event) {
        userCount.setText(String.valueOf(User.getUserCount()));
    }

    @FXML
    void setSubredditCount(MouseEvent event) {
        subredditCount.setText(String.valueOf(SubReddit.getSubRedditCount()));
    }

    @FXML
    void setPostCount(MouseEvent event) {
        postCount.setText(String.valueOf(Post.getPostCount()));
    }

    @FXML
    void setCommentCount(MouseEvent event) {
        commentCount.setText(String.valueOf(Comment.getCommentCount()));
    }

    @FXML
    void openSignUpPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/signup-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to signup page");
    }

    @FXML
    void openLogInPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/login-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to login page");
    }

    @FXML
    void refreshTrendingPosts(ActionEvent event) {
        trendingPostList.getItems().addAll(Post.getTrendingPosts());
        System.out.println("> trending post list refreshed");
    }

    @FXML
    void searchAll(ActionEvent event) {

    }
}

package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML
    private TabPane tabsPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshAll();
        System.out.println("> main panel got initialized");
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
    void refreshAll() {
        this.userCount.setText(String.valueOf(User.getUserCount()));
        this.subredditCount.setText(String.valueOf(SubReddit.getSubRedditCount()));
        this.postCount.setText(String.valueOf(Post.getPostCount()));
        this.commentCount.setText(String.valueOf(Comment.getCommentCount()));
        this.trendingPostList.getItems().clear();
        this.trendingPostList.getItems().addAll(Post.getTrendingPosts());
        System.out.println("> main panel refreshed");
    }

    @FXML
    void searchAll() {
        if (searchText.getText().startsWith("r/")) {
            SubReddit subReddit = SubReddit.findSubReddit(searchText.getText().substring(2));
            if (subReddit == null) {
                System.out.println("> Invalid topic");
                return;
            }
            loadSubreddit(subReddit);
        } else if (searchText.getText().startsWith("u/")) {
            User user = User.findUserViaUsername(searchText.getText().substring(2));
            if (user == null) {
                System.out.println("> Invalid username");
                return;
            }
        } else {
            System.out.println("> Invalid input");
        }
    }

    void loadSubreddit(SubReddit subReddit) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/subreddit-view.fxml"));
        try {
            Node node = loader.load();
            SubredditController controller = loader.getController();
            controller.subReddit = subReddit;
            controller.topicText.setText(subReddit.getTopic());
            controller.memberCount.setText("Members: " + subReddit.getMemberCount());
            controller.dateText.setText(subReddit.getCreateDateTime().substring(0, 10));
            controller.joinButton.setVisible(false);
            controller.createPostPane.setVisible(false);
            controller.refreshSubreddit();
            Tab subredditTab = new Tab(subReddit.getTopic());
            subredditTab.setClosable(true);
            subredditTab.setContent(node);
            tabsPane.getTabs().add(subredditTab);
            tabsPane.getSelectionModel().select(subredditTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

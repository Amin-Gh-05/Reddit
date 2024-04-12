package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public static User user;

    @FXML
    private ListView<String> savedPostList;

    @FXML
    private Label subredditCount;

    @FXML
    private Label postCount;

    @FXML
    private Label commentCount;

    @FXML
    private TextField searchText;

    @FXML
    private VBox postBox;

    @FXML
    private TabPane tabsPane;

    @FXML
    private TextField topicText;

    @FXML
    private Label invalidAlert;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshAll();
        System.out.println("> user panel got initialized");
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Returning to main panel");
        alert.setContentText("Are you sure you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            user = null;
            ProfileController.user = null;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            System.out.println("> redirect to main panel");
        }
    }

    @FXML
    void searchAll(ActionEvent event) {
        if (this.searchText.getText().startsWith("r/")) {
            SubReddit subReddit = SubReddit.findSubReddit(this.searchText.getText().substring(2));
            if (subReddit == null) {
                System.out.println("> Invalid topic");
                return;
            }
            loadSubreddit(subReddit);
        } else if (this.searchText.getText().startsWith("u/")) {
            User user = User.findUserViaUsername(this.searchText.getText().substring(2));
            if (user == null) {
                System.out.println("> Invalid username");
                return;
            }
            loadUser(user);
        } else {
            System.out.println("> Invalid input");
        }
    }

    @FXML
    void viewProfile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/profile-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to profile panel");
    }

    @FXML
    void refreshAll() {
        this.subredditCount.setText(String.valueOf(user.getSubRedditList().size()));
        this.postCount.setText(String.valueOf(user.getPostList().size()));
        this.commentCount.setText(String.valueOf(user.getCommentList().size()));
        int size = user.getSavedPostList().size();
        String[] savedPosts = new String[size];
        for (int i = 0; i < size; i++) {
            savedPosts[i] = user.getSavedPostList().get(i).getTitle();
        }
        this.savedPostList.getItems().clear();
        this.savedPostList.getItems().addAll(savedPosts);
        refreshTimeline();
        invalidAlert.setVisible(false);
        System.out.println("> user panel got refreshed");
    }

    @FXML
    void createSubreddit() {
        for (SubReddit subReddit : SubReddit.subRedditList) {
            if (subReddit.getTopic().equals(topicText.getText())) {
                System.out.println("> subreddit already exists");
                invalidAlert.setVisible(true);
                return;
            }
        }
        user.createSubReddit(topicText.getText());
        topicText.clear();
        refreshAll();
    }

    void refreshTimeline() {
        postBox.getChildren().remove(1, postBox.getChildren().size());
        int size = user.getTimelinePostList().size();
        Post[] timelinePosts = new Post[size];
        if (size >= 10) {
            for (int i = size - 1; i >= size - 10; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        }
        for (Post post : timelinePosts) {
            this.postBox.getChildren().add(getPostLayout(post));
        }
    }

    Node getPostLayout(Post post) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            controller.userController = this;
            controller.usernameText.setText(post.getUser().getUsername());
            controller.karmaCount.setText("Karma: " + post.getKarma());
            controller.dateTimeText.setText(post.getCreateDateTime());
            controller.topicText.setText(post.getTitle());
            controller.textBody.setText(post.getText());
            if (!post.getTagList().isEmpty()) {
                String tags = "";
                for (String tag : post.getTagList()) {
                    tags += "#" + tag;
                }
                controller.tagsText.setText(tags);
            } else {
                controller.tagsText.setVisible(false);
            }
            controller.refreshComments();
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            if (controller.subReddit.getMemberList().contains(user)) {
                controller.joinButton.setVisible(false);
            } else {
                controller.leaveButton.setVisible(false);
            }
            controller.refreshAll();
            Tab subredditTab = new Tab(subReddit.getTopic());
            subredditTab.setClosable(true);
            subredditTab.setContent(node);
            this.tabsPane.getTabs().add(subredditTab);
            this.tabsPane.getSelectionModel().select(subredditTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    void loadUser(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/show-view.fxml"));
        try {
            Node node = loader.load();
            ShowController controller = loader.getController();
            controller.user = user;
            controller.usernameText.setText(user.getUsername());
            controller.karmaCount.setText("Karma: " + user.getKarma());
            controller.refreshUser();
            Tab userTab = new Tab(user.getUsername());
            userTab.setClosable(true);
            userTab.setContent(node);
            this.tabsPane.getTabs().add(userTab);
            this.tabsPane.getSelectionModel().select(userTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
